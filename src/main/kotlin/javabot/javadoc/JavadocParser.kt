package javabot.javadoc

import javabot.JavabotThreadFactory
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import org.objectweb.asm.ClassReader
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.io.Writer
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.jar.JarEntry
import java.util.jar.JarFile
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
public class JavadocParser {

    @Inject
    lateinit var apiDao: ApiDao

    @Inject
    lateinit var javadocClassDao: JavadocClassDao

    @Inject
    lateinit var provider: Provider<JavadocClassVisitor>

    lateinit var api: JavadocApi
        private set

    private val deferred = HashMap<String, MutableList<JavadocClass>?>()

    public fun parse(classApi: JavadocApi, location: String, writer: Writer) {
        api = classApi
        try {
            val tmpDir = File("/tmp")
            if (!tmpDir.exists()) {
                File(System.getProperty("java.io.tmpdir"))
            }

            val workQueue = LinkedBlockingQueue<Runnable>()
            val executor = ThreadPoolExecutor(20, 30, 30, TimeUnit.SECONDS, workQueue,
                  JavabotThreadFactory(false, "javadoc-thread-"))
            executor.prestartCoreThread()
            val file = File(location)
            val deleteFile = "JDK" != api.name
            try {
                JarFile(file).use { jarFile ->
                    val entries = jarFile.entries()
                    while (entries.hasMoreElements()) {
                        val entry = entries.nextElement()
                        if (entry.name.endsWith(".class")) {
                            if (!workQueue.offer(JavadocClassReader(jarFile, entry), 1, TimeUnit.MINUTES)) {
                                writer.write("Failed to class to queue: " + entry)
                            }
                        }
                    }
                    while (!workQueue.isEmpty()) {
                        writer.write("Waiting on %s work queue to drain.  %d items left".format(api.name, workQueue.size()))
                        Thread.sleep(5000)
                    }
                }
            } finally {
                if (deleteFile) {
                    file.delete()
                }
            }
            executor.shutdown()
            executor.awaitTermination(1, TimeUnit.HOURS)
            writer.write("Finished importing %s.  %s!".format(api.name, if (workQueue.isEmpty()) "SUCCESS" else "FAILURE"))
        } catch (e: IOException) {
            log.error(e.getMessage(), e)
            throw RuntimeException(e.getMessage(), e)
        } catch (e: InterruptedException) {
            log.error(e.getMessage(), e)
            throw RuntimeException(e.getMessage(), e)
        }

    }

    public fun getOrQueue(api: JavadocApi, pkg: String, name: String, newClass: JavadocClass): JavadocClass? {
        synchronized (deferred) {
            val parent = getJavadocClass(api, pkg, name)
            if (parent == null) {
                val fqcn = pkg + "." + name
                var list: MutableList<JavadocClass>? = deferred.get(fqcn)
                if (list == null) {
                    list = ArrayList<JavadocClass>()
                    deferred.put(fqcn, list)
                }
                list.add(newClass)
            }
            return parent
        }
    }

    public fun getOrCreate(api: JavadocApi, pkg: String, name: String): JavadocClass {
        synchronized (deferred) {
            var cls = getJavadocClass(api, pkg, name)
            if (cls == null) {
                cls = JavadocClass(api, pkg, name)
                javadocClassDao.save(cls)
            }
            val list = deferred.get(pkg + "." + name)
            if (list != null) {
                for (subclass in list) {
                    subclass.superClassId = cls.superClassId
                    javadocClassDao.save(subclass)
                }
                deferred.remove(pkg + "." + name)
            }
            return cls
        }
    }

    private fun getJavadocClass(api: JavadocApi, pkg: String, name: String): JavadocClass? {
        var cls: JavadocClass? = null
        for (javadocClass in javadocClassDao.getClass(api, pkg, name)) {
            if (javadocClass.apiId == api.id) {
                cls = javadocClass
            }
        }
        return cls
    }

    private inner class JavadocClassReader(private val jarFile: JarFile, private val entry: JarEntry) : Runnable {

        override fun run() {
            try {
                val classVisitor = provider.get()
                if ("JDK" == api.name) {
                    classVisitor.setPackages("java", "javax")
                }
                ClassReader(jarFile.getInputStream(entry)).accept(classVisitor, 0)
            } catch (e: Exception) {
                throw RuntimeException(e.getMessage(), e)
            }

        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(JavadocParser::class.java)
    }
}
