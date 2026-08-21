package javabot.web.views

import com.antwerkz.sofia.Sofia
import com.google.inject.Injector
import io.quarkus.qute.Engine
import io.quarkus.qute.ReflectionValueResolver
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import io.quarkus.qute.TemplateLocator
import io.quarkus.qute.ValueResolver
import io.quarkus.qute.Variant
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import java.io.InputStreamReader
import java.io.Reader
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional
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

/**
 * Renders the site's Qute templates.
 *
 * This owns a standalone [Engine] rather than relying on CDI's `@Location`-injected [Template]
 * beans, so it can be constructed identically by Quarkus/Arc at runtime and by plain Guice in tests
 * (the existing test suite builds the object graph with Guice, which has no notion of Quarkus's
 * build-time Qute wiring).
 *
 * Page composition mirrors the old FreeMarker `main.ftl` -> `paged.ftl` -> `<child>.ftl` nesting:
 * [mainTemplate] always renders the site chrome and dynamically includes whatever template is named
 * by the "contentTemplate" data key (see `templates/main.html`); paged views additionally set
 * "pagedView" to name the innermost content template that `templates/paged.html` includes.
 *
 * DAOs and [Javabot] are Guice-managed, not CDI beans, so they're pulled from the shared [Injector]
 * (see `GuiceInjectorProducer`) rather than being constructor-injected directly -- Arc has no bean
 * definitions for the domain layer.
 */
@ApplicationScoped
class TemplateService @Inject constructor(private val injector: Injector) {

    private val adminDao: AdminDao by lazy { injector.getInstance(AdminDao::class.java) }
    private val channelDao: ChannelDao by lazy { injector.getInstance(ChannelDao::class.java) }
    private val factoidDao: FactoidDao by lazy { injector.getInstance(FactoidDao::class.java) }
    private val apiDao: ApiDao by lazy { injector.getInstance(ApiDao::class.java) }
    private val karmaDao: KarmaDao by lazy { injector.getInstance(KarmaDao::class.java) }
    private val logsDao: LogsDao by lazy { injector.getInstance(LogsDao::class.java) }
    private val changeDao: ChangeDao by lazy { injector.getInstance(ChangeDao::class.java) }
    private val configDao: ConfigDao by lazy { injector.getInstance(ConfigDao::class.java) }
    private val javabot: Javabot by lazy { injector.getInstance(Javabot::class.java) }

    private val engine: Engine = buildEngine()

    private val mainTemplate: Template = engine.getTemplate("main.html")
    private val error403Template: Template = engine.getTemplate("error/403.html")
    private val error404Template: Template = engine.getTemplate("error/404.html")
    private val error500Template: Template = engine.getTemplate("error/500.html")

    // Index view
    fun createIndexView(request: HttpServletRequest): TemplateInstance {
        return mainTemplate.data(shellData(request))
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

        val data = shellData(request)
        data["contentTemplate"] = "paged.html"
        data["pagedView"] = "factoids.html"
        data.putAll(pagedData(pageData, factoids))
        data["filter"] = filter
        return mainTemplate.data(data)
    }

    // Karma view
    fun createKarmaView(request: HttpServletRequest, page: Int): TemplateInstance {
        val pageData = PageData(page, karmaDao.count(), ITEMS_PER_PAGE)
        val karmaList = karmaDao.list(QueryParam(pageData.index, ITEMS_PER_PAGE, "value", false))

        val data = shellData(request)
        data["contentTemplate"] = "paged.html"
        data["pagedView"] = "karma.html"
        data.putAll(pagedData(pageData, karmaList))
        return mainTemplate.data(data)
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

        val data = shellData(request)
        data["contentTemplate"] = "paged.html"
        data["pagedView"] = "changes.html"
        data.putAll(pagedData(pageData, changes))
        data["message"] = message
        return mainTemplate.data(data)
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

        val data = shellData(request)
        data["contentTemplate"] = "logs.html"
        data["logs"] = logs
        data["channel"] = channel
        data["today"] = today
        data["yesterday"] = yesterday
        data["tomorrow"] = tomorrow
        return mainTemplate.data(data)
    }

    // Admin index view
    fun createAdminIndexView(
        request: HttpServletRequest,
        current: Admin,
        editing: Admin?,
    ): TemplateInstance {
        val data = shellData(request)
        data["contentTemplate"] = "admin/index.html"
        data["current"] = current
        data["editing"] = editing
        data["admins"] = adminDao.findAll()
        return mainTemplate.data(data)
    }

