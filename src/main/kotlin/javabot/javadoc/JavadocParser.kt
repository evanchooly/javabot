package javabot.javadoc

import com.jayway.awaitility.Awaitility
import javabot.JavabotConfig
import javabot.JavabotThreadFactory
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.javadoc.Visibility.PackagePrivate
import javabot.javadoc.Visibility.Private
import javabot.javadoc.Visibility.Protected
import javabot.javadoc.Visibility.Public
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
import java.util.concurrent.ScheduledThreadPoolExecutor
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
            val executor = ScheduledThreadPoolExecutor(100, JavabotThreadFactory(false, "javadoc-thread-"))
            executor.prestartCoreThread()

            val packages = if ("JDK" == api.name) listOf("java", "javax") else listOf()
            executor.submit {
                buildHtml(api, location, packages)
            }
            (1..executor.corePoolSize).forEach {
               executor.scheduleAtFixedRate(JavadocClassReader(api, apiDao), 0, 1, SECONDS)
            }

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
                                JavadocSource(it.name, api, text)
                            }
                            .forEach { source ->
                                apiDao.save(source)
                            }
                }

                Awaitility
                    .waitAtMost(30, MINUTES)
                    .pollInterval(5, SECONDS)
                    .until<Boolean> {
                        val workQueue = apiDao.countUnprocessed(api)
                        writer.write("Waiting on %s work queue to drain.  %d items left".format(api.name, workQueue))
                        workQueue == 0L
                    }
                executor.shutdown()
                executor.awaitTermination(10, TimeUnit.MINUTES)
                javadocClassDao.deleteNotVisible(api)
            } finally {
                location.delete()
            }
            writer.write("Finished importing %s.  %s!".format(api.name, if (apiDao.countUnprocessed(api) == 0L) "SUCCESS" else "FAILURE"))
        } catch (e: IOException) {
            log.error(e.message, e)
            throw RuntimeException(e.message, e)
        } catch (e: InterruptedException) {
            log.error(e.message, e)
            throw RuntimeException(e.message, e)
        }

    }

    fun buildHtml(api: JavadocApi, jar: File, packages: List<String>) {
        val javadocDir = File("javadoc/${api.name}/${api.version}/")
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
            val index = File("javadoc/JDK/1.8/index.html")
            val replaced = index.readText().replace(
                    "top.classFrame.location = top.targetPage;",
                    "top.classFrame.location = top.targetPage + location.hash;")
            index.writeText(replaced)

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

    private inner class JavadocClassReader(private val api: JavadocApi, val apiDao: ApiDao) : Runnable {
        override fun run() {
            try {
                val javadocSource = apiDao.findUnprocessedSource(api)
                if (javadocSource != null) {
                    val packages = if ("JDK" == api.name) arrayOf("java", "javax") else arrayOf<String>()
                    provider.get().parse(api, Roaster.parse(javadocSource.text), *packages)
                }
            } catch(e: Exception) {
                e.printStackTrace()
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

fun visibility(scope: String): Visibility {
    return when(scope) {
        "public" -> Public
        "private" -> Private
        "protected" -> Protected
        else -> PackagePrivate
    }
}

