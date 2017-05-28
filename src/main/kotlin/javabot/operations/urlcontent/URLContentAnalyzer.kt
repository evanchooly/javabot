package javabot.operations.urlcontent

import com.google.inject.Singleton
import java.io.InputStream
import java.util.*
import java.util.regex.Pattern

@Singleton class URLContentAnalyzer {

    fun precheck(url: String): Boolean {
        try {
            checkForBlacklistedSites(url)
            return true
        } catch (e: ContentException) {
            return false
        }

    }

    fun check(url: String, title: String?): Boolean {
        try {
            checkNulls(url, title)
            checkTitleToURLRatio(url, title!!)
            checkForBlacklistedSites(url)
            return true
        } catch (e: ContentException) {
            // log failure?
            return false
        }

    }

    @Throws(ContentException::class)
    private fun checkForBlacklistedSites(url: String) {
        matchingPatterns.keys.forEach { pattern ->
            if (matchingPatterns[pattern]?.matcher(url)?.matches() ?: false) {
                throw ContentException("Rejected: blacklisted site ${url}")
            }
        }
    }

    @Throws(ContentException::class)
    private fun checkNulls(url: String, title: String?) {
        if (null == title || "" == title.trim()) {
            throw ContentException("Rejected: Title content for ${url} was empty")
        }
    }

    @Throws(ContentException::class)
    private fun checkTitleToURLRatio(url: String, title: String) {
        // now break down the words in the title.
        val wordsInTitle = title.toLowerCase().split(" ")
        val words = wordsInTitle
                .map({ s -> s.replace("[\\W]|_".toRegex(), "") })
                .filter { s -> s.length > 2 }
                .count()
        // generate a ratio of words to occurrences in the title.
        val hits = wordsInTitle
                .map({ s -> s.replace("[\\W]|_".toRegex(), "") }).filter { s -> s.length > 2 }.filter { s ->
            url.toLowerCase().contains(s.toLowerCase())
        }.count()

        // ratio close enough to 1:1? reject.
        val ratio = (hits * 1.0) / words
        if ((hits * 1.0) / words >= 0.8) {
            throw ContentException("Rejected: Ratio of content from ${url} to ${title} was ${ratio}")
        }
    }

    inner class ContentException(message: String) : Exception(message)

    companion object {
        val matchingPatterns: Map<String, Pattern> = readBlacklist()

        fun readBlacklist(): Map<String, Pattern> {
            val patternsResource: InputStream? = String::class.java.getResourceAsStream("/blacklist.txt")
            val patterns: MutableMap<String, Pattern> = HashMap()
            if (patternsResource != null) {
                patternsResource.bufferedReader().use {
                    it.lines().forEach {
                        patterns.put(it, Pattern.compile(".*$it.*"))
                    }
                }
            }
            return patterns
        }
    }
}
