package javabot.javadoc

import javabot.JavabotConfig
import javabot.dao.JavadocClassDao
import javabot.model.downloadZip
import javabot.model.javadoc.JavadocApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.Writer
import java.net.URI
import java.util.jar.JarFile
import java.util.jar.Manifest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JavadocParser @Inject constructor(private val classDao: JavadocClassDao, private val config: JavabotConfig) {
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
    }

    fun parse(api: JavadocApi, writer: Writer)/* = runBlocking*/  {
        try {
            val root = extractJavadocContent(api)
            val type = JavadocType.discover(root)
            val jobs = mutableListOf<Job>()
            root.walkTopDown()
                    .filter { !it.path.contains("-") }
                    .filter { it.name != "allclasses.html" }
                    .filter { it.name != "index.html" }
                    .filter { it.extension == "html" }
                    .forEach { html ->
                        LOG.debug("html = ${html}")
//                        jobs += GlobalScope.launch {
                            type.create(api, html.absolutePath).parse(classDao)
//                        }
                    }

//            jobs.joinAll()

            writer.write("Finished importing ${api.name}.")
        } catch (e: Exception) {
            LOG.error(e.message, e)
            throw RuntimeException(e.message, e)
        }
    }

    fun extractJavadocContent(api: JavadocApi): File {
        val downloadUri = if ("JDK" == api.name) {
            File(config.jdkJavadoc()).toURI()
        } else  {
            api.buildMavenUri()
        }

        val javadocDir = File("javadoc/${api.name}/${api.version}/")
        if(!javadocDir.exists()) {
            val extracted = extractJar(downloadUri.downloadZip())

            try {
                javadocDir.mkdirs()
                copyJavadocJar(api, extracted, javadocDir)
            } catch (e: Exception) {
                LOG.error(e.message, e)
            } finally {
                extracted.deleteRecursively()
            }
        }

        return javadocDir
    }

    private fun copyJavadocJar(api: JavadocApi, extracted: File, javadocDir: File) {
        val sourceRoot = if(api.name == "JDK") File(extracted, "docs/api") else discoverRoot(extracted)

        javadocDir.mkdirs()
        sourceRoot.copyRecursively(javadocDir)
    }

    private fun discoverRoot(extracted: File): File {
        return extracted
                .walkTopDown()
                .filter { it.name == "index.html" }
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

    private fun JavadocApi.buildMavenUri()
            = URI("https://repo1.maven.org/maven2/${groupId.toPath()}/${artifactId}/${version}/${artifactId}-${version}-javadoc.jar")


    private fun String.toPath(): String {
        return this.replace(".", "/")
    }
}
 enum class JavadocType {
     JAVA6 {
         override fun create(api: JavadocApi, absolutePath: String): JavadocSource {
             return Java6JavadocSource(api, absolutePath)
         }
     },
     JAVA7 {
         override fun create(api: JavadocApi, absolutePath: String): JavadocSource {
             return Java7JavadocSource(api, absolutePath)
         }
     },
     JAVA8 {
         override fun create(api: JavadocApi, absolutePath: String): JavadocSource {
             return Java8JavadocSource(api, absolutePath)
         }
     },
     JAVA11 {
         override fun create(api: JavadocApi, absolutePath: String): JavadocSource {
             return Java11JavadocSource(api, absolutePath)
         }
     };

     companion object {
         fun discover(root: File) : JavadocType {
             val manFile = File(root, "META-INF/MANIFEST.mf")
             if(manFile.exists()) {
                 val manifest = Manifest()
                 manifest.read(manFile.inputStream())
                 val jdkVersion = manifest.mainAttributes.getValue("Build-Jdk")
                 return when {
                     jdkVersion.startsWith("1.6") -> JAVA6
                     jdkVersion.startsWith("1.7") -> JAVA7
                     jdkVersion.startsWith("1.8") -> JAVA8
                     else -> throw IllegalArgumentException("unknown JDK version for javadoc:  $jdkVersion")
                 }
             }
             return when {
                 File(root, "package-list").exists() -> JAVA8
                 File(root, "element-list").exists() -> JAVA11
                 else -> throw IllegalArgumentException("unknown JDK version for javadoc for $root")
             }
         }
     }

     abstract fun create(api: JavadocApi, absolutePath: String): JavadocSource
 }
