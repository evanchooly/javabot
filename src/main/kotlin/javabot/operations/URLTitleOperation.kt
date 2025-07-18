package javabot.operations

import java.io.IOException
import java.net.URI
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.operations.urlcontent.URLContentAnalyzer
import javabot.operations.urlcontent.URLFromMessageParser
import javabot.service.HttpService
import javabot.service.TwitterService
import javax.inject.Inject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements

class URLTitleOperation
@Inject
constructor(
    bot: Javabot,
    adminDao: AdminDao,
    private val analyzer: URLContentAnalyzer,
    private val parser: URLFromMessageParser,
    private val httpService: HttpService,
    private val twitterService: TwitterService,
) : BotOperation(bot, adminDao) {

    override fun handleChannelMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        try {
            val titlesToPost =
                parser
                    .urlsFromMessage(message)
                    .map { it.toString() }
                    .distinct()
                    .mapNotNull { s -> findTitle(s, true) }
                    .filter { s -> s.length >= 20 }
            return if (titlesToPost.isEmpty()) {
                responses
            } else {
                postMessageToChannel(responses, titlesToPost, event)
                responses
            }
        } catch (ignored: Exception) {
            return responses
        }
    }

    private fun postMessageToChannel(
        responses: MutableList<Message>,
        titlesToPost: List<String>,
        event: Message,
    ) {
        val distinctTitles = titlesToPost.distinct()
        val title = if (distinctTitles.size == 1) "title" else "titles"
        responses.add(
            Message(
                event,
                "${event.user.nick}'s $title: " +
                    distinctTitles.joinToString(
                        " | ",
                        transform = { s ->
                            if (s.length > 240) {
                                """"${s.substring(0, 239)}...""""
                            } else {
                                """"$s""""
                            }
                        },
                    ),
            )
        )
    }

    fun hostBasename(host: String, sections: Int = 2): String {
        return host.split(".").takeLast(sections).joinToString(".").lowercase()
    }

    private fun findTitle(url: String, loop: Boolean): String? {
        val twitterUrls = listOf("twitter.com", "x.com")
        if (analyzer.precheck(url)) {
            @Suppress("GrazieInspection")
            try {
                val typedUrl = URI(url).toURL()
                // there used to be parsing here for youtube but they use <title> now
                if (twitterUrls.contains(typedUrl.host.lowercase())) {
                    return findTwitterTitle(url)
                }
                val document = Jsoup.parse(httpService.get(url))
                var title = parseTitle(document)
                title = clean(title)
                return if (analyzer.check(url, title)) title else null
            } catch (ioe: IOException) {
                return if (loop && !url.take(10).contains("//www.")) {
                    val tUrl = url.replace("//", "//www.")
                    findTitle(tUrl, false)
                } else {
                    null
                }
            } catch (ignored: IllegalArgumentException) {
                return null
            }
        } else {
            return null
        }
    }

    private fun findTwitterTitle(url: String): String {
        @Suppress("GrazieInspection")
        if (twitterService.isEnabled()) {
            if (url.contains("status")) {
                val regex = Regex("status/\\d++")
                val match = regex.find(url) // if match isn't null, we have a status number now, woo
                if (match != null) {
                    val id = (match.value.substring("status/".length)).toLong()
                    return twitterService.getStatus(id) ?: ""
                }
            }
            // what happens if it's actually a twitter user? Who cares, we haven't seen that yet in
            // use, right?
        }
        return ""
    }

    private fun fixTwitterLinks(elt: Element) {
        elt.children().forEach { fixTwitterLinks(it) }
        val twitterLinks = elt.getElementsByAttributeValue("data-pre-embedded", "true")
        twitterLinks.forEach { link -> link.insertChildren(0, TextNode(" ")) }
    }

    private fun parseTitle(doc: Document): String {
        val activityPubHeader = doc.select("link[type=\"application/activity+json\"]")
        if (activityPubHeader.isNotEmpty()) {
            val description = doc.head().select("meta[property=\"og:description\"]")
            if (description.isNotEmpty()) {
                val text = description.first()!!.attr("content")
                return text
            }
        }
        val header = doc.select("meta[property=\"og:title\"]")
        return if (header.isNotEmpty()) {
            val body: Elements =
                doc.select(".permalink-tweet-container .js-tweet-text-container") ?: Elements()
            if (body.isNotEmpty()) {
                body.forEach { fixTwitterLinks(it) }
                String.format("%s: \"%s\"", header.first()?.attr("content"), body.first()?.text())
            } else {
                doc.title()
            }
        } else {
            doc.title()
        }
    }

    private fun clean(title: String) = title // IRC can handle real characters
}