    // Configuration view
    fun createConfigurationView(request: HttpServletRequest): TemplateInstance {
        val config = configDao.get()
        val operations = javabot.getAllOperations().values.sortedBy { it.getName() }
        val currentOps = config.operations.toSet()

        val data = shellData(request)
        data["contentTemplate"] = "admin/configuration.html"
        data["configuration"] = config
        data["operations"] = operations
        data["currentOps"] = currentOps
        return mainTemplate.data(data)
    }

    // Channel edit view
    fun createChannelEditView(request: HttpServletRequest, channel: Channel): TemplateInstance {
        val data = shellData(request)
        data["contentTemplate"] = "admin/editChannel.html"
        data["channel"] = channel
        return mainTemplate.data(data)
    }

    // Javadoc admin view
    fun createJavadocAdminView(request: HttpServletRequest): TemplateInstance {
        val data = shellData(request)
        data["contentTemplate"] = "admin/javadoc.html"
        return mainTemplate.data(data)
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

    // Data shared by every page that renders through main.html
    private fun shellData(request: HttpServletRequest): MutableMap<String, Any?> {
        return mutableMapOf(
            "factoidCount" to factoidDao.count(),
            "loggedIn" to isLoggedIn(request),
            "isAdmin" to isAdmin(request),
            "channels" to channelDao.getChannels(isAdmin(request)),
            "currentChannel" to "",
            "apis" to apiDao.findAll(),
            "sofia" to Sofia,
            "errors" to emptyList<String>(),
            "hasErrors" to false,
            "contentTemplate" to null,
        )
    }

    // Data shared by every page rendered through paged.html
    private fun pagedData(pageData: PageData, pageItems: List<*>): Map<String, Any?> {
        return mapOf(
            "page" to pageData.page,
            "itemCount" to pageData.itemCount,
            "pageCount" to pageData.pageCount,
            "startRange" to pageData.startRange,
            "endRange" to pageData.endRange,
            "nextPage" to pageData.nextPage,
            "previousPage" to pageData.previousPage,
            "pageItems" to pageItems,
        )
    }

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

    private fun getRandomImage(images: Array<String>): String {
        return images.random()
    }

    // Builds a standalone Qute engine that loads templates from src/main/resources/templates
    // on the classpath and knows how to format the domain dates/strings the templates use.
    private fun buildEngine(): Engine {
        return Engine.builder()
            .addDefaults()
            .addValueResolver(dateResolver("format", DATE_TIME_FORMATTER))
            .addValueResolver(dateResolver("logFormat", LOG_FORMAT))
            .addValueResolver(
                ValueResolver.builder()
                    .applyToBaseClass(String::class.java)
                    .applyToName("urlEncode")
                    .applyToNoParameters()
                    .resolveSync { ctx ->
                        URLEncoder.encode(ctx.base as String, StandardCharsets.UTF_8)
                    }
                    .build()
            )
            .addValueResolver(
                ValueResolver.builder()
                    .applyToBaseClass(Enum::class.java)
                    .applyToName("toLowerCase")
                    .applyToNoParameters()
                    .resolveSync { ctx -> (ctx.base as Enum<*>).name.lowercase() }
                    .build()
            )
            // addDefaults() only wires up map/collection/logic resolvers; plain bean property
            // access (channel.name, factoid.value, admin.ircName, ...) needs this explicitly.
            .addValueResolver(ReflectionValueResolver())
            .addLocator(::locate)
            .build()
    }

    private fun dateResolver(name: String, formatter: DateTimeFormatter): ValueResolver {
        return ValueResolver.builder()
            .applyToBaseClass(LocalDateTime::class.java)
            .applyToName(name)
            .applyToNoParameters()
            .resolveSync { ctx -> formatter.format(ctx.base as LocalDateTime) }
            .build()
    }

    private fun locate(id: String): Optional<TemplateLocator.TemplateLocation> {
        val resource = javaClass.classLoader.getResource("templates/$id") ?: return Optional.empty()
        return Optional.of(
            object : TemplateLocator.TemplateLocation {
                override fun read(): Reader =
                    InputStreamReader(resource.openStream(), StandardCharsets.UTF_8)

                override fun getVariant(): Optional<Variant> = Optional.empty()
            }
        )
    }

    // Helper data class for paged views
    data class PageData(val requestedPage: Int, val itemCount: Long, val itemsPerPage: Int) {
        val pageCount: Int =
            (itemCount.toDouble() / itemsPerPage).let { kotlin.math.ceil(it).toInt() }

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
        val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm")
        val LOG_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm")
        const val ITEMS_PER_PAGE = 50
        private val IMAGE_403 = arrayOf("403_1.gif", "403_2.gif", "403_3.gif")
        private val IMAGE_404 = arrayOf("404_1.gif", "404_2.gif", "404_3.gif", "404_4.gif")
        private val IMAGE_500 = arrayOf("500.gif")
    }
}
