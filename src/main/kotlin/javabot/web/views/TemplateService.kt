package javabot.web.views

import com.antwerkz.sofia.Sofia
import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javabot.Javabot
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.dao.ChangeDao
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.FactoidDao
import javabot.dao.KarmaDao
import javabot.dao.LogsDao
import javabot.dao.util.CleanHtmlConverter
import javabot.dao.util.QueryParam
import javabot.model.Admin
import javabot.model.Channel
import javabot.model.Factoid
import javabot.web.JavabotConfiguration
import javabot.web.model.InMemoryUserCache.INSTANCE
import javabot.web.resources.BotResource
import org.slf4j.LoggerFactory

@ApplicationScoped
class TemplateService
@Inject
constructor(
    private val adminDao: AdminDao,
    private val channelDao: ChannelDao,
    private val factoidDao: FactoidDao,
    private val apiDao: ApiDao,
    private val karmaDao: KarmaDao,
    private val logsDao: LogsDao,
    private val changeDao: ChangeDao,
    private val configDao: ConfigDao,
    private val javabot: Javabot,
) {

    @Location("templates/main.html") lateinit var mainTemplate: Template

    @Location("templates/paged.html") lateinit var pagedTemplate: Template

    @Location("templates/error/403.html") lateinit var error403Template: Template

    @Location("templates/error/404.html") lateinit var error404Template: Template

    @Location("templates/error/500.html") lateinit var error500Template: Template

    // Index view
    fun createIndexView(request: HttpServletRequest): TemplateInstance {
        return mainTemplate
            .data("factoidCount", factoidDao.count())
            .data("loggedIn", isLoggedIn(request))
            .data("isAdmin", isAdmin(request))
            .data("channels", channelDao.getChannels(isAdmin(request)))
            .data("apis", apiDao.findAll())
            .data("sofia", Sofia)
            .data("errors", emptyList<String>())
            .data("hasErrors", false)
    }

    // Factoids view
    fun createFactoidsView(
        request: HttpServletRequest,
        page: Int,
        filter: Factoid,
    ): TemplateInstance {
        val pageData = PageData(page, factoidDao.countFiltered(filter), ITEMS_PER_PAGE)
        val factoids =
            factoidDao.getFactoidsFiltered(
                QueryParam(pageData.index, ITEMS_PER_PAGE, "Name", true),
                filter,
            )

        val nextPage = pageData.nextPage?.let { applyFactoidFilter(it, filter) }
        val previousPage = pageData.previousPage?.let { applyFactoidFilter(it, filter) }

        return pagedTemplate
            .data("factoidCount", factoidDao.count())
            .data("loggedIn", isLoggedIn(request))
            .data("isAdmin", isAdmin(request))
            .data("channels", channelDao.getChannels(isAdmin(request)))
            .data("apis", apiDao.findAll())
            .data("sofia", Sofia)
            .data("errors", emptyList<String>())
            .data("hasErrors", false)
            .data("page", pageData.page)
            .data("itemCount", pageData.itemCount)
            .data("pageCount", pageData.pageCount)
            .data("startRange", pageData.startRange)
            .data("endRange", pageData.endRange)
            .data("nextPage", nextPage)
            .data("previousPage", previousPage)
            .data("pageItems", factoids)
            .data("filter", filter)
            .data("pagedView", "factoids.html")
    }

    private fun applyFactoidFilter(url: String, filter: Factoid): String {
        val builder = StringBuilder()
        builder.append("&name=").append(encode(filter.name))
        builder.append("&value=").append(encode(filter.value))
        builder.append("&userName=").append(encode(filter.userName))
        return url + builder
    }

    // Karma view
    fun createKarmaView(request: HttpServletRequest, page: Int): TemplateInstance {
        val pageData = PageData(page, karmaDao.count(), ITEMS_PER_PAGE)
        val karmaList = karmaDao.list(QueryParam(pageData.index, ITEMS_PER_PAGE, "value", false))

        return pagedTemplate
            .data("factoidCount", factoidDao.count())
            .data("loggedIn", isLoggedIn(request))
            .data("isAdmin", isAdmin(request))
            .data("channels", channelDao.getChannels(isAdmin(request)))
            .data("apis", apiDao.findAll())
            .data("sofia", Sofia)
            .data("errors", emptyList<String>())
            .data("hasErrors", false)
            .data("page", pageData.page)
            .data("itemCount", pageData.itemCount)
            .data("pageCount", pageData.pageCount)
            .data("startRange", pageData.startRange)
            .data("endRange", pageData.endRange)
            .data("nextPage", pageData.nextPage)
            .data("previousPage", pageData.previousPage)
            .data("pageItems", karmaList)
            .data("pagedView", "karma.html")
    }

    // Changes view
    fun createChangesView(
        request: HttpServletRequest,
        page: Int,
        message: String?,
        date: LocalDateTime?,
    ): TemplateInstance {
        val pageData = PageData(page, changeDao.count(message, date), ITEMS_PER_PAGE)
        val changes =
            changeDao.getChanges(
                QueryParam(pageData.index, ITEMS_PER_PAGE, "updated"),
                message,
                date,
            )

        return pagedTemplate
            .data("factoidCount", factoidDao.count())
            .data("loggedIn", isLoggedIn(request))
            .data("isAdmin", isAdmin(request))
            .data("channels", channelDao.getChannels(isAdmin(request)))
            .data("apis", apiDao.findAll())
            .data("sofia", Sofia)
            .data("errors", emptyList<String>())
            .data("hasErrors", false)
            .data("page", pageData.page)
            .data("itemCount", pageData.itemCount)
            .data("pageCount", pageData.pageCount)
            .data("startRange", pageData.startRange)
            .data("endRange", pageData.endRange)
            .data("nextPage", pageData.nextPage)
            .data("previousPage", pageData.previousPage)
            .data("pageItems", changes)
            .data("pagedView", "changes.html")
    }

    // Logs view
    fun createLogsView(
        request: HttpServletRequest,
        channel: String,
        date: LocalDateTime,
    ): TemplateInstance {
        val logs = logsDao.findByChannel(channel, date, isAdmin(request))
        // Filter the log content
        for (log in logs) {
            log.message =
                CleanHtmlConverter.convert(log.message) { s -> Sofia.logsAnchorFormat(s, s) }
        }

        val today = BotResource.FORMAT.format(date)
        val yesterday = BotResource.FORMAT.format(date.minusDays(1))
        val tomorrow = BotResource.FORMAT.format(date.plusDays(1))

        return mainTemplate
            .data("factoidCount", factoidDao.count())
            .data("loggedIn", isLoggedIn(request))
            .data("isAdmin", isAdmin(request))
            .data("channels", channelDao.getChannels(isAdmin(request)))
            .data("apis", apiDao.findAll())
            .data("sofia", Sofia)
            .data("errors", emptyList<String>())
            .data("hasErrors", false)
            .data("logs", logs)
            .data("channel", channel)
            .data("today", today)
            .data("yesterday", yesterday)
            .data("tomorrow", tomorrow)
            .data("logFormat", LOG_FORMAT)
    }

    // Admin index view
    fun createAdminIndexView(
        request: HttpServletRequest,
        current: Admin,
        editing: Admin?,
    ): TemplateInstance {
        return mainTemplate
            .data("factoidCount", factoidDao.count())
            .data("loggedIn", isLoggedIn(request))
            .data("isAdmin", isAdmin(request))
            .data("channels", channelDao.getChannels(isAdmin(request)))
            .data("apis", apiDao.findAll())
            .data("sofia", Sofia)
            .data("errors", emptyList<String>())
            .data("hasErrors", false)
            .data("current", current)
            .data("editing", editing)
            .data("admins", adminDao.findAll())
    }

    // Configuration view
    fun createConfigurationView(request: HttpServletRequest): TemplateInstance {
        val config = configDao.get()
        val operations = javabot.getAllOperations().values.sortedBy { it.getName() }
        val currentOps = config.operations.toSet()

        return mainTemplate
            .data("factoidCount", factoidDao.count())
            .data("loggedIn", isLoggedIn(request))
            .data("isAdmin", isAdmin(request))
            .data("channels", channelDao.getChannels(isAdmin(request)))
            .data("apis", apiDao.findAll())
            .data("sofia", Sofia)
            .data("errors", emptyList<String>())
            .data("hasErrors", false)
            .data("configuration", config)
            .data("operations", operations)
            .data("currentOps", currentOps)
    }

    // Channel edit view
    fun createChannelEditView(request: HttpServletRequest, channel: Channel): TemplateInstance {
        return mainTemplate
            .data("factoidCount", factoidDao.count())
            .data("loggedIn", isLoggedIn(request))
            .data("isAdmin", isAdmin(request))
            .data("channels", channelDao.getChannels(isAdmin(request)))
            .data("apis", apiDao.findAll())
            .data("sofia", Sofia)
            .data("errors", emptyList<String>())
            .data("hasErrors", false)
            .data("channel", channel)
    }

    // Javadoc admin view
    fun createJavadocAdminView(request: HttpServletRequest): TemplateInstance {
        return mainTemplate
            .data("factoidCount", factoidDao.count())
            .data("loggedIn", isLoggedIn(request))
            .data("isAdmin", isAdmin(request))
            .data("channels", channelDao.getChannels(isAdmin(request)))
            .data("apis", apiDao.findAll())
            .data("sofia", Sofia)
            .data("errors", emptyList<String>())
            .data("hasErrors", false)
    }

    // Error views
    fun createError403View(): TemplateInstance {
        return error403Template.data("image", getRandomImage(IMAGE_403))
    }

    fun createError404View(): TemplateInstance {
        return error404Template.data("image", getRandomImage(IMAGE_404))
    }

    fun createError500View(): TemplateInstance {
        return error500Template.data("image", getRandomImage(IMAGE_500))
    }

    // Helper methods
    private fun isLoggedIn(request: HttpServletRequest): Boolean {
        return INSTANCE.getBySessionToken(getSessionCookie(request)?.value) != null
    }

    private fun isAdmin(request: HttpServletRequest): Boolean {
        val cookie = getSessionCookie(request)
        if (cookie != null) {
            val user = INSTANCE.getBySessionToken(cookie.value)
            return user != null && adminDao.getAdminByEmailAddress(user.email) != null
        }
        return false
    }

    private fun getSessionCookie(request: HttpServletRequest): Cookie? {
        return request.cookies?.firstOrNull { it.name == JavabotConfiguration.SESSION_TOKEN_NAME }
    }

    private fun encode(value: String): String {
        return java.net.URLEncoder.encode(value, "UTF-8")
    }

    private fun getRandomImage(images: Array<String>): String {
        return images.random()
    }

    // Helper data class for paged views
    data class PageData(val requestedPage: Int, val itemCount: Long, val itemsPerPage: Int) {
        val pageCount: Int = (itemCount.toDouble() / itemsPerPage).let { kotlin.math.ceil(it).toInt() }

        val page: Int =
            when {
                requestedPage < 1 -> 1
                requestedPage > pageCount -> pageCount
                else -> requestedPage
            }

        val index: Int =
            when {
                itemCount == 0L -> -1
                (page - 1) * itemsPerPage > itemCount -> (pageCount - 1) * itemsPerPage
                else -> (page - 1) * itemsPerPage
            }

        val startRange: Long = index + 1L

        val endRange: Long = minOf(itemCount, startRange + itemsPerPage - 1)

        val nextPage: String? = if (page + 1 <= pageCount) "?page=${page + 1}" else null

        val previousPage: String? = if (page > 1) "?page=${page - 1}" else null
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(TemplateService::class.java)
        val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm")
        val LOG_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm")
        const val ITEMS_PER_PAGE = 50
        private val IMAGE_403 = arrayOf("403_1.gif", "403_2.gif", "403_3.gif")
        private val IMAGE_404 = arrayOf("404_1.gif", "404_2.gif", "404_3.gif", "404_4.gif")
        private val IMAGE_500 = arrayOf("500.gif")
    }
}
