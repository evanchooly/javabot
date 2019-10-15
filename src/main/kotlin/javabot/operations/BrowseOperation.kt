package javabot.operations

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.rivescript.Config
import com.rivescript.RiveScript
import javabot.Javabot
import javabot.JavabotConfig
import javabot.Message
import javabot.dao.AdminDao
import javabot.operations.browse.BrowseResult
import javabot.service.HttpService
import javabot.service.RiveScriptService
import javabot.service.UrlCacheService
import net.swisstech.bitly.BitlyClient
import okhttp3.OkHttpClient
import javax.annotation.Nullable

class BrowseOperation @Inject constructor(bot: Javabot, adminDao: AdminDao,
                                          private val httpService: HttpService,
                                          var config: JavabotConfig,
                                          private val urlCacheService: UrlCacheService,
                                          private val rivescript: RiveScriptService) : BotOperation(bot, adminDao) {
    companion object {
        val typeReference = object : TypeReference<Array<BrowseResult>>() {}
    }

    private val client = OkHttpClient()
    @field:[Nullable Inject(optional = true)]
    var bitly: BitlyClient? = null
        get() {
            if (field == null) {
                field = if (config.bitlyToken() != "") BitlyClient(config.bitlyToken()) else null
            }
            return field
        }

    init {
        RiveScript::class.java.getResourceAsStream("/rive/browse.rive").use {
            rivescript.loadInputStream(it)
        }

        rivescript.setSubroutine("browseSingle") { rs, args -> callService(args[0]) }
        rivescript.setSubroutine("browseDouble") { rs, args -> callService(args[0], args[1]) }
        // must be the last operation in the constructor after scripts are loaded
        rivescript.sortReplies()
    }

    private fun correctReference(cardinality: Int, text: String): String {
        return if (cardinality == 1) {
            "A ${text.toLowerCase()}"
        } else {
            "${text}s"
        }
    }

    private fun callService(clazz: String, module: String? = null): String {
        return try {
            val sourceData = httpService.get(
                    "https://java-browser.yawk.at/api/javabotSearch/v1",
                    mapOf(
                            "term" to clazz,
                            "artifact" to module
                    )
                            .mapNotNull { it.value?.let { value -> it.key to value } }
                            .toMap()
            )
            val data = ObjectMapper().configure(JsonParser.Feature.ALLOW_COMMENTS, true)
                    .readValue(sourceData, typeReference) as Array<BrowseResult>
            data
                    .map { urlCacheService.shorten("https://java-browser.yawk.at${it.uri}")+" [${it.binding}]" }
                    .joinToString(
                            separator = ", ",
                            prefix = correctReference(data.size, "Reference")
                                    + " matching '$clazz' can be found at: ")
        } catch (e: Throwable) {
            e.printStackTrace()

            return "ERR: not found"
        }
    }

    override fun handleMessage(event: Message): List<Message> {
        return listOfNotNull(rivescript.reply(event.user.nick, event.value))
                .filter { !it.startsWith("ERR:") }
                .map {
                    Message(event, it)
                }
    }
}