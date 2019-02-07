package javabot.javadoc

import com.jayway.awaitility.Awaitility
import javabot.JavabotConfig
import javabot.JavabotThreadFactory
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.model.downloadZip
import javabot.model.javadoc.Java6JavadocSource
import javabot.model.javadoc.Java8JavadocSource
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocSource
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.Writer
import java.net.URI
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS
import java.util.jar.JarFile
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class JavadocParser @Inject constructor(val apiDao: ApiDao, val javadocClassDao: JavadocClassDao,
                                        val config: JavabotConfig) {
    companion object {
        private val LOG = LoggerFactory.getLogger(JavadocParser::class.java)

        fun calculateNameAndPackage(value: String): Pair<String?, String> {
            var clsName = value
            while (clsName.contains(".") && Character.isLowerCase(clsName[0])) {
                clsName = clsName.substring(clsName.indexOf(".") + 1)
            }
            val pkgName = if (value != clsName) {
                value.substring(0, value.indexOf(clsName) - 1)
            } else {
                null
            }

            return pkgName to clsName
        }

        fun getPackage(name: String): Pair<String, String> {
            val split = name.split('.')

            return Pair(split.takeWhile { it[0].isLowerCase() }.joinToString("."),
                    split.dropWhile { it[0].isLowerCase() }.joinToString("."))
        }
    }

    fun parse(api: JavadocApi, writer: Writer) {
        val downloadUri = if ("JDK" == api.name) {
            File(config.jdkJavadoc()).toURI()
        } else  {
            api.buildMavenUri()
        }

        val location = downloadUri.downloadZip()
        try {

            val executor = ScheduledThreadPoolExecutor(100, JavabotThreadFactory(false, "javadoc-thread-"))
            executor.prestartCoreThread()

            repeat(executor.corePoolSize) {
                executor.scheduleAtFixedRate({
                    apiDao.findUnprocessedSource(api)
                            ?.process(javadocClassDao)
                }, 0, 1, SECONDS)
            }

            val root = extractJavadocContent(api, location)
            val type = JavadocType.discover(root)
            type?.let {
                root
                        .walkTopDown()
                        .filter { !it.path.contains("-") }
                        .filter { it.extension == "html" }
                        .forEach {
                            apiDao.save(type.create(api, it.absolutePath))
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

            writer.write("Finished importing %s.  %s!".format(api.name, if (apiDao.countUnprocessed(api) == 0L) "SUCCESS" else "FAILURE"))
        } catch (e: IOException) {
            LOG.error(e.message, e)
            throw RuntimeException(e.message, e)
        } catch (e: InterruptedException) {
            LOG.error(e.message, e)
            throw RuntimeException(e.message, e)
        } finally {
            location.delete()
        }
    }

    fun extractJavadocContent(api: JavadocApi, jar: File): File {

        val extracted = extractJar(jar)
        val javadocDir = File("javadoc/${api.name}/${api.version}/")

        try {
            javadocDir.mkdirs()
            copyJavadocJar(extracted, javadocDir)
        } catch (e : Exception) {
            LOG.error(e.message, e)
        } finally {
            extracted.deleteRecursively()
        }

        return javadocDir
    }

    private fun copyJavadocJar(extracted: File, javadocDir: File) {
        val sourceRoot = discoverRoot(extracted)

        javadocDir.mkdirs()
        sourceRoot.copyRecursively(javadocDir)
    }

    private fun discoverRoot(extracted: File): File {
        return extracted
                .walkTopDown()
                .filter { it.name == "index.html" }
                .filter { JavadocType.discover(it.parentFile) != null }
                .map { it.parentFile }.first()
    }

    private fun extractJar(jar: File): File {
        var tmp = File("/tmp/")
        if (!tmp.exists()) {
            tmp = File(System.getProperty("java.io.tmpdir"))
        }
        val extracted = File(tmp, ObjectId().toString())
        extracted.mkdirs()

        val jarFile = JarFile(jar)
        jarFile.entries().iterator().forEach { entry ->
            if (!entry.isDirectory) {
                val javaFile = File(extracted, entry.name)
                javaFile.parentFile.mkdirs()
                FileOutputStream(javaFile).use {
                    jarFile.getInputStream(entry).copyTo(it)
                }
            }
        }

        return extracted
    }

    fun getJavadocClass(fqcn: String): JavadocClass? {
        val pair = getPackage(fqcn)
        return getJavadocClass(pair.first, pair.second)
    }

    fun getJavadocClass(pkg: String, name: String): JavadocClass? {
        return javadocClassDao.getClass(null, pkg, name)
    }

    private fun JavadocApi.buildMavenUri()
            = URI("https://repo1.maven.org/maven2/${groupId.toPath()}/${artifactId}/${version}/${artifactId}-${version}-javadoc.jar")


    private fun String.toPath(): String {
        return this.replace(".", "/")
    }
}
 enum class JavadocType {
     JAVA6 {
         override fun create(api: JavadocApi, file: String): JavadocSource {
             return Java6JavadocSource(api, file)
         }
     },
     JAVA8 {
         override fun create(api: JavadocApi, file: String): JavadocSource {
             return Java8JavadocSource(api, file)
         }
     };

     companion object {
         fun discover(root: File) : JavadocType? {
             return when {
                 File(root, "element-list").let { it.exists() } -> JAVA8
                 File(root, "package-list").let { it.exists() } -> JAVA6
                 else -> null
             }
         }
     }

     abstract fun create(api: JavadocApi, absolutePath: String): JavadocSource
 }