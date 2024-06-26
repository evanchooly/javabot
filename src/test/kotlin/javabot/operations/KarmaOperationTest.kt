package javabot.operations

import com.antwerkz.sofia.Sofia
import java.lang.String.format
import java.util.Date
import javabot.BaseTest
import javabot.dao.KarmaDao
import javabot.dao.NickServDao
import javabot.dao.util.EntityNotFoundException
import javabot.registerIrcUser
import javax.inject.Inject
import org.testng.Assert.assertEquals
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test(groups = arrayOf("operations"))
class KarmaOperationTest
@Inject
constructor(val nickServDao: NickServDao, val karmaDao: KarmaDao, val operation: KarmaOperation) :
    BaseTest() {

    @DataProvider
    fun karmaTestData() =
        arrayOf(
            arrayOf("%s++", 1),
            arrayOf("~%s++", 1),
            arrayOf("%s++", 1),
            arrayOf("~ %s ++", 1),
            arrayOf("%s--", -1),
            arrayOf("~%s--", -1),
            arrayOf("%s: ++", 1),
            arrayOf("~%s: ++", 1),
            arrayOf("~ %s: ++", 1),
            arrayOf("~ %s : ++", 1),
            arrayOf("%s: --", -1),
            arrayOf("~%s: --", -1)
        )

    @Test(dataProvider = "karmaTestData")
    fun karmaTests(command: String, expectedKarma: Int) {
        deleteKarma("foo")
        var message = message(command.format("foo"))
        var response = operation.handleMessage(message)
        var karma = getKarma("foo")
        assertEquals(expectedKarma, karma)
        deleteKarma("foo")
    }

    fun handleCPlusPlus1() {
        deleteKarma("C++")
        var message = message("C++++")
        var response = operation.handleMessage(message)
        println(response)
        var karma = getKarma("C++")
        assertEquals(1, karma)
        deleteKarma("C++")
    }

    fun handleCPlusPlus2() {
        deleteKarma("C++")
        var message = message("C++:++")
        var response = operation.handleMessage(message)
        println(response)
        var karma = getKarma("C++")
        assertEquals(1, karma)
        deleteKarma("C++")
    }

    fun noncontiguousNameReadKarma() {
        val target = "foo ${Date().time}"
        val response = operation.handleMessage(message("~karma ${target}"))
        assertEquals(response[0].value, format("%s has no karma, %s", target, TEST_USER))
    }

    fun noncontiguousNameAddKarma() {
        val target = "foo ${Date().time}"
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target} ++"))
        assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, TEST_USER.nick))
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    fun botNameWithKarmaWithAddress() {
        val target = TEST_BOT_NICK
        val karma = getKarma(target) + 1
        val event = message("${TEST_BOT_NICK}: ${target}++")
        val response = operation.handleMessage(event)
        assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, TEST_USER.nick))

        bot.get().processMessage(event)
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    fun botNameWithKarma() {
        val target = TEST_BOT_NICK
        val karma = getKarma(target) + 1
        val event = message("${TEST_BOT_NICK}: ++")
        val response = operation.handleMessage(event)
        assertEquals(1, response.size)
        assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, TEST_USER.nick))

        bot.get().processMessage(event)
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    fun botNameKarma() {
        val target = "foo"
        val karma = getKarma(target) + 1
        val event = message("${TEST_BOT_NICK}: ${target}++", TEST_BOT_NICK)
        val response = operation.handleMessage(event)
        assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, TEST_USER.nick))

        bot.get().processMessage(event)
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    fun noncontiguousNameAddKarmaTrailingSpace() {
        val target = "foo ${Date().time}"
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target} ++"))
        assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, TEST_USER.nick))
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    fun noncontiguousNameAddKarmaWithComment() {
        val target = "foo ${Date().time}"
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target}++ hey coolio"))
        assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, TEST_USER.nick))
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    fun shortNameAddKarma() {
        val target = "a" // shortest possible name
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target}++"))
        assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, TEST_USER.nick))
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    fun noNameAddKarma() {
        val target = "" // no name
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target}++"))
        assertEquals(response.size, 0)
        assertFalse(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
    }

    fun noNameSubKarma() {
        val target = "" // no name
        val karma = getKarma(target) - 1
        val response = operation.handleMessage(message("~${target}--"))
        assertEquals(response.size, 0)
        assertFalse(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
    }

    fun logNew() {
        val target = "${Date().time}"
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target}++"))
        assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, TEST_USER.nick))
        assertTrue(
            changeDao.findLog(Sofia.karmaChanged(TEST_USER.nick, target, karma, TEST_CHANNEL.name))
        )
        deleteKarma(target)
    }

    fun logChanged() {
        val target = "javabot"
        val karma = getKarma(target) + 1
        val response = operation.handleMessage(message("~${target}++"))
        assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, TEST_USER.nick))
    }

    fun changeOwnKarma() {
        val karma = getKarma(TEST_USER.nick)
        val response = operation.handleMessage(message("~${TEST_USER}++"))
        assertEquals(response[0].value, "You can't increment your own karma.")
        assertEquals(response[1].value, "${TEST_USER}, you have a karma level of ${karma - 1}")
        val karma2 = getKarma(TEST_USER.nick)
        assertTrue(karma2 == karma - 1, "Should have lost one karma point.")
    }

    fun queryOwnKarma() {
        val bill = nickServDao.registerIrcUser("bill", "bill", "localhost")
        val karma = getKarma(bill.nick)
        assertEquals(karma, 0)
        val response = operation.handleMessage(message("~karma bill", user = bill))
        assertEquals(response[0].value, Sofia.karmaOwnNone("bill"))
    }

    fun karmaChangeWithComments() {
        val target = "L-----D"
        try {
            val response = operation.handleMessage(message("~target google java embedded nosql"))
            assertEquals(response.size, 0, "Should not have gotten a karma message")
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
