package javabot.operations

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import javabot.Javabot
import javabot.JavabotConfig
import javabot.Message
import javabot.dao.AdminDao
import javabot.operations.browse.BrowseResult
import javabot.service.HttpService
import javabot.service.RiveScriptService
import javabot.service.UrlCacheService

class BrowseOperation @Inject constructor(bot: Javabot, adminDao: AdminDao,
                                          private val httpService: HttpService,
                                          var config: JavabotConfig,
                                          private val urlCacheService: UrlCacheService,
                                          private val rivescript: RiveScriptService) : BotOperation(bot, adminDao) {
    companion object {
        val typeReference = object : TypeReference<Array<BrowseResult>>() {}
    }

    init {
        rivescript.load("/rive/browse.rive")

        rivescript.setSubroutine("browseSingle") { _, args -> callService(args[0]) }
        rivescript.setSubroutine("browseDouble") { _, args -> callService(args[1], args[0]) }
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
            if (data.size > 0) {
                data
                        .map { urlCacheService.shorten("https://java-browser.yawk.at${it.uri}") + " [${it.binding}]" }
                        .joinToString(
                                separator = ", ",
                                prefix = correctReference(data.size, "Reference")
                                        + " matching '$clazz' can be found at: ")
            } else {
                "No source matching `$clazz` " +
                        if (module != null) {
                            "and module `$module` "
                        } else {
                            ""
                        }
            } +
                    "found."
        } catch (e: Throwable) {
            "ERR: not found"
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