package javabot.qtest.operations

import io.quarkus.test.junit.QuarkusTest
import java.net.URI
import java.util.stream.Stream
import javabot.BaseTest
import javabot.Message
import javabot.operations.URLTitleOperation
import javabot.operations.urlcontent.URLContentAnalyzer
import javabot.operations.urlcontent.URLFromMessageParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@QuarkusTest
@Tag("operations")
class URLTitleOperationTest : BaseTest() {
    private val operation: URLTitleOperation by lazy {
        injector.getInstance(URLTitleOperation::class.java)
    }

    private val analyzer = URLContentAnalyzer()

    companion object {
        @JvmStatic
        fun longUrls(): Stream<Arguments> =
            Stream.of(
                Arguments.of("https://twitter.com", "twitter.com"),
                Arguments.of("http://www.twitter.com", "twitter.com"),
                Arguments.of("http://x.com", "x.com"),
                Arguments.of("https://foo.www.x.com", "x.com"),
                Arguments.of("https://twitter", "twitter"), // ew
            )

        @JvmStatic
        fun urls(): Stream<Arguments> =
            Stream.of(
                Arguments.of("https://www.youtube.com/watch?v=LDtM", null),
                Arguments.of("http://google.com/", null),
                Arguments.of("http://google.com", null),
                Arguments.of("http://localhost", null),
                Arguments.of("http://127.0.0.1", null),
                Arguments.of(
                    "http://a",
                    null,
                ), // should work unless DNS gets in the way, as it does for me
                Arguments.of("Have you tried to http://google.com", null),
                Arguments.of("http://varietyofsound.wordpress.com has a lot of VSTs", null),
                Arguments.of(
                    "Have you tried to http://javachannel.org/",
                    "botuser's title: \"Libera #java – enthusiasts united\"",
                ),
                Arguments.of(
                    "http://javachannel.org/posts/finding-hash-collisions-in-java-strings/",
                    null,
                ),
                Arguments.of("http://hastebin.com/askhjahs", null),
                Arguments.of("http://pastebin.com/askhjahs", null),
                Arguments.of(
                    "http://facebook.com/foo/bar/blah",
                    null,
                ), // doesn't exist on facebook, I hope
                Arguments.of("http://", null),
                Arguments.of(
                    "http://docs.oracle.com/javaee/6/tutorial/doc/",
                    "botuser's title: \"- The Java EE 6 Tutorial\"",
                ),
                Arguments.of(
                    "https://docs.oracle.com/javaee/7/api/javax/enterprise/inject/Instance.html",
                    null,
                ),
                Arguments.of(
                    "http://docs.oracle.com/javase/tutorial/java/nutsandbolts/branch.html",
                    "botuser's title: \"Branching Statements (The Java™ Tutorials > Learning the Java Language > Language Basics)\"",
                ),
                Arguments.of("http://git.io/foo", null),
                Arguments.of(
                    "Two urls with titles: http://docs.oracle.com/javaee/6/tutorial/doc/ and http://javachannel.org/",
                    "botuser's titles: \"- The Java EE 6 Tutorial\" | \"Libera #java – enthusiasts united\"",
                ),
                Arguments.of(
                    "Two urls, one with a title: http://javachannel.org/posts/finding-hash-collisions-in-java-strings/  and " +
                        "http://javachannel.org/",
                    "botuser's title: \"Libera #java – enthusiasts united\"",
                ),
                Arguments.of(
                    "Two urls, duplicated:  http://javachannel.org/ and http://javachannel.org/",
                    "botuser's title: \"Libera #java – enthusiasts united\"",
                ),
                Arguments.of(
                    "Two urls, effectively the same:  https://javachannel.org/ and http://javachannel.org/",
                    "botuser's title: \"Libera #java – enthusiasts united\"",
                ),
                Arguments.of(
                    "https://twitter.com/djspiewak/status/1004038775989678080",
                    "botuser's title: \"Daniel Spiewak on Twitter: \"Random best practice note: just because your language has type inference doesn't mean it's bad to explicitly write types. Types are good! Types are documentation. Don't make future code reviewers play the human ...\"",
                ),
                Arguments.of("http://refheap.com", null), // caught by blacklist
                Arguments.of("https://imagebin/ca", null), // caught by blacklist
                Arguments.of("https://imgur.com", null), // caught by blacklist
                Arguments.of(
                    "https://twitter.com/OpenJDK/status/1040259287556259842",
                    "botuser's title: \"OpenJDK on Twitter: \"http://openjdk.java.net and some of its subdomains (cr, hg, and mail), along with http://jdk.java.net, are unreachable via http. We're working to fix the problem as quickly as possible.\"\"",
                ),
                Arguments.of(
                    "Ignore title if it doesn't contain at least 20 ascii chars https://www.baidu.com/",
                    null,
                ),
                Arguments.of(
                    "https://twitter.com/DonaldOJDK/status/1045791557901643777",
                    "botuser's title: \"DonaldOJDK on Twitter: \"@jodastephen So simply put, yes there should be javadoc on http://Jdk.java.net, it’s coming soon (tm).\"\"",
                ),
                Arguments.of(
                    "https://twitter.com/TheOnion/status/1080887787266674690",
                    "botuser's title: \"The Onion on Twitter: \"Meghan McCain Forced To Live Out Socialist Nightmare Of Empathy For Sick Person https://trib.al/6EXfUh5 #OurAnnualYear2018 https://t.co/Qyg7DUpqre\"\"",
                ),
                Arguments.of(
                    "http://thepasteb.in/foo",
                    null,
                ), // caught by blacklist; note that this site is shuttered
                Arguments.of(
                    "https://hachyderm.io/@kinabalu/109367252866888149",
                    "botuser's title: \"Can I find some way to change this button from Toot! to literally anything else 😂\"",
                ),
            )

        @JvmStatic
        fun urlRulesCheck(): Stream<Arguments> =
            Stream.of(
                Arguments.of("http://pastebin.com", "pastebin for your wastebin", false),
                Arguments.of(
                    "http://makemoneyfast.com/super-profit",
                    "make money fast! super profit",
                    false,
                ),
                Arguments.of("http://varietyofsound.wordpress.com", "Variety Of Sound", false),
                Arguments.of(
                    "http://javachannel.com",
                    "Freenode ##java: for enthusiasts by enthusiasts",
                    true,
                ),
                Arguments.of(
                    "http://javachannel.com/exceptions",
                    "Freenode ##java: How to properly handle exceptions",
                    true,
                ),
                Arguments.of("http://foo.bar.com", "", false),
                Arguments.of("http://foo.bar.com", null, false),
            )
    }

    @ParameterizedTest
    @MethodSource("longUrls")
    fun testHostBasename(url: String, basenamed: String) {
        val host = URI(url).toURL().host
        assertEquals(basenamed.lowercase(), operation.hostBasename(host))
    }

    @ParameterizedTest
    @MethodSource("urls")
    fun testSimpleUrl(url: String, content: String?) {
        val results = operation.handleChannelMessage(Message(TEST_CHANNEL, TEST_USER, url))
        if (content != null) {
            if (!url.contains("twitter")) {
                assertTrue(
                    results.isNotEmpty(),
                    "testing $url  - results: '$results', expected content: $content",
                )
                assertEquals(content, results[0].value)
            }
        } else {
            assertTrue(results.isEmpty(), "Results for '$url' should be empty: $results")
        }
    }

    @ParameterizedTest
    @MethodSource("urlRulesCheck")
    fun testFuzzyContent(url: String, title: String?, pass: Boolean) {
        assertEquals(pass, analyzer.check(url, title))
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
        assertEquals(1, results.size)
        assertTrue(results[0].value.length < 301)
    }
}
