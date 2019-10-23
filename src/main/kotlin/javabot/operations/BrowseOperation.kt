package javabot.operations

import com.antwerkz.sofia.Sofia
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
import javabot.service.UrlCacheService

class BrowseOperation @Inject constructor(bot: Javabot, adminDao: AdminDao,
                                          val httpService: HttpService,
                                          val config: JavabotConfig,
                                          val urlCacheService: UrlCacheService) : BotOperation(bot, adminDao) {
    companion object {
        val typeReference = object : TypeReference<Array<BrowseResult>>() {}
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
            if (data.isNotEmpty()) {
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
                        } +
                        "found."
            }
        } catch (e: Throwable) {
            throw e
        }
    }

    override fun handleMessage(event: Message): List<Message> {
        val tokens = event.value.split("\\s".toRegex())
        val (command, part1, part2) = listOf(
                if (tokens.isNotEmpty()) tokens[0] else null,
                if (tokens.size > 1) tokens[1] else null,
                if (tokens.size > 2) tokens[2] else null
        )
        return try {
            if ("browse".equals(command, true) && part1 != null) {
                if ("-help".equals(part1, true)) {
                    listOf(Message(event, Sofia.browseHelp()))
                } else {
                    if (part2 != null) {
                        listOf(Message(event, callService(part2, part1)))
                    } else {
                        listOf(Message(event, callService(part1)))
                    }
                }
            } else {
                emptyList()
            }
        } catch (_: Throwable) {
            emptyList()
        }
    }
}