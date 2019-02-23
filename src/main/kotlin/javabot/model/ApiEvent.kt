package javabot.model

import javabot.JavabotConfig
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.javadoc.JavadocParser
import javabot.model.EventType.DELETE
import javabot.model.EventType.RELOAD
import javabot.model.javadoc.JavadocApi
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Reference
import org.mongodb.morphia.annotations.Transient
import org.slf4j.LoggerFactory
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
        fun add(requestedBy: String, api: JavadocApi): ApiEvent {
            return ApiEvent(requestedBy, api)
        }
        
        fun drop(requestedBy: String, api: JavadocApi) : ApiEvent {
            return ApiEvent(requestedBy, DELETE, api)
        }

        fun drop(requestedBy: String, name: String) : ApiEvent {
            return ApiEvent(requestedBy, DELETE, name)
        }

        fun reload(requestedBy: String, api: JavadocApi) : ApiEvent {
            return ApiEvent(requestedBy, RELOAD, api)
        }

        fun reload(requestedBy: String, name: String) : ApiEvent {
            return ApiEvent(requestedBy, RELOAD, name)
        }

        private val LOG = LoggerFactory.getLogger(ApiEvent::class.java)
    }

    lateinit var name: String

    @Reference(idOnly = true, ignoreMissing = true)
    var api: JavadocApi? = null

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

    constructor()

    private constructor(requestedBy: String, api: JavadocApi) :
    super(requestedBy, EventType.ADD) {
        this.requestedBy = requestedBy
        this.api = api
    }

    private constructor(requestedBy: String, type: EventType, api: JavadocApi) : super(requestedBy, type) {
        this.api = api
    }

    private constructor(requestedBy: String, type: EventType, name: String) : super(requestedBy, type) {
        this.name = name
    }

    override fun update() {
        delete()
        add()
    }

    override fun delete() {
        if(api == null) {
            api = apiDao.find(name)
        }
        api?.let {
            File("javadoc/${it.name}/${it.version}/").deleteTree()
            apiDao.delete(it)
            it.id = ObjectId()
        }
    }

    private fun findApi()= api ?: apiDao.find(name)

    override fun add() {
        api?.let {
            apiDao.save(it)
            val admin = adminDao.getAdmin(JavabotUser(requestedBy, requestedBy, ""))
            if (admin != null) {
                val user = JavabotUser(admin.ircName, admin.emailAddress, admin.hostName)

                parser.parse(it, object : StringWriter() {
                    override fun write(line: String) {
                        bot.privateMessageUser(user, line)
                        LOG.debug(line)
                    }
                })
            }
        }
    }

    override fun reload() {
        val api = findApi()
        if (api != null) {
            delete()
            add()
        }
    }

    override fun toString(): String {
        return "ApiEvent{name='${name}', state=${state}, completed=${completed}, type=${type}}"
    }
}

fun URI.downloadZip(): File {
    val downloads = File("downloads")
    downloads.mkdir()
    val file = File(downloads, path.substringAfterLast("/"))
    if(!file.exists()) {
        FileOutputStream(file).use { outputStream ->
            this.toURL().openStream().use { inputStream ->
                outputStream.write(inputStream.readBytes())
            }
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
