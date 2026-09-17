package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import java.lang.String.format
import java.util.Date
import java.util.stream.Stream
import javabot.BaseTest
import javabot.dao.KarmaDao
import javabot.dao.NickServDao
import javabot.dao.util.EntityNotFoundException
import javabot.operations.KarmaOperation
import javabot.registerIrcUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@QuarkusTest
@Tag("operations")
class KarmaOperationTest : BaseTest() {

    private val nickServDao: NickServDao by lazy { injector.getInstance(NickServDao::class.java) }
    private val karmaDao: KarmaDao by lazy { injector.getInstance(KarmaDao::class.java) }
    private val operation: KarmaOperation by lazy {
        injector.getInstance(KarmaOperation::class.java)
    }

    companion object {
        @JvmStatic
        fun karmaTestData(): Stream<Arguments> =
            Stream.of(
                Arguments.of("%s++", 1),
                Arguments.of("~%s++", 1),
                Arguments.of("%s++", 1),
                Arguments.of("~ %s ++", 1),
                Arguments.of("%s--", -1),
                Arguments.of("~%s--", -1),
                Arguments.of("%s: ++", 1),
                Arguments.of("~%s: ++", 1),
                Arguments.of("~ %s: ++", 1),
                Arguments.of("~ %s : ++", 1),
                Arguments.of("%s: --", -1),
                Arguments.of("~%s: --", -1),
            )
    }

    @ParameterizedTest
    @MethodSource("karmaTestData")
    fun karmaTests(command: String, expectedKarma: Int) {
        deleteKarma("foo")
        val message = message(command.format("foo"))
        operation.handleMessage(message)
        val karma = getKarma("foo")
        assertEquals(expectedKarma, karma)
        deleteKarma("foo")
    }

    @Test
    fun handleCPlusPlus1() {
        deleteKarma("C++")
        val message = message("C++++")
        val response = operation.handleMessage(message)
        println(response)
        val karma = getKarma("C++")
        assertEquals(1, karma)
        deleteKarma("C++")
    }

    @Test
    fun handleCPlusPlus2() {
        deleteKarma("C++")
        val message = message("C++:++")
        val response = operation.handleMessage(message)
        println(response)
        val karma = getKarma("C++")
        assertEquals(1, karma)
        deleteKarma("C++")
    }

    @Test
    fun noncontiguousNameReadKarma() {
        val target = "foo ${Date().time}"
        val response = operation.handleMessage(message("~karma ${target}"))
        assertEquals(format("%s has no karma, %s", target, TEST_USER), response[0].value)
    }

    @Test
    fun noncontiguousNameAddKarma() {
        val target = "foo ${Date().time}"
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target} ++"))
        assertEquals(Sofia.karmaOthersValue(target, karma, TEST_USER.nick), response[0].value)
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    @Test
    fun botNameWithKarmaWithAddress() {
        val target = TEST_BOT_NICK
        val karma = getKarma(target) + 1
        val event = message("${TEST_BOT_NICK}: ${target}++")
        val response = operation.handleMessage(event)
        assertEquals(Sofia.karmaOthersValue(target, karma, TEST_USER.nick), response[0].value)

        bot.get().processMessage(event)
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    @Test
    fun botNameWithKarma() {
        val target = TEST_BOT_NICK
        val karma = getKarma(target) + 1
        val event = message("${TEST_BOT_NICK}: ++")
        val response = operation.handleMessage(event)
        assertEquals(1, response.size)
        assertEquals(Sofia.karmaOthersValue(target, karma, TEST_USER.nick), response[0].value)

        bot.get().processMessage(event)
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    @Test
    fun botNameKarma() {
        val target = "foo"
        val karma = getKarma(target) + 1
        val event = message("${TEST_BOT_NICK}: ${target}++", TEST_BOT_NICK)
        val response = operation.handleMessage(event)
        assertEquals(Sofia.karmaOthersValue(target, karma, TEST_USER.nick), response[0].value)

        bot.get().processMessage(event)
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    @Test
    fun noncontiguousNameAddKarmaTrailingSpace() {
        val target = "foo ${Date().time}"
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target} ++"))
        assertEquals(Sofia.karmaOthersValue(target, karma, TEST_USER.nick), response[0].value)
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    @Test
    fun noncontiguousNameAddKarmaWithComment() {
        val target = "foo ${Date().time}"
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target}++ hey coolio"))
        assertEquals(Sofia.karmaOthersValue(target, karma, TEST_USER.nick), response[0].value)
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    @Test
    fun shortNameAddKarma() {
        val target = "a"
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target}++"))
        assertEquals(Sofia.karmaOthersValue(target, karma, TEST_USER.nick), response[0].value)
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    @Test
    fun noNameAddKarma() {
        val target = ""
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target}++"))
        assertEquals(0, response.size)
        assertFalse(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
    }

    @Test
    fun noNameSubKarma() {
        val target = ""
        val karma = getKarma(target) - 1
        val response = operation.handleMessage(message("~${target}--"))
        assertEquals(0, response.size)
        assertFalse(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
    }

    @Test
    fun logNew() {
        val target = "${Date().time}"
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target}++"))
        assertEquals(Sofia.karmaOthersValue(target, karma, TEST_USER.nick), response[0].value)
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    @Test
    fun logChanged() {
        val target = "javabot"
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target}++"))
        assertEquals(Sofia.karmaOthersValue(target, karma, TEST_USER.nick), response[0].value)
    }

    @Test
    fun changeOwnKarma() {
        val karma = getKarma(TEST_USER.nick)
        val response = operation.handleMessage(message("~${TEST_USER}++"))
        assertEquals("You can't increment your own karma.", response[0].value)
        assertEquals("${TEST_USER}, you have a karma level of ${karma - 1}", response[1].value)
        val karma2 = getKarma(TEST_USER.nick)
        assertTrue(karma2 == karma - 1, "Should have lost one karma point.")
    }

    @Test
    fun queryOwnKarma() {
        val bill = nickServDao.registerIrcUser("bill", "bill", "localhost")
        val karma = getKarma(bill.nick)
        assertEquals(0, karma)
        val response = operation.handleMessage(message("~karma bill", user = bill))
        assertEquals(Sofia.karmaOwnNone("bill"), response[0].value)
    }

    @Test
    fun karmaChangeWithComments() {
        val target = "L-----D"
        try {
            val response = operation.handleMessage(message("~target google java embedded nosql"))
            assertEquals(0, response.size, "Should not have gotten a karma message")
        } finally {
            val karma = karmaDao.find(target)
            if (karma != null) {
                karmaDao.delete(karma.id)
            }
        }
    }

    private fun deleteKarma(nick: String) {
        try {
            karmaDao.delete(karmaDao.find(nick)?.id)
        } catch (_: EntityNotFoundException) {
            // it's okay if the target isn't found. This is a cleanup operation.
        }
    }

    private fun getKarma(nick: String): Int {
        val karma = karmaDao.find(nick)
        return if (karma != null) karma.value else 0
    }
}
