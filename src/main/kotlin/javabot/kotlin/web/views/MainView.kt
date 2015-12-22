package javabot.kotlin.web.views

import com.antwerkz.sofia.Sofia
import com.google.common.base.Charsets
import com.google.inject.Injector
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
import javax.inject.Inject
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

public abstract class MainView(injector: Injector, val request: HttpServletRequest) : View("/main.ftl", Charsets.ISO_8859_1) {
    companion object {
        public val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm")
    }

    @Inject
    lateinit var adminDao: AdminDao

    @Inject
    lateinit var channelDao: ChannelDao

    @Inject
    lateinit var factoidDao: FactoidDao

    private val errors = ArrayList<String>()

    init {
        injector.injectMembers(this)
    }

    public fun sofia(): Sofia {
        return Sofia()
    }

    public abstract fun getChildView(): String

    public fun getFactoidCount(): Long {
        return factoidDao.count()
    }

    public fun loggedIn(): Boolean {
        return InMemoryUserCache.INSTANCE.getBySessionToken(getSessionCookie()?.value) != null
    }

    public fun isAdmin(): Boolean {
        val cookie = getSessionCookie()
        if (cookie != null) {
            val user = InMemoryUserCache.INSTANCE.getBySessionToken(cookie.value)
            return user != null && adminDao.getAdminByEmailAddress(user.email!!) != null
        } else {
            return false
        }
    }

    public fun getCurrentChannel(): String {
        return ""
    }

    public fun getChannels(): List<Channel> {
        return channelDao.getChannels(isAdmin())
    }

    @Throws(UnsupportedEncodingException::class)
    public fun encode(value: String): String {
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

    public fun addError(message: String) {
        errors.add(message)
    }

    public fun getErrors(): List<String> {
        return errors
    }

    public fun hasErrors(): Boolean {
        return !errors.isEmpty()
    }

    public open fun format(date: LocalDateTime): String {
        return DATE_TIME_FORMATTER.format(date)
    }
}
