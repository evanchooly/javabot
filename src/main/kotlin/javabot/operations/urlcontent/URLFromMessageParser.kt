package javabot.operations.urlcontent

import java.net.URL
import java.util.ArrayList
import java.util.stream.Collectors
import org.apache.commons.lang.StringUtils.isBlank
import org.apache.commons.lang3.ArrayUtils

class URLFromMessageParser {

    fun urlsFromMessage(message: String): List<URL> {
        if (isBlank(message)) {
            return listOf()
        }

        val potentialUrlsFound = ArrayList<String>()
        var idxHttp = message.indexOf("http")
        while (idxHttp >= 0) {
            val idxSpace = message.indexOf(' ', idxHttp)
            val url =
                if (idxSpace == -1) message.substring(idxHttp)
                else message.substring(idxHttp, idxSpace)
            potentialUrlsFound.add(stripPunctuation(message, url, idxHttp))
            idxHttp = if (idxSpace == -1) -1 else message.indexOf("http", idxSpace)
        }

        val list = arrayListOf<URL>()
        potentialUrlsFound.map { this.urlFromToken(it) }.filterNotNullTo(list)
        return list
    }

    private fun stripPunctuation(message: String, url: String, idxUrlStart: Int): String {
        val last = url[url.length - 1]

        val idxPunc = ArrayUtils.indexOf(CLOSE_PUNCTUATION, last)
        if (idxPunc == -1) {
            return url
        } else {
            return url.substring(0, url.indexOf(CLOSE_PUNCTUATION[idxPunc]))
        }

        // Walk backwards in message from urlStart, and strip the punctuation if an open
        // brace/bracket is seen
        // before another close.  Otherwise, return the url as is.
        //        for (c in StringUtils.reverse(message.substring(0, idxUrlStart)).toCharArray()) {
        //            if (c == OPEN_PUNCTUATION[idxPunc]) {
        //                return url.substring(0, url.length - 1)
        //            }
        //            if (c == CLOSE_PUNCTUATION[idxPunc]) {
        //                return url
        //            }
        //        }
        //        return url
    }

    private fun urlFromToken(token: String): URL? {
        return try {
            val url = URL(token)
            if (blacklistHosts.contains(url.host)) null else url
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private val OPEN_PUNCTUATION = charArrayOf('{', '(', '[')
        private val CLOSE_PUNCTUATION = charArrayOf('}', ')', ']')
        val blacklistHosts: List<String> =
            try {
                this::class
                    .java
                    .getResourceAsStream("/urlBlacklist.csv")
                    .bufferedReader(Charsets.UTF_8)
                    .use { it.lines().collect(Collectors.toList()) }
            } catch (ignored: Exception) {
                emptyList()
            }
    }
}
