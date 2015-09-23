package javabot.operations.urlcontent

import com.google.inject.Singleton

import java.util.HashMap
import java.util.regex.Pattern
import java.util.stream.Stream

Singleton
public class URLContentAnalyzer {

    public fun precheck(url: String): Boolean {
        try {
            checkForBlacklistedSites(url)
            return true
        } catch (e: ContentException) {
            return false
        }

    }

    public fun check(url: String, title: String): Boolean {
        try {
            checkNulls(url, title)
            checkTitleToURLRatio(url, title)
            checkForBlacklistedSites(url)
            return true
        } catch (e: ContentException) {
            // log failure?
            return false
        }

    }

    Throws(ContentException::class)
    private fun checkForBlacklistedSites(url: String) {
        for (pattern in patterns) {
            if (matchingPatterns.get(pattern).matcher(url).matches()) {
                throw ContentException(String.format("Rejected: blacklisted site %s", url))
            }
        }
    }

    Throws(ContentException::class)
    private fun checkNulls(url: String, title: String?) {
        if (null == title || "" == title.trim()) {
            throw ContentException(
                  String.format("Rejected: Title content for %s was empty",
                        url))
        }
    }

    Throws(ContentException::class)
    private fun checkTitleToURLRatio(url: String, title: String) {
        // now break down the words in the title.
        val wordsInTitle = title.toLowerCase().split(" ")
        val words = Stream.of<String>(*wordsInTitle).map<String>(
              { s -> s.replaceAll("[\\W]|_", "") }).filter { s -> s.length() > 2 }.count()
        // generate a ratio of words to occurrences in the title.
        val hits = Stream.of<String>(*wordsInTitle).map<String>(
              { s -> s.replaceAll("[\\W]|_", "") }).filter { s -> s.length() > 2 }.filter { s ->
            url.toLowerCase().contains(s.toLowerCase())
        }.count()

        // ratio close enough to 1:1? reject.
        val ratio = (hits * 1.0) / words
        if ((hits * 1.0) / words >= 0.8) {
            throw ContentException(
                  String.format("Rejected: Ratio of content from  %s to %s was %f%n",
                        url,
                        title,
                        ratio))
        }
    }

    inner class ContentException(message: String) : Exception(message)

    companion object {
        public val patterns: Array<String> = arrayOf("astebin", "mysticpaste.com", "pastie", "gist.github.com", "ideone.com",
              "docs.oracle.com.*api", "git.io")
        public val matchingPatterns: MutableMap<String, Pattern> = HashMap()

        init {
            Stream.of(*patterns).forEach { s -> matchingPatterns.put(s, Pattern.compile(".*$s.*")) }
        }
    }
}
