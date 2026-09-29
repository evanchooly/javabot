package javabot.operations.urlcontent

import java.net.URI
import java.net.URL
import java.util.stream.Stream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class URLFromMessageParserTest {

    var parser = URLFromMessageParser()

    @ParameterizedTest
    @MethodSource("getUrlsForParensBracketsStrip")
    fun testUrlFromMessage(message: String, expected: List<URL>) {
        assertEquals(expected, parser.urlsFromMessage(message))
    }

    companion object {
        private val NONE = emptyList<URL>()

        @JvmStatic
        @Throws(Exception::class)
        fun getUrlsForParensBracketsStrip(): Stream<Arguments> =
            Stream.of(
                Arguments.of("ftp://non.http.url.com", NONE),
                Arguments.of("someone says http but it's not a url", NONE),
                Arguments.of("someone says https but it's not a url", NONE),
                Arguments.of("http://sample.com", expectedUrls("http://sample.com")),
                Arguments.of("https://sample.com", expectedUrls("https://sample.com")),
                Arguments.of("At end:  http://sample.com", expectedUrls("http://sample.com")),
                Arguments.of(
                    "In middle http://sample.com of message",
                    expectedUrls("http://sample.com"),
                ),
                Arguments.of(
                    "http://sample.com beginning of message",
                    expectedUrls("http://sample.com"),
                ),
                Arguments.of("In parens (http://sample.com)", expectedUrls("http://sample.com")),
                Arguments.of("In brackets [http://sample.com]", expectedUrls("http://sample.com")),
                Arguments.of(
                    "In curly brackets {http://sample.com}",
                    expectedUrls("http://sample.com"),
                ),
                Arguments.of(
                    "Fat finger a space ( http://sample.com)",
                    expectedUrls("http://sample.com"),
                ),
                Arguments.of(
                    "Valid URL with parens http://msdn.microsoft.com/en-us/library/aa752574(VS.85).aspx",
                    expectedUrls("http://msdn.microsoft.com/en-us/library/aa752574(VS.85).aspx"),
                ),
                Arguments.of(
                    "extract url from punctuation markers {http://sample.com)",
                    expectedUrls("http://sample.com"),
                ),
                Arguments.of(
                    "extract url from punctuation markers {http://sample.com)",
                    expectedUrls("http://sample.com"),
                ),
                Arguments.of(
                    "extract url from punctuation markers {http://sample.com)",
                    expectedUrls("http://sample.com"),
                ),
                Arguments.of(
                    "extract url from punctuation markers {http://sample.com)",
                    expectedUrls("http://sample.com"),
                ),
            )

        @Throws(Exception::class)
        private fun expectedUrls(vararg strings: String): List<URL> {
            return strings.map { URI(it).toURL() }
        }
    }
}
