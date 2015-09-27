package javabot.model

import com.google.inject.Provider
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.javadoc.JavadocApi
import javabot.javadoc.JavadocParser
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Transient
import org.pircbotx.PircBotX
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.StringWriter
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

@Entity("events")
public class ApiEvent : AdminEvent {
    public var apiId: ObjectId? = null

    lateinit var name: String

    lateinit var baseUrl: String

    lateinit var downloadUrl: String

    @Inject
    @Transient
    lateinit val ircBot: Provider<PircBotX>

    @Inject
    @Transient
    lateinit val parser: JavadocParser

    @Inject
    @Transient
    lateinit val apiDao: ApiDao

    @Inject
    @Transient
    lateinit val adminDao: AdminDao

    public constructor() {
    }

    public constructor(requestedBy: String, name: String, baseUrl: String, downloadUrl: String) : super(EventType.ADD, requestedBy) {
        this.requestedBy = requestedBy
        this.name = name
        this.baseUrl = baseUrl
        if (name == "JDK") {
            try {
                this.downloadUrl = File(System.getProperty("java.home"), "lib/rt.jar").toURI().toURL().toString()
            } catch (e: MalformedURLException) {
                throw IllegalArgumentException(e.getMessage(), e)
            }

        } else {
            this.downloadUrl = downloadUrl
        }
    }

    public constructor(type: EventType, requestedBy: String, apiId: ObjectId?) : super(type, requestedBy) {
        this.apiId = apiId
    }

    public constructor(type: EventType, requestedBy: String, name: String) : super(type, requestedBy) {
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
            apiDao.delete(api)
        }
    }

    override fun add() {
        val api = JavadocApi(name, baseUrl, downloadUrl)
        apiDao.save(api)
        process(api)
    }

    override fun reload() {
        val api = apiDao.find(apiId)
        if (api != null) {
            apiDao.delete(apiId)
            api.id = null
            apiDao.save(api)
            process(api)
        }
    }

    private fun process(api: JavadocApi) {
        val admin = adminDao.getAdmin(ircBot.get().userChannelDao.getUser(requestedBy))
        if (admin != null) {
            val user = ircBot.get().userChannelDao.getUser(admin.ircName)
            try {
                val file = downloadZip(api.name + ".jar", api.downloadUrl)
                parser.parse(api, file.absolutePath, object : StringWriter() {
                    override fun write(line: String) {
                        bot.postMessageToUser(user, line)
                    }
                })
            } catch (e: IOException) {
                throw RuntimeException(e.getMessage(), e)
            }
        }

    }

    @Throws(IOException::class)
    private fun downloadZip(fileName: String, zipURL: String): File {
        val file = File("/tmp/" + fileName)
        if (!file.exists()) {
            val fileOutputStream = FileOutputStream(file)
            val openStream = URL(zipURL).openStream()
            fileOutputStream.write(openStream.readBytes())
            fileOutputStream.close()
            openStream.close()
        }
        return file
    }

    override fun toString(): String {
        return "ApiEvent{name='%s', state=%s, completed=%s, type=%s}".format(name, state, completed, type)
    }
}