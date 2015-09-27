package javabot.operations.urlcontent

import org.testng.Assert.assertEquals
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.net.URL
import java.util.ArrayList


public class URLFromMessageParserTest {

    var parser = URLFromMessageParser()

    @DataProvider(name = "messages")
    @Throws(Exception::class)
    fun getUrlsForParensBracketsStrip(): Array<Array<Any>> {
        return arrayOf(arrayOf("ftp://non.http.url.com", NONE), arrayOf("someone says http but it's not a url", NONE),
              arrayOf("someone says https but it's not a url", NONE), arrayOf("http://sample.com", expectedUrls("http://sample.com")),
              arrayOf("https://sample.com", expectedUrls("https://sample.com")),
              arrayOf("At end:  http://sample.com", expectedUrls("http://sample.com")),
              arrayOf("In middle http://sample.com of message", expectedUrls("http://sample.com")),
              arrayOf("http://sample.com beginning of message", expectedUrls("http://sample.com")),
              arrayOf("In parens (http://sample.com)", expectedUrls("http://sample.com")),
              arrayOf("In brackets [http://sample.com]", expectedUrls("http://sample.com")),
              arrayOf("In curly brackets {http://sample.com}", expectedUrls("http://sample.com")),
              arrayOf("Fat finger a space ( http://sample.com)", expectedUrls("http://sample.com")),
              arrayOf("Valid URL with parens http://msdn.microsoft.com/en-us/library/aa752574(VS.85).aspx",
                    expectedUrls("http://msdn.microsoft.com/en-us/library/aa752574(VS.85).aspx")),
              arrayOf("Don't fix mismatched punctuation  {http://sample.com)", expectedUrls("http://sample.com)")),
              arrayOf("Don't fix mismatched punctuation (http://sample.com]", expectedUrls("http://sample.com]")),
              arrayOf("Don't fix mismatched punctuation [http://sample.com}", expectedUrls("http://sample.com}")),
              arrayOf("Message has parens (like this) before http://sample.com)", expectedUrls("http://sample.com)")))
    }

    @Throws(Exception::class)
    private fun expectedUrls(vararg strings: String): List<URL> {
        val list = ArrayList<URL>()
        for (s in strings) {
            list.add(URL(s))
        }
        return list
    }

    @Test(dataProvider = "messages")
    public fun testUrlFromMessage(message: String, expected: List<URL>) {
        assertEquals(parser.urlsFromMessage(message), expected)
    }

    companion object {
        private val NONE = emptyList<URL>()
    }

}