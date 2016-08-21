package javabot.model

import javabot.JavabotConfig
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.javadoc.JavadocApi
import javabot.javadoc.JavadocParser
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Transient
import java.io.File
import java.io.FileOutputStream
import java.io.StringWriter
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject

@Entity("events")
class ApiEvent : AdminEvent {

    companion object {
        fun locateJDK(): String {
            var property = System.getProperty("java.home")
            if (property.endsWith("/jre")) {
                property = property.dropLast(4)
            }
            return File(property, "src.zip").toURI().toURL().toString()
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

    constructor(requestedBy: String, name: String, groupId: String, artifactId: String, version: String) :
    super(requestedBy, EventType.ADD) {
        this.requestedBy = requestedBy
        this.name = name
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
    }

    constructor(requestedBy: String, type: EventType, apiId: ObjectId?) : super(requestedBy, type) {
        this.apiId = apiId
    }

    constructor(requestedBy: String, type: EventType, name: String) : super(requestedBy, type) {
        this.name = name
    }

    override fun update() {
        delete()
        add()
    }

    override fun delete() {
        var api = apiDao.find(apiId)
        if (api == null) {
            api = apiDao.find(name)
        }
        if (api != null) {
            val host = config.databaseHost()
            val port = config.databasePort()
            val database = config.databaseName()

            Files.delete(Paths.get(URI("gridfs://$host:$port/$database.javadoc/${api.name}/")))
            apiDao.delete(api)
        }
    }

    override fun add() {
        val api = JavadocApi(name, "${config.url()}", groupId, artifactId, version)
        apiDao.save(api)
        process(api)
    }

    override fun reload() {
        val api = apiDao.find(apiId)
        if (api != null) {
            name = api.name
            groupId = api.groupId
            artifactId = api.artifactId
            version = if (name == "JDK" ) System.getProperty("java.version") else  api.version
//            apiDao.delete(apiId)
//            api.id = ObjectId()
//            apiDao.save(api)
            process(api)
        }
    }

    private fun process(api: JavadocApi) {
        val downloadUrl = if (name == "JDK") locateJDK() else buildMavenUrl()
        val admin = adminDao.getAdmin(JavabotUser(requestedBy, requestedBy, ""))
        if (admin != null) {
            val user = JavabotUser(admin.ircName, admin.emailAddress, admin.hostName)

            parser.parse(api, downloadUrl.downloadZip(File("/tmp/${api.name}.jar")),
                    object : StringWriter() {
                        override fun write(line: String) = bot.privateMessageUser(user, line)
                    })
        }
    }

    private fun buildMavenUrl(): String {
        return "https://repo1.maven.org/maven2/${groupId.replace(".", "/")}/${artifactId}/${version}/${artifactId}-${version}-sources.jar"
    }

    override fun toString(): String {
        return "ApiEvent{name='${name}', state=${state}, completed=${completed}, type=${type}}"
    }
}

fun String.downloadZip(file: File): File {
    if (!file.exists()) {
        val fileOutputStream = FileOutputStream(file)
        val openStream = URL(this).openStream()
        fileOutputStream.write(openStream.readBytes())
        fileOutputStream.close()
        openStream.close()
    }
    return file
}