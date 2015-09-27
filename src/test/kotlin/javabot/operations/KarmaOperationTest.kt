package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.BaseMessagingTest
import javabot.dao.ConfigDao
import javabot.dao.KarmaDao
import javabot.model.ThrottleItem
import org.mongodb.morphia.Datastore
import org.testng.Assert
import org.testng.annotations.Test
import java.lang.String.format
import java.util.Date
import javax.inject.Inject

@Test(groups = arrayOf("operations"))
public class KarmaOperationTest : BaseMessagingTest() {
    @Inject
    lateinit val karmaDao: KarmaDao

    @Inject
    lateinit val ds: Datastore

    @Inject
    lateinit val configDao: ConfigDao

    @Throws(InterruptedException::class)
    public fun updateKarma() {
        val config = configDao.get()
        val throttleThreshold = config.throttleThreshold
        try {
            ds.delete(ds.createQuery(ThrottleItem::class.java))
            val karma = karmaDao.find("testjavabot")
            var value = if (karma != null) karma.value else 0
            val bob = registerIrcUser("bob", "bob", "localhost")
            for (i in 0..throttleThreshold - 1) {
                testMessageAs(bob, "~testjavabot++", Sofia.karmaOthersValue("testjavabot", ++value, bob.nick))
            }
            testMessageAs(bob, "~testjavabot++", Sofia.throttledUser())
            testMessageAs(bob, "~testjavabot--", Sofia.throttledUser())
            testMessageAs(bob, "~testjavabot--", Sofia.throttledUser())
            testMessageAs(bob, "~testjavabot--", Sofia.throttledUser())

            ds.delete(ds.createQuery(ThrottleItem::class.java))

            testMessageAs(bob, "~testjavabot++", Sofia.karmaOthersValue("testjavabot", ++value, bob.nick))
        } finally {
            config.throttleThreshold = throttleThreshold
            configDao.save(config)
        }
    }

    public fun noncontiguousNameReadKarma() {
        val target = "foo " + Date().time
        testMessage(format("~karma %s", target), format("%s has no karma, %s", target, testUser))
    }

    public fun noncontiguousNameAddKarma() {
        val target = "foo " + Date().time
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) + 1
        testMessage(format("~%s ++", target), Sofia.karmaOthersValue(target, karma, testUser.nick))
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
        karmaDao.delete(karmaDao.find(target)?.id)
    }

    public fun karmaLooksLikeParam() {
        val target = "foo " + Date().time
        testMessage(format("~%s--bar=as", target), Sofia.unhandledMessage(testUser.nick))
    }

    public fun karmaLooksLikeParamShort() {
        testMessage("~--bar=as", Sofia.unhandledMessage(testUser.nick))
        testMessage("~ --bar=af", Sofia.unhandledMessage(testUser.nick))
    }

    public fun noncontiguousNameAddKarmaTrailingSpace() {
        val target = "foo " + Date().time
        val karma = getKarma(target) + 1
        testMessage(format("~%s ++", target), Sofia.karmaOthersValue(target, karma, testUser.nick))
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
        karmaDao.delete(karmaDao.find(target)?.id)
    }

    public fun noncontiguousNameAddKarmaWithComment() {
        val target = "foo " + Date().time
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) + 1
        testMessage(format("~%s++ hey coolio", target), Sofia.karmaOthersValue(target, karma, testUser.nick))
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
        karmaDao.delete(karmaDao.find(target)?.id)
    }

    public fun shortNameAddKarma() {
        val target = "a" // shortest possible name
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) + 1
        testMessage(format("~%s++", target), Sofia.karmaOthersValue(target, karma, testUser.nick))
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
        karmaDao.delete(karmaDao.find(target)?.id)
    }

    public fun noNameAddKarma() {
        val target = "" // no name
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) + 1
        testMessage(format("~%s++", target), Sofia.unhandledMessage(testUser.nick))
        Assert.assertFalse(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
    }

    public fun noNameSubKarma() {
        val target = "" // no name
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) - 1
        testMessage(format("~%s--", target), Sofia.unhandledMessage(testUser.nick))
        Assert.assertFalse(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
    }

    public fun logNew() {
        val target =  "${Date().time}"
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) + 1
        testMessage(format("~%s++", target), Sofia.karmaOthersValue(target, karma, testUser.nick))
        Assert.assertTrue(changeDao.findLog(Sofia.karmaChanged(testUser.nick, target, karma)))
        karmaDao.delete(karmaDao.find(target)?.id)
    }

    public fun logChanged() {
        val target = "javabot"
        val karma = getKarma(userFactory.createUser(target, target, "localhost").nick) + 1
        testMessage(format("~%s++", target), Sofia.karmaOthersValue(target, karma, testUser.nick))
    }

    public fun changeOwnKarma() {
        val karma = getKarma(testUser.nick)
        testMessage(format("~%s++", testUser), "You can't increment your own karma.",
              format("%s, you have a karma level of %d", testUser, karma - 1))
        val karma2 = getKarma(testUser.nick)
        Assert.assertTrue(karma2 == karma - 1, "Should have lost one karma point.")
    }

    public fun queryOwnKarma() {
        val bill = registerIrcUser("bill", "bill", "localhost")
        val karma = getKarma(bill.nick)
        Assert.assertEquals(karma, 0)
        testMessageAs(bill, "~karma bill", Sofia.karmaOwnNone("bill"))
    }

    public fun karmaChangeWithComments() {
        val target = "L-----D"
        try {
            val messages = sendMessage(format("~~%s google java embedded nosql", target))
            Assert.assertFalse(messages.get(0).contains("has a karma level"), format("Should not have gotten a karma message: %s",
                  messages.get(0)))
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
