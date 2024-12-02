package javabot.operations.urlcontent

import jakarta.inject.Singleton
import java.util.HashMap
import java.util.Locale
import java.util.regex.Pattern
import java.util.stream.Stream

@Singleton
class URLContentAnalyzer {

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
        for (pattern in patterns) {
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
        val wordsInTitle = title.lowercase(Locale.getDefault()).split(" ")
        val words =
            wordsInTitle.map { s -> s.replace("[\\W]|_".toRegex(), "") }.count { s -> s.length > 2 }
        // generate a ratio of words to occurrences in the title.
        val hits =
            wordsInTitle
                .map { s -> s.replace("[\\W]|_".toRegex(), "") }
                .filter { s -> s.length > 2 }
                .count { s ->
                    url.lowercase(Locale.getDefault()).contains(s.lowercase(Locale.getDefault()))
                }
        // ratio close enough to 1:1? reject.
        val ratio = (hits * 1.0) / words
        if ((hits * 1.0) / words >= 0.8) {
            throw ContentException(
                "Rejected: Ratio of content from ${url} to ${title} was ${ratio}"
            )
        }
    }

    inner class ContentException(message: String) : Exception(message)

    companion object {
        val patterns: Array<String> =
            arrayOf(
                "astebin",
                "mysticpaste\\.com",
                "pastie",
                "gist\\.github\\.com",
                "ideone\\.com",
                "docs\\.oracle\\.com.*api",
                "git\\.io",
                "localhost",
                "127\\.0\\.0\\.1",
                "glot\\.io"
            )
        val matchingPatterns: MutableMap<String, Pattern> = HashMap()

        init {
            Stream.of(*patterns).forEach { s -> matchingPatterns.put(s, Pattern.compile(".*$s.*")) }
        }
    }
}
