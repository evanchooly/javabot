package javabot.javadoc

import io.quarkus.arc.Unremovable
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.io.File
import java.io.FileOutputStream
import java.io.Writer
import java.net.URI
import java.util.jar.JarEntry
import java.util.jar.JarFile
import javabot.JavabotConfig
import javabot.dao.ApiDao
import javabot.javadoc.JavadocType.Companion.discover
import javabot.javadoc.JavadocType.Companion.forVersion
import javabot.model.download
import javabot.model.javadoc.JavadocApi
import org.bson.types.ObjectId
import org.objectweb.asm.ClassReader
import org.slf4j.LoggerFactory

// @Unremovable: this bean's only "consumer" is ApiEvent.asmParser, an @Inject field on a plain
// Morphia-deserialized POJO (not itself a CDI bean) that EventInjector populates via runtime
// BeanManager reflection rather than Arc's normal build-time injection graph. Without this
// annotation, Arc's unused-bean removal prunes JavadocAsmParser entirely (nothing else injects
// it through a real constructor injection point), and EventInjector.inject() then throws
// UnsatisfiedResolutionException at runtime -- caught empirically by this task's ApiEvent test.
@Unremovable
@Singleton
class JavadocAsmParser
@Inject
constructor(private val apiDao: ApiDao, private val config: JavabotConfig) {
    companion object {
        private val LOG = LoggerFactory.getLogger(JavadocAsmParser::class.java)
    }

    fun extractJavadocContent(api: JavadocApi): JavadocType {
        val javadocDir = File("javadoc/${api.name}/${api.version}/")

        if (!javadocDir.exists()) {
            if ("JDK" != api.name) {
                val extracted = extractJar(api.javadocUri().download())

                try {
                    copyJavadocJar(api, extracted, javadocDir)
                } catch (e: Exception) {
                    LOG.error(e.message, e)
                } finally {
                    extracted.deleteRecursively()
                }
            }
        }

        return if ("JDK" != api.name) discover(javadocDir) else forVersion(api.version)
    }

    fun scan(api: JavadocApi, writer: Writer) {
        val type = extractJavadocContent(api)

        if ("JDK" == api.name) {
            File(System.getProperty("java.home"), "jmods")
                .listFiles { _, s -> s.startsWith("java") }
                .forEach { scanJar(api, type, it) }
        } else {
            scanJar(api, type, api.classesUri().download())
        }

        writer.write("Finished importing ${api.name}.")
    }

    private fun scanJar(api: JavadocApi, type: JavadocType, jar: File) {
        // Resolved in its own pass first, before scanning any other entry: JAR/ZIP iteration
        // order is not guaranteed to place module-info.class before the classes it describes
        // (observed in practice -- some JDK jmod builds order it later), so a single sequential
        // pass that sets `module` as it goes can silently leave earlier-scanned classes with no
        // module segment in their generated javadoc URL.
        val module = findModule(jar)
        JarFile(jar).entries().iterator().forEach { entry ->
            if (entry.name.endsWith("class") && !entry.name.endsWith("module-info.class")) {
                val packageName = entry.packageName()
                if (!packageName.startsWith("com.sun") and !packageName.startsWith("sun")) {
                    ClassReader(JarFile(jar).getInputStream(entry))
                        .accept(
                            JavadocClassVisitor(
                                apiDao,
                                api,
                                packageName,
                                entry.className(),
                                module,
                                type,
                            ),
                            ClassReader.SKIP_CODE,
                        )
                }
            }
        }
    }

    private fun findModule(jar: File): String? {
        val entry =
            JarFile(jar).entries().iterator().asSequence().find {
                it.name.endsWith("module-info.class")
            }
        return entry?.let {
            val moduleInfoVisitor = ModuleInfoVisitor()
            ClassReader(JarFile(jar).getInputStream(it))
                .accept(moduleInfoVisitor, ClassReader.SKIP_CODE)
            moduleInfoVisitor.module
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
        return this.name.substringAfter("classes/").substringBeforeLast("/").replace("/", ".")
    }

    private fun copyJavadocJar(api: JavadocApi, extracted: File, javadocDir: File) {
        javadocDir.mkdirs()

        val sourceRoot =
            if (api.name == "JDK") File(extracted, "docs/api") else discoverRoot(extracted)
        sourceRoot.copyRecursively(javadocDir)
    }

    private fun discoverRoot(extracted: File): File {
        return extracted
            .walkTopDown()
            .filter { it.name == "index.html" }
            .map { it.parentFile }
            .first()
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
                FileOutputStream(javaFile).use { jarFile.getInputStream(entry).copyTo(it) }
            }
        }

        return extracted
    }

    private fun JavadocApi.javadocUri() =
        URI(
            "https://repo1.maven.org/maven2/${groupId.toPath()}/${artifactId}/${version}/${artifactId}-${version}-javadoc.jar"
        )

    private fun JavadocApi.classesUri() =
        URI(
            "https://repo1.maven.org/maven2/${groupId.toPath()}/${artifactId}/${version}/${artifactId}-${version}.jar"
        )

    private fun String.toPath() = this.replace(".", "/")
}
