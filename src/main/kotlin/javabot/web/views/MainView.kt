package javabot.web.views

import com.antwerkz.sofia.Sofia
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javabot.model.Channel
import javabot.model.javadoc.JavadocApi
import javabot.web.JavabotConfiguration
import javabot.web.model.InMemoryUserCache.INSTANCE
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

abstract class MainView(
    var adminDao: AdminDao,
    var channelDao: ChannelDao,
    var factoidDao: FactoidDao,
    var apiDao: ApiDao,
    val request: HttpServletRequest,
) {

    companion object {
        val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm")
    }

    private val errors = ArrayList<String>()

    fun sofia(): Sofia {
        return Sofia
    }

    abstract fun getChildView(): String

    fun getFactoidCount(): Long {
        return factoidDao.count()
    }

    fun loggedIn(): Boolean {
        return INSTANCE.getBySessionToken(getSessionCookie()?.value) != null
    }

    fun isAdmin(): Boolean {
        val cookie = getSessionCookie()
        if (cookie != null) {
            val user = INSTANCE.getBySessionToken(cookie.value)
            return user != null && adminDao.getAdminByEmailAddress(user.email) != null
        } else {
            return false
        }
    }

    fun getCurrentChannel(): String {
        return ""
    }

    fun getChannels(): List<Channel> {
        return channelDao.getChannels(isAdmin())
    }

    fun getAPIs(): List<JavadocApi> {
        return apiDao.findAll()
    }

    fun encode(value: String): String {
        return java.net.URLEncoder.encode(value, "UTF-8")
    }

    private fun getSessionCookie(): Cookie? {
        return request.cookies?.firstOrNull { it.name == JavabotConfiguration.SESSION_TOKEN_NAME }
    }

    fun addError(message: String) {
        errors.add(message)
    }

    fun getErrors(): List<String> {
        return errors
    }

    fun hasErrors(): Boolean {
        return !errors.isEmpty()
    }

    open fun format(date: LocalDateTime?): String {
        return if (date != null) DATE_TIME_FORMATTER.format(date) else ""
    }

    /** Convert this view to a model map for FreeMarker */
    open fun toModel(): Map<String, Any?> {
        return mapOf(
            "sofia" to sofia(),
            "factoidCount" to getFactoidCount(),
            "loggedIn" to loggedIn(),
            "isAdmin" to isAdmin(),
            "currentChannel" to getCurrentChannel(),
            "channels" to getChannels(),
            "apis" to getAPIs(),
            "errors" to getErrors(),
            "hasErrors" to hasErrors(),
            "childView" to getChildView(),
        )
    }
}
