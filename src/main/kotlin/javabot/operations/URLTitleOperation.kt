package javabot.operations

import javabot.Message
import javabot.operations.urlcontent.URLContentAnalyzer
import javabot.operations.urlcontent.URLFromMessageParser
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import javax.inject.Inject
import java.io.IOException
import java.net.URL
import java.util.stream.Collectors

import java.lang.String.format

public class URLTitleOperation : BotOperation() {
    Inject
    var analyzer: URLContentAnalyzer
    Inject
    var parser: URLFromMessageParser

    override fun handleChannelMessage(event: Message): Boolean {
        val message = event.value
        try {
            val titlesToPost = parser.urlsFromMessage(message)!!.stream().map(Function<URL, String> { it.toString() }).map(
                  { s -> findTitle(s, true) }).filter(({ s -> s != null })).collect(Collectors.toList<String>())
            if (titlesToPost.isEmpty()) {
                return false
            } else {
                postMessageToChannel(titlesToPost, event)
                return true
            }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
            return false
        }

    }

    private fun postMessageToChannel(titlesToPost: List<String>, event: Message) {
        val botMessage: String
        if (titlesToPost.size() == 1) {
            botMessage = format("%s'%s title: %s",
                  event.user.nick,
                  if (event.user.nick.endsWith("s")) "" else "s",
                  titlesToPost.get(0))
        } else {
            botMessage = format("%s'%s titles: %s",
                  event.user.nick,
                  if (event.user.nick.endsWith("s")) "" else "s",
                  String.join(" | ", titlesToPost.stream().map({ s -> "\"" + s + "\"" }).collect(Collectors.toList<String>())))
        }

        bot.postMessageToChannel(event, botMessage)
    }

    private fun findTitle(url: String, loop: Boolean): String? {
        if (analyzer.precheck(url)) {
            try {
                HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build().use { client ->
                    val httpget = HttpGet(url)
                    httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0")
                    val response = client.execute(httpget)
                    val entity = response.entity

                    try {
                        if (!(response.statusLine.statusCode == 404 || response.statusLine.statusCode == 403) && entity != null) {

                            val doc = Jsoup.parse(EntityUtils.toString(entity))
                            val title = clean(doc.title())
                            return if ((analyzer.check(url, title))) title else null
                        } else {
                            return null
                        }
                    } finally {
                        EntityUtils.consume(entity)
                    }
                }
            } catch (ioe: IOException) {
                if (loop && !url.substring(0, 10).contains("//www.")) {
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
        title.chars().filter({ i -> i < 127 }).forEach({ i -> sb.append(i.toChar()) })
        return sb.toString()
    }

    companion object {

        val requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(5000).setSocketTimeout(5000).build()
    }
}
