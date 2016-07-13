package javabot.admin

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.Message
import javabot.dao.FactoidDao
import javabot.dao.LogsDaoTest
import javabot.dao.NickServDao
import javabot.operations.ForgetFactoidOperation
import javabot.registerIrcUser
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import javax.inject.Inject

@Test class LockFactoidTest @Inject constructor(val nickServDao: NickServDao, val factoidDao: FactoidDao,
                                                val forgetFactoid: ForgetFactoidOperation) : BaseTest() {

    @DataProvider(name = "factoids") fun names(): Array<Array<String>> {
        return arrayOf(arrayOf("lock me"), arrayOf("lockme"))
    }

    @Test(dataProvider = "factoids") fun lock(name: String) {
        try {
            factoidDao.delete(testUser.nick, name, LogsDaoTest.CHANNEL_NAME)
            var factoid = factoidDao.addFactoid(testUser.nick, name, "i should be locked", LogsDaoTest.CHANNEL_NAME)
            factoid.locked = true
            factoidDao.save(factoid)

            val bob = nickServDao.registerIrcUser("bob", "bob", "localhost")

            val message = Message(testChannel, bob, "forget ${name}")
            var response = forgetFactoid.handleMessage(message)
            Assert.assertEquals(response[0].value, Sofia.factoidDeleteLocked(bob.nick))

            factoid.locked = false
            factoidDao.save(factoid)

            response = forgetFactoid.handleMessage(message)
            Assert.assertEquals(response[0].value, Sofia.factoidForgotten(name, bob.nick))

            factoid = factoidDao.addFactoid(testUser.nick, name, "i should be locked", LogsDaoTest.CHANNEL_NAME)
            factoid.locked = true
            factoidDao.save(factoid)
            response = forgetFactoid.handleMessage(message("~forget ${name}"))
            Assert.assertEquals(response[0].value, Sofia.factoidForgotten(name, testUser.nick))

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            factoidDao.delete(testUser.nick, name, LogsDaoTest.CHANNEL_NAME)

        }
    }
}
