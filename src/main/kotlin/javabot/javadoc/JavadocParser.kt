package javabot.javadoc

import com.jayway.awaitility.Awaitility
import javabot.JavabotConfig
import javabot.JavabotThreadFactory
import javabot.dao.ApiDao
import javabot.dao.JavadocClassDao
import javabot.model.downloadZip
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocSource
import javabot.model.javadoc.Visibility
import javabot.model.javadoc.Visibility.PackagePrivate
import javabot.model.javadoc.Visibility.Private
import javabot.model.javadoc.Visibility.Protected
import javabot.model.javadoc.Visibility.Public
import org.bson.types.ObjectId
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.Writer
import java.net.URI
import java.nio.charset.Charset
import java.util.Collections
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS
import java.util.jar.JarEntry
import java.util.jar.JarFile
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class JavadocParser @Inject constructor(val apiDao: ApiDao, val javadocClassDao: JavadocClassDao,
                                        val provider: Provider<JavadocClassParser>, val config: JavabotConfig) {

    fun parse(api: JavadocApi, writer: Writer) {
        val downloadUri : URI
        if ("JDK" == api.name) {
            downloadUri = File(config.jdkJavadoc()).toURI()
        } else  {
            downloadUri = api.buildMavenUri()
        }

        val location = downloadUri.downloadZip()
        try {

            val executor = ScheduledThreadPoolExecutor(100, JavabotThreadFactory(false, "javadoc-thread-"))
            executor.prestartCoreThread()

            val javadocDir = extractJavadocContent(api, location)

//            (1..executor.corePoolSize).forEach {
//                executor.scheduleAtFixedRate(JavadocClassReader(api, apiDao), 0, 1, SECONDS)
//            }

            javadocDir.walkTopDown()
                .filter { !it.name.contains("-") }
                .filter { it.extension == "html" }
//                .filter { !it.name.contains("package-") }
//                .filter { !it.name.contains("-summary") }
                .forEach {
                    processHtml(api, it)
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

            writer.write("Finished importing %s.  %s!".format(api.name, if (apiDao.countUnprocessed(api) == 0L) "SUCCESS" else "FAILURE"))
        } catch (e: IOException) {
            log.error(e.message, e)
            throw RuntimeException(e.message, e)
        } catch (e: InterruptedException) {
            log.error(e.message, e)
            throw RuntimeException(e.message, e)
        } finally {
            location.delete()
        }
    }

    private fun processHtml(api: JavadocApi, file: File) {
        val document = Jsoup.parse(file.readText())
        val attribute = document.getElementsByTag("meta")
            .flatMap { it.attributes() }
            .filter { it.key == "content" && it.value.contains(" ") && !it.value.contains("/")}
            .map { it.value }
            .firstOrNull()
        if (attribute != null) {
            val (name, type) = attribute.split(" ")
            if (!name.startsWith("jdk.")) {
                when (type) {
                    "class" -> {
                        provider.get().parse(api, document)
                    }
                    "interface" -> {
                        provider.get().parse(api, document)
                    }
                    else -> TODO("handle $type in $file")
                }
            }
        }
    }

    fun extractJavadocContent(api: JavadocApi, jar: File): File {
        val javadocDir = File("javadoc/${api.name}/${api.version}/")
        var tmp = File("/tmp/")
        if (!tmp.exists()) {
            tmp = File(System.getProperty("java.io.tmpdir"))
        }
        val jarTarget = File(tmp, ObjectId().toString())

        jarTarget.mkdirs()
        javadocDir.mkdirs()

        extractJar(jar, jarTarget)
        copyJavadocJar(api, jarTarget, javadocDir)

        try {
            val index = File("javadoc/JDK/1.8/index.html")
            val replaced = index.readText().replace(
                    "top.classFrame.location = top.targetPage;",
                    "top.classFrame.location = top.targetPage + location.hash;")
            index.writeText(replaced)

        } catch (e : Exception) {
            log.error(e.message, e)
        } finally {
            jarTarget.deleteRecursively()
        }

        return javadocDir
    }

    private fun copyJavadocJar(api: JavadocApi, jarTarget: File, javadocDir: File) {
        val sourceRoot = if(api.name == "JDK") {
            File(jarTarget, "docs/api")
        } else {
            jarTarget
        }

        javadocDir.deleteRecursively()
        javadocDir.mkdirs()
        sourceRoot.copyRecursively(javadocDir)
        sourceRoot.deleteRecursively()
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

    companion object {
        private val log = LoggerFactory.getLogger(JavadocParser::class.java)

        fun getPackage(name: String): Pair<String, String> {
            val split = name.split('.')

            return Pair(split.takeWhile { it[0].isLowerCase() }.joinToString("."),
                    split.dropWhile { it[0].isLowerCase() }.joinToString("."))
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

fun JarFile.extractToSource(api: JavadocApi, filter: (JarEntry) -> Boolean, terminal: (JavadocSource) -> Unit) {
    use { jarFile ->
        Collections.list(jarFile.entries())
                .filter(filter)
                .map {
                    val text = jarFile . getInputStream(it).use {
                         it.readBytes().toString(Charset.forName("UTF-8"))
                    }
                    JavadocSource(it.name, api, text)
                }
                .forEach(terminal)
    }
}
