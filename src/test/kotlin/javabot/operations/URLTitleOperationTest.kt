package javabot.operations

import javabot.BaseMessagingTest
import javabot.operations.urlcontent.URLContentAnalyzer
import org.testng.Assert.assertEquals
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test(groups = arrayOf("operations"))
public class URLTitleOperationTest : BaseMessagingTest() {
    private var analyzer = URLContentAnalyzer()

    @Test(dataProvider = "urls")
    public fun testSimpleUrl(url: String, content: String?) {
        if (content != null) {
            testMessage(url, content)
        } else {
            testMessage(url)
        }
    }

    @Test(dataProvider = "urlRulesCheck")
    public fun testFuzzyContent(url: String, title: String?, pass: Boolean) {
        assertEquals(analyzer.check(url, title), pass)
    }

    @DataProvider(name = "urls")
    fun getUrls(): Array<Array<*>>  {
        return arrayOf(
                arrayOf("http://google.com/", null),
                arrayOf("http://google.com", null),
                arrayOf("Have you tried to http://google.com", null),
                arrayOf("http://varietyofsound.wordpress.com has a lot of VSTs", null),
                arrayOf("Have you tried to http://javachannel.org/", "botuser's title: Freenode ##java  enthusiasts united"),
                arrayOf("http://javachannel.org/posts/finding-hash-collisions-in-java-strings/", null),
                arrayOf("http://hastebin.com/askhjahs", null),
                arrayOf("http://pastebin.com/askhjahs", null),
                arrayOf("https://www.facebook.com/thechurchofspace", null), // url matches content title too well
                arrayOf("http://architects.dzone.com/articles/why-programmers-should-have",
                        "botuser's title: Why Programmers Should Have a Blog - DZone Agile"), // url matches title
                arrayOf("http://facebook.com/foo/bar/blah", null), // doesn't exist on facebook, I hope
                arrayOf("http://", null),
                arrayOf("http://docs.oracle.com/javaee/6/tutorial/doc/", "botuser's title: - The Java EE 6 Tutorial"),
                arrayOf("https://docs.oracle.com/javaee/7/api/javax/enterprise/inject/Instance.html", null),
                arrayOf("http://docs.oracle.com/javase/tutorial/java/nutsandbolts/branch.html",
                        "botuser's title: Branching Statements (The Java Tutorials > Learning the Java Language > Language Basics)"),
                arrayOf("http://git.io/foo", null),
                arrayOf("Two urls with titles: http://docs.oracle.com/javaee/6/tutorial/doc/ and http://javachannel.org/",
                        "botuser's titles: \"- The Java EE 6 Tutorial\" | \"Freenode ##java  enthusiasts united\""),
                arrayOf("Two urls, one with a title: http://javachannel.org/posts/finding-hash-collisions-in-java-strings/  and " +
                        "http://javachannel.org/",
                        "botuser's title: Freenode ##java  enthusiasts united"))
    }

    @DataProvider(name = "urlRulesCheck")
    fun getUrlsForRulesCheck(): Array<Array<*>> {
        return arrayOf(arrayOf("http://pastebin.com", "pastebin for your wastebin", false),
              arrayOf("http://makemoneyfast.com/super-profit", "make money fast! super profit", false),
              arrayOf("http://varietyofsound.wordpress.com", "Variety Of Sound", false),
              arrayOf("http://javachannel.com", "Freenode ##java: for enthusiasts by enthusiasts", true),
              arrayOf("http://javachannel.com/exceptions", "Freenode ##java: How to properly handle exceptions", true),
              arrayOf("http://foo.bar.com", "", false),
              arrayOf("http://foo.bar.com", null, false))
    }

}
