package javabot.kotlin.web.views

import com.antwerkz.sofia.Sofia
import com.google.common.base.Charsets
import io.dropwizard.views.View
import javabot.dao.AdminDao
import javabot.dao.ChannelDao
import javabot.dao.FactoidDao
import javabot.kotlin.web.JavabotConfiguration
import javabot.kotlin.web.model.InMemoryUserCache
import javabot.model.Channel
import java.io.UnsupportedEncodingException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

abstract class MainView(var adminDao: AdminDao, var channelDao: ChannelDao, var factoidDao: FactoidDao,
                        val request: HttpServletRequest) : View("/main.ftl", Charsets.ISO_8859_1) {

    companion object {
        val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm")
    }


    private val errors = ArrayList<String>()

    fun sofia(): Sofia {
        return Sofia()
    }

    abstract fun getChildView(): String

    fun getFactoidCount(): Long {
        return factoidDao.count()
    }

    fun loggedIn(): Boolean {
        return InMemoryUserCache.INSTANCE.getBySessionToken(getSessionCookie()?.value) != null
    }

    fun isAdmin(): Boolean {
        val cookie = getSessionCookie()
        if (cookie != null) {
            val user = InMemoryUserCache.INSTANCE.getBySessionToken(cookie.value)
            return user != null && adminDao.getAdminByEmailAddress(user.email!!) != null
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

    @Throws(UnsupportedEncodingException::class) fun encode(value: String): String {
        return java.net.URLEncoder.encode(value, "UTF-8")
    }

    private fun getSessionCookie(): Cookie? {
        for (cookie in request.cookies) {
            if (cookie.name == JavabotConfiguration.SESSION_TOKEN_NAME) {
                return cookie
            }
        }
        return null
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

    open fun format(date: LocalDateTime): String {
        return DATE_TIME_FORMATTER.format(date)
    }
}
