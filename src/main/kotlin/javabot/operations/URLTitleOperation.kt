package javabot.operations

import com.fasterxml.jackson.databind.ObjectMapper
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.operations.urlcontent.URLContentAnalyzer
import javabot.operations.urlcontent.URLFromMessageParser
import javabot.service.HttpService
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
                                            private var analyzer: URLContentAnalyzer,
                                            var parser: URLFromMessageParser,
                                            private val httpService: HttpService) : BotOperation(bot, adminDao) {

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
            ignored.printStackTrace()
            return responses
        }
    }

    private fun postMessageToChannel(responses: MutableList<Message>, titlesToPost: List<String>, event: Message) {
        val title = if (titlesToPost.size == 1) "title" else "titles"
        responses.add(Message(event, "${event.user.nick}'s $title: " +
                titlesToPost.joinToString(" | ", transform = { s ->
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
                return if (URL(url).host.contains("youtube.com", true)) {
                    // youtube needs special handling.
                    findYoutubeTitle(url)
                } else {
                    val document = Jsoup.parse(httpService.get(url))
                    var title = parseTitle(document)
                    title = clean(title)
                    if (analyzer.check(url, title)) title else null
                }
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

    private fun clean(title: String) = title.replace("[^\\p{InBASIC_LATIN}]".toRegex(), "")
}
