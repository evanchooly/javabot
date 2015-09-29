package javabot.operations

import javabot.BaseMessagingTest
import javabot.operations.urlcontent.URLContentAnalyzer
import org.testng.Assert.assertEquals
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test(groups = arrayOf("operations"))
public class URLTitleOperationTest : BaseMessagingTest() {
    private var analyzer = URLContentAnalyzer()

    @DataProvider(name = "urls")
    fun getUrls(): Array<Array<Any?>> {
        return arrayOf(arrayOf<Any?>("http://google.com/", null),
              arrayOf<Any?>("http://google.com", null),
              arrayOf<Any?>("Have you tried to http://google.com", null),
              arrayOf<Any?>("http://varietyofsound.wordpress.com has a lot of VSTs", null),
              arrayOf<Any?>("Have you tried to http://javachannel.org/", "botuser's title: Freenode ##java  enthusiasts united"),
              arrayOf<Any?>("http://javachannel.org/posts/finding-hash-collisions-in-java-strings/", null),
              arrayOf<Any?>("http://hastebin.com/askhjahs", null),
              arrayOf<Any?>("http://pastebin.com/askhjahs", null),
              arrayOf<Any?>("https://www.facebook.com/thechurchofspace", null), // url matches content title too well
              arrayOf<Any?>("http://architects.dzone.com/articles/why-programmers-should-have", null), // url matches title
              arrayOf<Any?>("http://facebook.com/foo/bar/blah", null), // doesn't exist on facebook, I hope
              arrayOf<Any?>("http://", null),
              arrayOf<Any?>("http://docs.oracle.com/javaee/6/tutorial/doc/", "botuser's title: - The Java EE 6 Tutorial"),
              arrayOf<Any?>("https://docs.oracle.com/javaee/7/api/javax/enterprise/inject/Instance.html", null),
              arrayOf<Any?>("http://docs.oracle.com/javase/tutorial/java/nutsandbolts/branch.html",
                    "botuser's title: Branching Statements (The Java Tutorials > Learning the Java Language > Language Basics)"),
              arrayOf<Any?>("http://git.io/foo", null),
              arrayOf<Any?>("Two urls with titles: http://docs.oracle.com/javaee/6/tutorial/doc/ and http://javachannel.org/",
                    "botuser's titles: \"- The Java EE 6 Tutorial\" | \"Freenode ##java  enthusiasts united\""),
              arrayOf<Any?>( "Two urls, one with a title: http://javachannel.org/posts/finding-hash-collisions-in-java-strings/  and " +
                    "http://javachannel.org/",
              "botuser's title: Freenode ##java  enthusiasts united"))
    }

    @Test(dataProvider = "urls")
    public fun testSimpleUrl(url: String, content: String?) {
        if (content != null) {
            testMessage(url, content)
        } else {
            testMessage(url)
        }
    }

    @DataProvider(name = "urlRulesCheck")
    fun getUrlsForRulesCheck(): Array<Array<Any?>> {
        return arrayOf(arrayOf<Any?>("http://pastebin.com", "pastebin for your wastebin", false),
              arrayOf<Any?>("http://makemoneyfast.com/super-profit", "make money fast! super profit", false),
              arrayOf<Any?>("http://varietyofsound.wordpress.com", "Variety Of Sound", false),
              arrayOf<Any?>("http://javachannel.com", "Freenode ##java: for enthusiasts by enthusiasts", true),
              arrayOf<Any?>("http://javachannel.com/exceptions", "Freenode ##java: How to properly handle exceptions", true),
              arrayOf<Any?>("http://foo.bar.com", "", false),
              arrayOf<Any?>("http://foo.bar.com", null, false))
    }

    @Test(dataProvider = "urlRulesCheck")
    public fun testFuzzyContent(url: String, title: String?, pass: Boolean) {
        assertEquals(analyzer.check(url, title), pass)
    }

}
