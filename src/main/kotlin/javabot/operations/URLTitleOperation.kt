package javabot.operations

import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.operations.urlcontent.URLContentAnalyzer
import javabot.operations.urlcontent.URLFromMessageParser
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.jsoup.Jsoup
import java.io.IOException
import javax.inject.Inject

class URLTitleOperation @Inject constructor(bot: Javabot, adminDao: AdminDao, var analyzer: URLContentAnalyzer,
                                            var parser: URLFromMessageParser) : BotOperation(bot, adminDao) {

    override fun handleChannelMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        try {
            val titlesToPost = parser.urlsFromMessage(message)
                    .map({ it.toString() })
                    .distinct()
                    .mapNotNull({ s -> findTitle(s, true) })
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
        responses.add(Message(event, "${event.user.nick}'s ${title}: " +
                titlesToPost.map({ s -> "\"${s}\"" }).joinToString(" | ")))
    }

    private fun findTitle(url: String, loop: Boolean): String? {
        if (analyzer.precheck(url)) {
            try {
                val document = Jsoup
                        .connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/41.0")
                        .timeout(5000)
                        .get()
                var title = document.title()
                title = clean(title)
                return if (analyzer.check(url, title)) title else null
            } catch (ioe: IOException) {
                if (loop && !url.take(10).contains("//www.")) {
                    val tUrl = url.replace("//", "//www.")
                    return findTitle(tUrl, false)
                } else {
                    return null
                }
            } catch (ignored: IllegalArgumentException) {
                return null
            }
        } else {
            return null
        }
    }

    private fun clean(title: String): String {
        val sb = StringBuilder()
        title.filter({ i -> i.toInt() < 127 }).forEach({ i -> sb.append(i) })
        return sb.toString()
    }
}
