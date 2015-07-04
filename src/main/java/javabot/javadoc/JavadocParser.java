package javabot.javadoc;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import javabot.JavabotThreadFactory;
import javabot.dao.ApiDao;
import javabot.dao.JavadocClassDao;
import org.objectweb.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class JavadocParser {
  private static final Logger log = LoggerFactory.getLogger(JavadocParser.class);

  @Inject
  private ApiDao apiDao;

  @Inject
  private JavadocClassDao dao;

  @Inject
  private Provider<JavadocClassVisitor> provider;

  private JavadocApi api;

  private final Map<String, List<JavadocClass>> deferred = new HashMap<>();

  public void parse(final JavadocApi classApi, String location, final Writer writer) {
    api = classApi;
    try {
      File tmpDir = new File("/tmp");
      if (!tmpDir.exists()) {
        new File(System.getProperty("java.io.tmpdir"));
      }

      BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
      ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 30, 30, TimeUnit.SECONDS, workQueue,
          new JavabotThreadFactory(false, "javadoc-thread-"));
      executor.prestartCoreThread();
      final File file = new File(location);
      final boolean deleteFile = !"JDK".equals(api.getName());
      try (JarFile jarFile = new JarFile(file)) {
        for (final Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
          final JarEntry entry = entries.nextElement();
          if (entry.getName().endsWith(".class")) {
            if (!workQueue.offer(new JavadocClassReader(jarFile, entry), 1, TimeUnit.MINUTES)) {
              writer.write("Failed to class to queue: " + entry);
            }
          }
        }
        while (!workQueue.isEmpty()) {
          writer.write(String.format("Waiting on %s work queue to drain.  %d items left", api.getName(),
              workQueue.size()));
          Thread.sleep(5000);
        }
      } finally {
        if (deleteFile) {
          file.delete();
        }
      }
      executor.shutdown();
      executor.awaitTermination(1, TimeUnit.HOURS);
      writer.write(String.format("Finished importing %s.  %s!", api.getName(),
          workQueue.isEmpty() ? "SUCCESS" : "FAILURE"));
    } catch (IOException | InterruptedException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public JavadocClass getOrQueue(final JavadocApi api, final String pkg, final String name,
      final JavadocClass newClass) {
    synchronized (deferred) {
      JavadocClass parent = getJavadocClass(api, pkg, name);
      if (parent == null) {
        final String fqcn = pkg + "." + name;
        List<JavadocClass> list = deferred.get(fqcn);
        if (list == null) {
          list = new ArrayList<>();
          deferred.put(fqcn, list);
        }
        list.add(newClass);
      }
      return parent;
    }
  }

  public JavadocClass getOrCreate(final JavadocApi api, final String pkg, final String name) {
    synchronized (deferred) {
      JavadocClass cls = getJavadocClass(api, pkg, name);
      if (cls == null) {
        cls = new JavadocClass(api, pkg, name);
        dao.save(cls);
      }
      final List<JavadocClass> list = deferred.get(pkg + "." + name);
      if (list != null) {
        for (JavadocClass subclass : list) {
          subclass.setSuperClassId(cls.getSuperClassId());
          dao.save(subclass);
        }
        deferred.remove(pkg + "." + name);
      }
      return cls;
    }
  }

  private JavadocClass getJavadocClass(final JavadocApi api, final String pkg, final String name) {
    JavadocClass cls = null;
    for (JavadocClass javadocClass : dao.getClass(api, pkg, name)) {
      if (javadocClass.getApiId().equals(api.getId())) {
        cls = javadocClass;
      }
    }
    return cls;
  }

  public JavadocApi getApi() {
    return api;
  }

  private class JavadocClassReader implements Runnable {
    private final JarFile jarFile;

    private final JarEntry entry;

    public JavadocClassReader(final JarFile jarFile, final JarEntry entry) {
      this.jarFile = jarFile;
      this.entry = entry;
    }

    @Override
    public void run() {
      try {
        JavadocClassVisitor classVisitor = provider.get();
        if("JDK".equals(api.getName())) {
          classVisitor.setPackages("java", "javax");
        }
        new ClassReader(jarFile.getInputStream(entry)).accept(classVisitor, 0);
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }
  }
}
