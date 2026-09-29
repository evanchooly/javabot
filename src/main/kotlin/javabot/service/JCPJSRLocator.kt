package javabot.service

import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.io.IOException
import org.jsoup.Jsoup

@Singleton
class JCPJSRLocator @Inject constructor(private val httpService: HttpService) {
    private fun locate(jsr: Int): Pair<String, String?> {
        var title: String? = null
        val urlString = "http://www.jcp.org/en/jsr/detail?id=$jsr"
        try {
            val extracted =
                Jsoup.parse(httpService.get(urlString))
                    .select("h1")
                    .first()
                    ?.textNodes()
                    ?.map { element -> element.text().trim() }
                    ?.joinToString(separator = " ") ?: throw IOException()
            // jcp.org serves this exact page (with its own <h1>, HTTP 200) for any unknown JSR id
            // instead of a 404 -- without this check, an unknown JSR would be reported as if its
            // title were "The specified JSR was not found."
            title = extracted.takeUnless { it == "The specified JSR was not found." }
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
