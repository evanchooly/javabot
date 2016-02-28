package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.dao.ConfigDao
import javabot.dao.KarmaDao
import org.mongodb.morphia.Datastore
import org.testng.Assert
import org.testng.annotations.Test
import java.lang.String.format
import java.util.Date
import javax.inject.Inject

@Test(groups = arrayOf("operations")) class KarmaOperationTest : BaseTest() {
    @Inject
    protected lateinit var karmaDao: KarmaDao

    @Inject
    protected lateinit var ds: Datastore

    @Inject
    protected lateinit var configDao: ConfigDao
    @Inject
    protected lateinit var operation: KarmaOperation

    fun noncontiguousNameReadKarma() {
        val target = "foo ${Date().time}"
        var response = operation.handleMessage(message("karma ${target}"))
        Assert.assertEquals(response[0].value, format("%s has no karma, %s", target, testUser))
    }

    fun noncontiguousNameAddKarma() {
        val target = "foo ${Date().time}"
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) + 1
        var response = operation.handleMessage(message("${target} ++"))
                Assert.assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, testUser.nick))
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
        karmaDao.delete(karmaDao.find(target)?.id)
    }

    fun karmaLooksLikeParam() {
        val target = "foo ${Date().time}"
        var response = operation.handleMessage(message("${target}--bar=as"))
        Assert.assertEquals(response.size, 0)
    }

    fun karmaLooksLikeParamShort() {
        var response = operation.handleMessage(message("--bar=as"))
        Assert.assertEquals(response.size, 0)
        response = operation.handleMessage(message(" --bar=af"))
        Assert.assertEquals(response.size, 0)
    }

    fun noncontiguousNameAddKarmaTrailingSpace() {
        val target = "foo ${Date().time}"
        val karma = getKarma(target) + 1
        var response = operation.handleMessage(message("${target} ++"))
                Assert.assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, testUser.nick))
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
        karmaDao.delete(karmaDao.find(target)?.id)
    }

    fun noncontiguousNameAddKarmaWithComment() {
        val target = "foo ${Date().time}"
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) + 1
        var response = operation.handleMessage(message("${target}++ hey coolio"))
                Assert.assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, testUser.nick))
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
        karmaDao.delete(karmaDao.find(target)?.id)
    }

    fun shortNameAddKarma() {
        val target = "a" // shortest possible name
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) + 1
        var response = operation.handleMessage(message("${target}++"))
                Assert.assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, testUser.nick))
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
        karmaDao.delete(karmaDao.find(target)?.id)
    }

    fun noNameAddKarma() {
        val target = "" // no name
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) + 1
        var response = operation.handleMessage(message("${target}++"))
        Assert.assertEquals(response.size, 0)
        Assert.assertFalse(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
    }

    fun noNameSubKarma() {
        val target = "" // no name
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) - 1
        var response = operation.handleMessage(message("${target}--"))
        Assert.assertEquals(response.size, 0)
        Assert.assertFalse(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
    }

    fun logNew() {
        val target = "${Date().time}"
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) + 1
        var response = operation.handleMessage(message("${target}++"))
                Assert.assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, testUser.nick))
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
        karmaDao.delete(karmaDao.find(target)?.id)
    }

    fun logChanged() {
        val target = "javabot"
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) + 1
        var response = operation.handleMessage(message("${target}++"))
                Assert.assertEquals(response[0].value, Sofia.karmaOthersValue(target, karma, testUser.nick))
    }

    fun changeOwnKarma() {
        val karma = getKarma(testUser.nick)
        var response = operation.handleMessage(message("${testUser}++"))
        Assert.assertEquals(response[0].value, "You can't increment your own karma.")
        Assert.assertEquals(response[1].value, "${testUser}, you have a karma level of ${karma - 1}")
        val karma2 = getKarma(testUser.nick)
        Assert.assertTrue(karma2 == karma - 1, "Should have lost one karma point.")
    }

    fun queryOwnKarma() {
        val bill = registerIrcUser("bill", "bill", "localhost")
        val karma = getKarma(bill.nick)
        Assert.assertEquals(karma, 0)
        var response = operation.handleMessage(message("karma bill", bill))
        Assert.assertEquals(response[0].value, Sofia.karmaOwnNone("bill"))
    }

    fun karmaChangeWithComments() {
        val target = "L-----D"
        try {
            var response = operation.handleMessage(message("target google java embedded nosql"))
            Assert.assertEquals(response.size, 0, "Should not have gotten a karma message")
        } finally {
            val karma = karmaDao.find(target)
            if (karma != null) {
                karmaDao.delete(karma.id)
            }
        }
    }

    private fun getKarma(nick: String): Int {
        val karma = karmaDao.find(nick)
        return if (karma != null) karma.value else 0
    }
}
