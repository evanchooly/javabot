package javabot.javadoc;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import javabot.JavabotThreadFactory;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.ClassReader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created Jan 9, 2009
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 * @noinspection unchecked
 */
@Component
public class JavadocParser {
  private static final Logger log = LoggerFactory.getLogger(JavadocParser.class);
//  @Autowired
  private ApiDao apiDao;
  private ClazzDao dao;
  private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
  private final ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 30, 30, TimeUnit.SECONDS, workQueue,
    new JavabotThreadFactory(false, "javadoc-thread-"));
  private Api api;
  private List<String> packages;
  private final Map<String, List<Clazz>> deferred = new HashMap<String, List<Clazz>>();

  @Transactional
  public void parse(final Api classApi, String location, final Writer writer) {
    api = classApi;
    try {
      File tmpDir = new File("/tmp");
      if (!tmpDir.exists()) {
        new File(System.getProperty("java.io.tmpdir"));
      }
      executor.prestartCoreThread();
      File file = new File(location);
      try (JarFile jarFile = new JarFile(file)) {
        for (final Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
          final JarEntry entry = entries.nextElement();
          if (entry.getName().endsWith(".class")) {
            workQueue.offer(new Runnable() {
              @Override
              public void run() {
                try {
                  JavadocClassVisitor visitor = new JavadocClassVisitor(JavadocParser.this, dao, apiDao);
                  new ClassReader(jarFile.getInputStream(entry)).accept(visitor, true);
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                  throw new RuntimeException(e.getMessage(), e);
                }
              }
            }, 1, TimeUnit.MINUTES);
          }
        }
        while (!workQueue.isEmpty()) {
          writer.write(String.format("Waiting on %s work queue to drain.  %d items left", api.getName(),
              workQueue.size()));
          Thread.sleep(5000);
        }
        executor.shutdown();
      } finally {
        file.delete();
        writer.write(String.format("Finished importing %s.  %s!", api.getName(),
            workQueue.isEmpty() ? "SUCCESS" : "FAILURE"));
      }
    } catch (IOException e) {
      log.debug(e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
  }

  public boolean acceptPackage(final String pkg) {
    if (packages == null) {
      packages = api.getPackages() == null ? Collections.<String>emptyList()
        : Arrays.asList(api.getPackages().split(","));
    }
    if (packages.isEmpty()) {
      return true;
    }
    for (String pakg : packages) {
      if (pkg.startsWith(pakg)) {
        return true;
      }
    }
    return false;
  }

  public Clazz getOrQueue(final Api api, final String pkg, final String name, final Clazz newClazz) {
    final Clazz[] clazzes = dao.getClass(pkg, name);
    synchronized (deferred) {
      Clazz parent = null;
      for (Clazz clazz : clazzes) {
        if (clazz.getApi().getId().equals(api.getId())) {
          parent = clazz;
        }
      }
      if (parent == null) {
        final String fqcn = pkg + "." + name;
        List<Clazz> list = deferred.get(fqcn);
        if (list == null) {
          list = new ArrayList<Clazz>();
          deferred.put(fqcn, list);
        }
        list.add(newClazz);
      }
      return parent;
    }
  }

  public Clazz getOrCreate(final Api api, final String pkg, final String name) {
    final Clazz[] clazzes = dao.getClass(pkg, name);
    synchronized (deferred) {
      Clazz cls = null;
      for (Clazz clazz : clazzes) {
        if (clazz.getApi().getId().equals(api.getId())) {
          cls = clazz;
        }
      }
      if (cls == null) {
        cls = new Clazz(api, pkg, name);
        dao.save(cls);
      }
      final List<Clazz> list = deferred.get(pkg + "." + name);
      if (list != null) {
        for (Clazz subclazz : list) {
          subclazz.setSuperClassId(cls);
          dao.save(subclazz);
        }
        deferred.remove(pkg + "." + name);
      }
      return cls;
    }
  }

  public Api getApi() {
    return api;
  }
}
