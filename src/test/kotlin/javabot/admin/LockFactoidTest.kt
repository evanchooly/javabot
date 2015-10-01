package javabot.admin

import com.antwerkz.sofia.Sofia
import javabot.BaseMessagingTest
import org.pircbotx.User
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

@Test
public class LockFactoidTest : BaseMessagingTest() {
    @DataProvider(name = "factoids")
    public fun names(): Array<Array<String>> {
        return arrayOf(arrayOf("lock me"), arrayOf("lockme"))
    }

    @Test(dataProvider = "factoids")
    public fun lock(name: String) {
        try {
            sendMessage("~forget ${name}").get()
            testMessage("~${name} is i should be locked", Sofia.ok(testUser.nick))
            testMessage("~admin lockFactoid ${name}", "${name} locked.")

            val bob = registerIrcUser("bob", "bob", "localhost")
            testMessageAs(bob, "~forget ${name}", Sofia.factoidDeleteLocked(bob.nick))

            testMessage("~admin unlockFactoid ${name}", "${name} unlocked.")
            testMessageAs(bob, "~forget ${name}", Sofia.factoidForgotten(name, bob.nick))

            testMessage("~$name is i should be locked", Sofia.ok(testUser.nick))
            testMessage("~admin lockFactoid ${name}", "${name} locked.")
            testMessage("~forget ${name}", Sofia.factoidForgotten(name, testUser.nick))

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            sendMessage("~forget " + name).get()
        }
    }
}
