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
import org.pircbotx.User

import javax.inject.Inject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.StringWriter
import java.net.MalformedURLException
import java.net.URL

Entity("events")
public class ApiEvent : AdminEvent {
    public var apiId: ObjectId? = null

    public var name: String

    public var baseUrl: String

    public var downloadUrl: String

    Inject
    Transient
    private val ircBot: Provider<PircBotX>? = null

    Inject
    Transient
    private val parser: JavadocParser? = null

    Inject
    Transient
    private val apiDao: ApiDao? = null

    Inject
    Transient
    private val adminDao: AdminDao? = null

    public constructor() {
    }

    public constructor(requestedBy: String, name: String, baseUrl: String, downloadUrl: String) : super(EventType.ADD, requestedBy) {
        requestedBy = requestedBy
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

    public constructor(type: EventType, requestedBy: String, apiId: ObjectId) : super(type, requestedBy) {
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
        var api = apiDao!!.find(apiId)
        if (api == null) {
            api = apiDao.find(name)
        }
        if (api != null) {
            apiDao.delete(api)
        }
    }

    override fun add() {
        val api = JavadocApi(name, baseUrl, downloadUrl)
        apiDao!!.save(api)
        process(api)
    }

    override fun reload() {
        val api = apiDao!!.find(apiId)
        apiDao.delete(apiId)
        api.id = null
        apiDao.save(api)
        process(api)
    }

    private fun process(api: JavadocApi) {
        val admin = adminDao!!.getAdmin(ircBot!!.get().userChannelDao.getUser(requestedBy))
        val user = ircBot.get().userChannelDao.getUser(admin.ircName)
        try {
            val file = downloadZip(api.name + ".jar", api.downloadUrl)
            parser!!.parse(api, file.absolutePath, object : StringWriter() {
                override fun write(line: String) {
                    bot.postMessageToUser(user, line)
                }
            })
        } catch (e: IOException) {
            throw RuntimeException(e.getMessage(), e)
        }

    }

    Throws(IOException::class)
    private fun downloadZip(fileName: String, zipURL: String): File {
        val file = File("/tmp/" + fileName)
        var read: Int
        if (!file.exists()) {
            URL(zipURL).openStream().use { inputStream ->
                FileOutputStream(file).use { fos ->
                    val bytes = ByteArray(8192)
                    while ((read = inputStream.read(bytes)) != -1) {
                        fos.write(bytes, 0, read)
                    }
                }
            }
        }
        return file
    }

    override fun toString(): String {
        return String.format("ApiEvent{name='%s', state=%s, completed=%s, type=%s}", name, state, completed, type)
    }
}