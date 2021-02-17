package javabot.operations

import com.fasterxml.jackson.databind.ObjectMapper
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.operations.urlcontent.URLContentAnalyzer
import javabot.operations.urlcontent.URLFromMessageParser
import javabot.service.HttpService
import javabot.service.TwitterService
import org.apache.http.client.utils.URLEncodedUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements
import java.io.IOException
import java.net.URI
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.inject.Inject


class URLTitleOperation @Inject constructor(bot: Javabot, adminDao: AdminDao,
                                            private val analyzer: URLContentAnalyzer,
                                            private val parser: URLFromMessageParser,
                                            private val httpService: HttpService,
                                            private val twitterService: TwitterService)
    : BotOperation(bot, adminDao) {

    override fun handleChannelMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        try {
            val titlesToPost = parser.urlsFromMessage(message)
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

    private fun postMessageToChannel(responses: MutableList<Message>, titlesToPost: List<String>, event: Message) {
        val distinctTitles=titlesToPost.distinct()
        val title = if (distinctTitles.size == 1) "title" else "titles"
        responses.add(Message(event, "${event.user.nick}'s $title: " +
                distinctTitles.joinToString(" | ", transform = { s ->
                    if (s.length > 240) {
                        """"${s.substring(0, 239)}...""""
                    } else {
                        """"$s""""
                    }
                })))
    }

    private fun findTitle(url: String, loop: Boolean): String? {
        if (analyzer.precheck(url)) {
            try {
                val typedUrl = URL(url)
                if (typedUrl.host.contains("youtube.com", true)) {
                    // youtube needs special handling.
                    return findYoutubeTitle(url)
                }
                if (typedUrl.host.contains("twitter.com", true)) {
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

    @Suppress("UNCHECKED_CAST")
    private fun findYoutubeTitle(url: String): String {
        // need to get the actual video id
        val v = URLEncodedUtils.parse(URI(url), "UTF-8").firstOrNull { it.name == "v" } ?: return ""

        val data = httpService.get("http://youtube.com/get_video_info?video_id=${v.value}")
        // parse returns a List of field/value pairs
        val playerResponseField = URLEncodedUtils.parse(data, StandardCharsets.UTF_8)
                .firstOrNull { it.name == "player_response" } ?: return ""

        val map: Map<String, *> = ObjectMapper().readValue(playerResponseField.value, Map::class.java) as Map<String, *>
        val videoDetails: Map<String, String?> = (map["videoDetails"] ?: return "") as Map<String, String>

        return videoDetails["title"] ?: ""
    }

    private fun findTwitterTitle(url: String): String {
        if (twitterService.isEnabled()) {
            if (url.contains("status")) {
                val regex = Regex("status/\\d++")
                val match = regex.find(url) // if match isn't null, we have a status number now, woo
                if (match != null) {
                    val id = (match.value.substring("status/".length)).toLong()
                    return twitterService.getStatus(id) ?: ""
                }
            }
            // what happens if it's actually a twitter user? Who cares, we haven't seen that yet in use, right?
        }
        return ""
    }

    private fun fixTwitterLinks(elt: Element) {
        elt.children().forEach { fixTwitterLinks(it) }
        val twitterLinks = elt.getElementsByAttributeValue("data-pre-embedded", "true")
        twitterLinks.forEach { link ->
            link.insertChildren(0, TextNode(" "))
        }
    }

    private fun parseTitle(doc: Document): String {
        val header = doc.select("meta[property=\"og:title\"]")
        return if (header.isNotEmpty()) {
            val body: Elements = doc.select(".permalink-tweet-container .js-tweet-text-container") ?: Elements()
            if (body.isNotEmpty()) {
                body.forEach { fixTwitterLinks(it) }
                String.format("%s: \"%s\"",
                        header.first().attr("content"),
                        body.first().text())
            } else {
                doc.title()
            }
        } else {
            doc.title()
        }
    }

    private fun clean(title: String) = title // IRC can handle real characters
}
