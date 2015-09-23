package javabot.operations.urlcontent

import org.apache.commons.lang.StringUtils.isBlank

import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.StringUtils

import java.net.URL
import java.util.ArrayList
import java.util.stream.Collectors

public class URLFromMessageParser {

    public fun urlsFromMessage(message: String): List<URL>? {
        if (isBlank(message)) {
            return null
        }

        val potentialUrlsFound = ArrayList<String>()
        var idxHttp = message.indexOf("http")
        while (idxHttp >= 0) {
            val idxSpace = message.indexOf(' ', idxHttp)
            val url = if ((idxSpace == -1)) message.substring(idxHttp) else message.substring(idxHttp, idxSpace)
            potentialUrlsFound.add(stripPunctuation(message, url, idxHttp))
            idxHttp = if ((idxSpace == -1)) -1 else message.indexOf("http", idxSpace)
        }

        return potentialUrlsFound.stream().map(Function<String, URL> { this.urlFromToken(it) }).filter({ url -> url != null }).collect(
              Collectors.toList<URL>())
    }

    private fun stripPunctuation(message: String, url: String, idxUrlStart: Int): String {
        val last = url.charAt(url.length() - 1)

        val idxPunc = ArrayUtils.indexOf(CLOSE_PUNCTUATION, last)
        if (idxPunc == -1) {
            return url
        }

        //Walk backwards in message from urlStart, and strip the punctuation if an open brace/bracket is seen
        //before another close.  Otherwise, return the url as is.
        for (c in StringUtils.reverse(message.substring(0, idxUrlStart)).toCharArray()) {
            if (c == OPEN_PUNCTUATION[idxPunc]) {
                return url.substring(0, url.length() - 1)
            }
            if (c == CLOSE_PUNCTUATION[idxPunc]) {
                return url
            }
        }
        return url
    }

    private fun urlFromToken(token: String): URL? {
        try {
            return URL(token)
        } catch (e: Exception) {
            return null
        }

    }

    companion object {

        private val OPEN_PUNCTUATION = charArrayOf('{', '(', '[')
        private val CLOSE_PUNCTUATION = charArrayOf('}', ')', ']')
    }
}
