package javabot.admin

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.Message
import javabot.dao.FactoidDao
import javabot.operations.ForgetFactoidOperation
import org.testng.Assert
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import javax.inject.Inject

@Test class LockFactoidTest : BaseTest() {
    @Inject
    private lateinit var factoidDao: FactoidDao
    @Inject
    private lateinit var forgetFactoid: ForgetFactoidOperation

    @DataProvider(name = "factoids") fun names(): Array<Array<String>> {
        return arrayOf(arrayOf("lock me"), arrayOf("lockme"))
    }

    @Test(dataProvider = "factoids") fun lock(name: String) {
        try {
            factoidDao.delete(testUser.nick, name)
            var factoid = factoidDao.addFactoid(testUser.nick, name, "i should be locked")
            factoid.locked = true
            factoidDao.save(factoid)

            val bob = registerIrcUser("bob", "bob", "localhost")

            var message = Message(testChannel, bob, "forget ${name}")
            var response = forgetFactoid.handleMessage(message)
            Assert.assertEquals(response[0].value, Sofia.factoidDeleteLocked(bob.nick))

            factoid.locked = false
            factoidDao.save(factoid)

            response = forgetFactoid.handleMessage(message)
            Assert.assertEquals(response[0].value, Sofia.factoidForgotten(name, bob.nick))

            factoid = factoidDao.addFactoid(testUser.nick, name, "i should be locked")
            factoid.locked = true
            factoidDao.save(factoid)
            response = forgetFactoid.handleMessage(message("forget ${name}"))
            Assert.assertEquals(response[0].value, Sofia.factoidForgotten(name, testUser.nick))

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            factoidDao.delete(testUser.nick, name)

        }
    }
}
