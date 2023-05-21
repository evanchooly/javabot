package javabot.service

import com.google.inject.Inject
import com.google.inject.Singleton
import java.io.IOException
import org.jsoup.Jsoup

@Singleton
class JCPJSRLocator @Inject constructor(private val httpService: HttpService) {
    private fun locate(jsr: Int): Pair<String, String?> {
        var title: String? = null
        val urlString = "http://www.jcp.org/en/jsr/detail?id=$jsr"
        try {
            title =
                Jsoup.parse(httpService.get(urlString))
                    .select("div.header1")
                    .first()
                    ?.textNodes()
                    ?.map { element -> element.text().trim() }
                    ?.joinToString(separator = " ")
                    ?: throw IOException()
        } catch (ignored: Exception) {}

        return urlString to title
    }

    fun findInformation(jsr: Int): String {
        val (url, title) = locate(jsr)
        if (title.isNullOrEmpty()) {
            return ""
        }
        return "'$title' can be found at $url"
    }
}
