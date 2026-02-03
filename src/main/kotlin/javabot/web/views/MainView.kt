package javabot.web.views

import com.antwerkz.sofia.Sofia
import io.dropwizard.views.common.View
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
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

abstract class MainView(
    var adminDao: AdminDao,
    var channelDao: ChannelDao,
    var factoidDao: FactoidDao,
    var apiDao: ApiDao,
    val request: HttpServletRequest,
) : View("/main.ftl", com.google.common.base.Charsets.ISO_8859_1) {

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
}
