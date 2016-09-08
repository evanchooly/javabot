package javabot.javadoc

import com.jayway.awaitility.Awaitility
import javabot.JavabotConfig
import javabot.JavabotThreadFactory
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import org.bson.types.ObjectId
import org.jboss.forge.roaster.Roaster
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.io.Writer
import java.nio.charset.Charset
import java.util.Collections
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS
import java.util.jar.JarFile
import javax.inject.Inject
import javax.inject.Provider
import javax.tools.ToolProvider

class JavadocParser @Inject constructor(val apiDao: ApiDao, val javadocClassDao: JavadocClassDao,
                                        val provider: Provider<JavadocClassParser>, val config: JavabotConfig) {


    fun parse(api: JavadocApi, location: File, writer: Writer) {
        try {
            val workQueue = LinkedBlockingQueue<Runnable>(100)
            val executor = ThreadPoolExecutor(10, 20, 10, SECONDS, workQueue, JavabotThreadFactory(false, "javadoc-thread-"))
            executor.prestartCoreThread()
            val packages = if ("JDK" == api.name) listOf("java", "javax") else listOf()

            try {
                JarFile(location).use { jarFile ->
                    Collections.list(jarFile.entries())
                            .filter { it.name.endsWith(".java") && (packages.isEmpty()
                                    || packages.any { pkg -> it.name.startsWith(pkg) }) }
                            .map {
                                var text: String = ""
                                jarFile.getInputStream(it).use {
                                    text = it.readBytes().toString(Charset.forName("UTF-8"))
                                }
                                text
                            }
                            .forEach { text ->
                                if (!workQueue.offer(JavadocClassReader(api, text), 30, TimeUnit.SECONDS)) {
                                    JavadocClassReader(api, text).run()
                                }
                            }
                }

                buildHtml(api, location, packages)

                Awaitility
                    .waitAtMost(30, MINUTES)
                    .pollInterval(5, SECONDS)
                    .until<Boolean> {
                        writer.write("Waiting on %s work queue to drain.  %d items left".format(api.name, workQueue.size))
                        workQueue.isEmpty()
                    }
                executor.shutdown()
                executor.awaitTermination(10, TimeUnit.MINUTES)

            } finally {
                location.delete()
            }
            writer.write("Finished importing %s.  %s!".format(api.name, if (workQueue.isEmpty()) "SUCCESS" else "FAILURE"))
        } catch (e: IOException) {
            log.error(e.message, e)
            throw RuntimeException(e.message, e)
        } catch (e: InterruptedException) {
            log.error(e.message, e)
            throw RuntimeException(e.message, e)
        }

    }

    fun buildHtml(api: JavadocApi, file: File, packages: List<String>) {
        buildJavadocHtml(packages, file, File("javadoc/${api.name}/${api.version}/"))
    }

    private fun buildJavadocHtml(packages: List<String>, jar: File, javadocDir: File) {
        var tmp = File("/tmp/")
        if (!tmp.exists()) {
            tmp = File(System.getProperty("java.io.tmpdir"))
        }
        val jarTarget = File(tmp, ObjectId().toString())

        jarTarget.mkdirs()
        javadocDir.mkdirs()

        val byteArrayOutputStream = ByteArrayOutputStream()
        val printStream = PrintStream(byteArrayOutputStream)
        try {
            extractJar(jar, jarTarget)
            val packageNames = if (!packages.isEmpty())
                packages
            else jarTarget
                    .listFiles { it -> it.isDirectory && it.name != "META-INF" }
                    .map { it.name }
            Awaitility
                .await()
                .atMost(30, MINUTES)
                .until {
                    ToolProvider.getSystemDocumentationTool().run(null, printStream, printStream,
                            "-d", javadocDir.absolutePath,
                            "-subpackages", packageNames.joinToString(":"),
                            "-protected",
                            "-use",
                            "-quiet",
                            "-sourcepath", jarTarget.absolutePath)
                }
        } catch (e : Exception) {
            log.error(e.message, e)
            log.error(byteArrayOutputStream.toString("UTF-8"))
        } finally {
            jarTarget.deleteRecursively()
        }
    }

    private fun extractJar(jar: File, jarTarget: File) {
        val jarFile = JarFile(jar)
        jarFile.entries().iterator().forEach { entry ->
            if (!entry.isDirectory) {
                val javaFile = File(jarTarget, entry.name)
                javaFile.parentFile.mkdirs()
                FileOutputStream(javaFile).use {
                    jarFile.getInputStream(entry).copyTo(it)
                }
            }
        }
    }

    fun getJavadocClass(api: JavadocApi, fqcn: String): JavadocClass {
        val pkgName = getPackage(fqcn)
        val parentName = fqcn.split('.').last()
        return getJavadocClass(api, pkgName, parentName)
    }

    fun getJavadocClass(api: JavadocApi, pkg: String, name: String): JavadocClass {
        var javadocClass = javadocClassDao.getClass(api, pkg, name)
        if (javadocClass == null) {
            javadocClass = JavadocClass(api, pkg, name)
            javadocClassDao.save(javadocClass)
        }
        return javadocClass
    }

    private inner class JavadocClassReader(private val api: JavadocApi, val text: String) : Runnable {

        override fun run() {
            try {
                val source = Roaster.parse(text)
                val parser = provider.get()
                val packages = if ("JDK" == api.name) arrayOf("java", "javax") else arrayOf<String>()
                parser.parse(api, source, *packages)
            } catch (e: Exception) {
                throw RuntimeException(e.message, e)
            }

        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(JavadocParser::class.java)

        fun getPackage(name: String): String {
            return name.split('.').dropLast(1).joinToString(".")
        }
    }
}
