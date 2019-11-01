package javabot.operations

import javabot.BaseTest
import javabot.Message
import javabot.operations.urlcontent.URLContentAnalyzer
import javabot.operations.urlcontent.URLFromMessageParser
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import javax.inject.Inject

@Test(groups = ["operations"])
class URLTitleOperationTest : BaseTest() {
    @Inject
    private lateinit var operation: URLTitleOperation
    private val analyzer = URLContentAnalyzer()

    @Test(dataProvider = "urls")
    fun testSimpleUrl(url: String, content: String?) {
        val results = operation.handleChannelMessage(Message(TEST_CHANNEL, TEST_USER, url))
        if (content != null) {
            assertTrue(results.isNotEmpty())
            assertEquals(results[0].value, content)
        } else {
            assertTrue(results.isEmpty(), "Results for '$url' should be empty: $results")
        }
    }

    @Test(dataProvider = "urlRulesCheck")
    fun testFuzzyContent(url: String, title: String?, pass: Boolean) {
        assertEquals(analyzer.check(url, title), pass)
    }

    @DataProvider(name = "urls")
    fun getUrls(): Array<Array<*>> {
        return arrayOf(
                // this would be nice to fix, but we need to figure out how bloomberg is detecting the bot
                arrayOf("https://www.bloomberg.com/graphics/2019-android-global-smartphone-growth/", "botuser's title: \"Bloomberg - Are you a robot?\""),
                arrayOf("https://www.youtube.com/watch?v=LD4kSBpxatM", "botuser's title: \"Jimi Hendrix - Hey Joe (live)\""),
                arrayOf("https://www.youtube.com/watch?v=LDtM", "botuser's title: \"Jimi Hendrix - Hey Joe (live)\""),
                arrayOf("http://google.com/", null),
                arrayOf("http://google.com", null),
                arrayOf("http://localhost", null),
                arrayOf("http://127.0.0.1", null),
                arrayOf("http://a", null), // should work unless DNS gets in the way, as it does for me
                arrayOf("Have you tried to http://google.com", null),
                arrayOf("http://varietyofsound.wordpress.com has a lot of VSTs", null),
                arrayOf("Have you tried to http://javachannel.org/", "botuser's title: \"Freenode ##java  enthusiasts united\""),
                arrayOf("http://javachannel.org/posts/finding-hash-collisions-in-java-strings/", null),
                arrayOf("http://hastebin.com/askhjahs", null),
                arrayOf("http://pastebin.com/askhjahs", null),
                arrayOf("http://architects.dzone.com/articles/why-programmers-should-have",
                        "botuser's title: \"Why Programmers Should Have a Blog - DZone Agile\""), // url matches title
                arrayOf("http://facebook.com/foo/bar/blah", null), // doesn't exist on facebook, I hope
                arrayOf("http://", null),
                arrayOf("http://docs.oracle.com/javaee/6/tutorial/doc/", "botuser's title: \"- The Java EE 6 Tutorial\""),
                arrayOf("https://docs.oracle.com/javaee/7/api/javax/enterprise/inject/Instance.html", null),
                arrayOf("http://docs.oracle.com/javase/tutorial/java/nutsandbolts/branch.html",
                        "botuser's title: \"Branching Statements (The Java Tutorials > Learning the Java Language > Language Basics)\""),
                arrayOf("http://git.io/foo", null),
                arrayOf("Two urls with titles: http://docs.oracle.com/javaee/6/tutorial/doc/ and http://javachannel.org/",
                        "botuser's titles: \"- The Java EE 6 Tutorial\" | \"Freenode ##java  enthusiasts united\""),
                arrayOf("Two urls, one with a title: http://javachannel.org/posts/finding-hash-collisions-in-java-strings/  and " +
                        "http://javachannel.org/", "botuser's title: \"Freenode ##java  enthusiasts united\""),
                arrayOf("Two urls, duplicated:  http://javachannel.org/ and http://javachannel.org/"
                        , "botuser's title: \"Freenode ##java  enthusiasts united\""),
                arrayOf("https://twitter.com/djspiewak/status/1004038775989678080", "botuser's title: \"Daniel Spiewak on Twitter: \"Random best practice note: just because your language has type inference doesn't mean it's bad to explicitly write types. Types are good! Types are documentation. Don't make future code reviewers play the human c...\"\""),
                arrayOf("http://refheap.com", null), // caught by blacklist
                arrayOf("https://imagebin/ca", null), // caught by blacklist
                arrayOf("https://imgur.com", null), // caught by blacklist
                arrayOf("https://twitter.com/OpenJDK/status/1040259287556259842", "botuser's title: \"OpenJDK on Twitter: \"http://openjdk.java.net and some of its subdomains (cr, hg, and mail), along with http://jdk.java.net , are unreachable via http. We're working to fix the problem as quickly as possible.\"\""),
                arrayOf("Ignore title if it doesn't contain at least 20 ascii chars https://www.baidu.com/", null),
                arrayOf("https://twitter.com/DonaldOJDK/status/1045791557901643777", "botuser's title: \"DonaldOJDK on Twitter: \"So simply put, yes there should be javadoc on http://Jdk.java.net , its coming soon (tm).\"\""),
                arrayOf("https://twitter.com/TheOnion/status/1080887787266674690", "botuser's title: \"The Onion on Twitter: \"Meghan McCain Forced To Live Out Socialist Nightmare Of Empathy For Sick Person https://trib.al/6EXfUh5 #OurAnnualYear2018 pic.twitter.com/Qyg7DUpqre\"\""),
                arrayOf("http://thepasteb.in/foo", null) // caught by blacklist; note that this site is shuttered
        )
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

    @Test
    fun testBlacklist() {
        // should contain AT LEAST refheap.com or else other tests don't pass
        assertTrue(URLFromMessageParser.blacklistHosts.contains("refheap.com"))
    }

    @Test
    fun testLongContentRestriction() {
        val url = "https://m.facebook.com/story.php?story_fbid=2097200947043549&id=516762978420695"
        val results = operation.handleChannelMessage(Message(TEST_CHANNEL, TEST_USER, url))
        assertEquals(results.size, 1)
        println(results[0].value.length)
        assertTrue(results[0].value.length<301)
    }
}