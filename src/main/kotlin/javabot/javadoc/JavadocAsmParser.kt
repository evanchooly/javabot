package javabot.javadoc

import javabot.JavabotConfig
import javabot.dao.ApiDao
import javabot.model.downloadZip
import javabot.model.javadoc.JavadocApi
import org.bson.types.ObjectId
import org.objectweb.asm.ClassReader
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.FilenameFilter
import java.io.Writer
import java.net.URI
import java.util.jar.JarEntry
import java.util.jar.JarFile
import javax.inject.Inject

class JavadocAsmParser @Inject constructor(private val apiDao: ApiDao, private val config: JavabotConfig) {
    companion object {
        private val LOG = LoggerFactory.getLogger(JavadocAsmParser::class.java)
    }

    fun extractJavadocContent(api: JavadocApi): File {
        val downloadUri = if ("JDK" == api.name) {
            File(config.jdkJavadoc()).toURI()
        } else {
            api.javadocUri()
        }

        val javadocDir = File("javadoc/${api.name}/${api.version}/")
        if (!javadocDir.exists()) {
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

    fun scan(api: JavadocApi, writer: Writer) {
        val root = extractJavadocContent(api)
        val type = JavadocType.discover(root)

        if ("JDK" == api.name) {
            File(System.getProperty("java.home"), "jmods")
                    .listFiles { file, s -> s.startsWith("java") }
                    .forEach {
                        scanJar(api, type, it)
                    }
        } else {
            scanJar(api, type, api.classesUri().downloadZip())
        }


        writer.write("Finished importing ${api.name}.")
    }

    private fun scanJar(api: JavadocApi, type: JavadocType, jar: File) {
        var module: String? = null
        JarFile(jar).entries().iterator().forEach { entry ->
            if (entry.name.endsWith("class")) {
                if (entry.name.endsWith("module-info.class")) {
                    val moduleInfoVisitor = ModuleInfoVisitor()
                    ClassReader(JarFile(jar).getInputStream(entry))
                            .accept(moduleInfoVisitor, ClassReader.SKIP_CODE)
                    module = moduleInfoVisitor.module
                } else {
                    val packageName = entry.packageName()
                    if (!packageName.startsWith("com.sun") and !packageName.startsWith("sun")) {
                        ClassReader(JarFile(jar).getInputStream(entry))
                                .accept(JavadocClassVisitor(apiDao, api, packageName, entry.className(), module, type),
                                        ClassReader.SKIP_CODE)
                    }
                }
            }
        }
    }

    private fun JarEntry.className(): String {
        return this.name
                .substringAfter("classes/")
                .substringAfterLast("/")
                .substringBeforeLast(".")
                .replace("/", ".")
    }
    private fun JarEntry.packageName(): String {
        return this.name
                .substringAfter("classes/")
                .substringBeforeLast("/")
                .replace("/", ".")
    }

    private fun copyJavadocJar(api: JavadocApi, extracted: File, javadocDir: File) {
        val sourceRoot = if (api.name == "JDK") File(extracted, "docs/api") else discoverRoot(extracted)

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

    private fun JavadocApi.javadocUri() = URI("https://repo1.maven.org/maven2/${groupId.toPath()}/${artifactId}/${version}/${artifactId}-${version}-javadoc.jar")
    private fun JavadocApi.classesUri() = URI("https://repo1.maven.org/maven2/${groupId.toPath()}/${artifactId}/${version}/${artifactId}-${version}.jar")

    private fun String.toPath() = this.replace(".", "/")

}

