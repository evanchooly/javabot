package javabot.model

import javabot.JavabotConfig
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.model.javadoc.JavadocApi
import javabot.javadoc.JavadocParser
import javabot.model.EventType.DELETE
import javabot.model.EventType.RELOAD
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Transient
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.StringWriter
import java.net.URI
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import javax.inject.Inject

@Entity("events")
class ApiEvent : AdminEvent {

    companion object {
        fun locateJDK(): URI {
            var property = System.getProperty("java.home")
            if (property.endsWith("/jre")) {
                property = property.dropLast(4)
            }
            var home = File(property)
            var file = File(property, "src.zip")

            while (!file.exists()) {
                home = home.parentFile
                file = File(home, "src.zip")
            }
            return file.toURI()
        }
        
        fun add(requestedBy: String, name: String, groupId: String = "",
                artifactId: String = "", version: String = ""): ApiEvent {
            return ApiEvent(requestedBy, name, groupId, artifactId, version)
        }
        
        fun drop(requestedBy: String, id: ObjectId) : ApiEvent {
            return ApiEvent(requestedBy, DELETE, id)
        }

        fun drop(requestedBy: String, name: String) : ApiEvent {
            return ApiEvent(requestedBy, DELETE, name)
        }

        fun reload(requestedBy: String, id: ObjectId) : ApiEvent {
            return ApiEvent(requestedBy, RELOAD, id)
        }

        fun reload(requestedBy: String, name: String) : ApiEvent {
            return ApiEvent(requestedBy, RELOAD, name)
        }
    }

    var apiId: ObjectId? = null

    lateinit var name: String

    lateinit var groupId: String

    lateinit var artifactId: String

    lateinit var version: String

    @Inject
    @Transient
    lateinit var config: JavabotConfig

    @Inject
    @Transient
    lateinit var parser: JavadocParser

    @Inject
    @Transient
    lateinit var apiDao: ApiDao

    @Inject
    @Transient
    lateinit var adminDao: AdminDao

    constructor() {
    }

    private constructor(requestedBy: String, name: String, groupId: String, artifactId: String, version: String) :
    super(requestedBy, EventType.ADD) {
        this.requestedBy = requestedBy
        this.name = name
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
    }

    private constructor(requestedBy: String, type: EventType, apiId: ObjectId?) : super(requestedBy, type) {
        this.apiId = apiId
    }

    private constructor(requestedBy: String, type: EventType, name: String) : super(requestedBy, type) {
        this.name = name
    }

    override fun update() {
        delete()
        add()
    }

    override fun delete() {
        var api = findApi()
        if (api != null) {
            File("javadoc/${api.name}/${api.version}/").deleteTree()
            apiDao.delete(api)
        }
    }

    private fun findApi(): JavadocApi? {
        var api = apiDao.find(apiId)
        if (api == null) {
            api = apiDao.find(name)
        }
        return api
    }

    override fun add() {
        val api = JavadocApi(name, config.url(), groupId, artifactId, version)
        process(api)
    }

    override fun reload() {
        val api = findApi()
        if (api != null) {
            name = api.name
            groupId = api.groupId
            artifactId = api.artifactId
            version = if (name == "JDK") System.getProperty("java.version") else api.version
//            apiDao.delete(apiId)
//            api.id = ObjectId()
//            apiDao.save(api)
            delete()
            process(api)
        }
    }

    private fun process(api: JavadocApi) {
        apiDao.save(api)
        val admin = adminDao.getAdmin(JavabotUser(requestedBy, requestedBy, ""))
        if (admin != null) {
            val user = JavabotUser(admin.ircName, admin.emailAddress, admin.hostName)

            parser.parse(api, object : StringWriter() {
                        override fun write(line: String) = bot.privateMessageUser(user, line)
                    })
        }
    }

    override fun toString(): String {
        return "ApiEvent{name='${name}', state=${state}, completed=${completed}, type=${type}}"
    }
}

fun URI.downloadZip(): File {
    val file = File("/tmp", path.substringAfterLast("/"))
    FileOutputStream(file).use { outputStream ->
        this.toURL().openStream().use { inputStream ->
            outputStream.write(inputStream.readBytes())
        }
    }
    return file
}

fun File.deleteTree() {
    if (exists()) {
        if (isDirectory) {
            Files.walkFileTree(toPath(), object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    Files.delete(file)
                    return FileVisitResult.CONTINUE
                }

                override fun postVisitDirectory(dir: Path, e: IOException?): FileVisitResult {
                    if (e == null) {
                        Files.delete(dir)
                        return FileVisitResult.CONTINUE
                    } else {
                        // directory iteration failed
                        throw e
                    }
                }
            })
        } else {
            throw RuntimeException("deleteTree() can only be called on directories:  ${this}")
        }
    }
}
