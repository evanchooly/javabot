package javabot.admin

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import java.util.stream.Stream
import javabot.BaseTest
import javabot.Message
import javabot.dao.FactoidDao
import javabot.dao.NickServDao
import javabot.operations.ForgetFactoidOperation
import javabot.qtest.dao.LogsDaoTest
import javabot.registerIrcUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@QuarkusTest
class LockFactoidTest : BaseTest() {

    private val nickServDao: NickServDao by lazy { injector.getInstance(NickServDao::class.java) }
    private val factoidDao: FactoidDao by lazy { injector.getInstance(FactoidDao::class.java) }
    private val forgetFactoid: ForgetFactoidOperation by lazy {
        injector.getInstance(ForgetFactoidOperation::class.java)
    }

    companion object {
        @JvmStatic
        fun names(): Stream<Arguments> = Stream.of(Arguments.of("lock me"), Arguments.of("lockme"))
    }

    @ParameterizedTest
    @MethodSource("names")
    fun lock(name: String) {
        try {
            factoidDao.delete(TEST_USER.nick, name, LogsDaoTest.CHANNEL_NAME)
            var factoid =
                factoidDao.addFactoid(
                    TEST_USER.nick,
                    name,
                    "i should be locked",
                    LogsDaoTest.CHANNEL_NAME,
                )
            factoid.locked = true
            factoidDao.save(factoid)

            val bob = nickServDao.registerIrcUser("bob", "bob", "localhost")

            val message = Message(TEST_CHANNEL, bob, "forget ${name}")
            var response = forgetFactoid.handleMessage(message)
            assertEquals(Sofia.factoidDeleteLocked(bob.nick), response[0].value)

            factoid.locked = false
            factoidDao.save(factoid)

            response = forgetFactoid.handleMessage(message)
            assertEquals(Sofia.factoidForgotten(name, bob.nick), response[0].value)

            factoid =
                factoidDao.addFactoid(
                    TEST_USER.nick,
                    name,
                    "i should be locked",
                    LogsDaoTest.CHANNEL_NAME,
                )
            factoid.locked = true
            factoidDao.save(factoid)
            response = forgetFactoid.handleMessage(message("~forget ${name}"))
            assertEquals(Sofia.factoidForgotten(name, TEST_USER.nick), response[0].value)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            factoidDao.delete(TEST_USER.nick, name, LogsDaoTest.CHANNEL_NAME)
        }
    }
}
