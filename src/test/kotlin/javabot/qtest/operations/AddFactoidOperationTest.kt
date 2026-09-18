package javabot.qtest.operations

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import java.util.stream.Stream
import javabot.BaseTest
import javabot.Message
import javabot.dao.FactoidDao
import javabot.model.JavabotUser
import javabot.operations.AddFactoidOperation
import javabot.operations.ForgetFactoidOperation
import javabot.operations.GetFactoidOperation
import javabot.qtest.dao.LogsDaoTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@QuarkusTest
@TestMethodOrder(OrderAnnotation::class)
class AddFactoidOperationTest : BaseTest() {

    // FactoidDao/AddFactoidOperation/GetFactoidOperation/ForgetFactoidOperation are Guice-domain
    // (javabot.dao.**/javabot.operations.** are excluded from CDI) -- not real @Inject sites
    // under @QuarkusTest.
    private val factoidDao: FactoidDao by lazy { injector.getInstance(FactoidDao::class.java) }
    private val addFactoidOperation: AddFactoidOperation by lazy {
        injector.getInstance(AddFactoidOperation::class.java)
    }
    private val getFactoidOperation: GetFactoidOperation by lazy {
        injector.getInstance(GetFactoidOperation::class.java)
    }
    private val forgetFactoidOperation: ForgetFactoidOperation by lazy {
        injector.getInstance(ForgetFactoidOperation::class.java)
    }

    companion object {
        val OK: String = Sofia.ok(TEST_USER_NICK.take(16))

        val TEST_NON_ADMIN_USER_NICK = "nonadminuser"
        val TEST_NON_ADMIN_USER =
            JavabotUser(TEST_NON_ADMIN_USER_NICK, TEST_NON_ADMIN_USER_NICK, "hostmask")

        private var factoidAddSucceeded = false

        @JvmStatic
        fun replaceInput(): Stream<Arguments> =
            Stream.of(
                Arguments.of("no"),
                Arguments.of("No"),
                Arguments.of("nO"),
                Arguments.of("NO"), // let's be emphatic!
            )
    }

    @BeforeEach
    fun setUp() {
        factoidDao.delete(TEST_TARGET_NICK, "test", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "ping $1", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "what", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "what up", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "test pong", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "asdf", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "12345", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete(TEST_TARGET_NICK, "replace", LogsDaoTest.CHANNEL_NAME)
    }

    @Test
    @Order(1)
    fun factoidAdd() {
        var response = addFactoidOperation.handleMessage(message("~test pong is pong"))
        assertEquals(OK, response[0].value)
        response =
            addFactoidOperation.handleMessage(
                message(
                    "~ping \$1 is <action>sends some radar to \$1, awaits a" +
                        " response then forgets how long it took"
                )
            )
        assertEquals(OK, response[0].value)

        response = addFactoidOperation.handleMessage(message("~what? is a question"))
        assertEquals(OK, response[0].value)
        response = addFactoidOperation.handleMessage(message("~what up? is <see>what?"))
        assertEquals(OK, response[0].value)
        factoidAddSucceeded = true
    }

    @ParameterizedTest
    @MethodSource("replaceInput")
    fun replace(text: String) {
        var response = addFactoidOperation.handleMessage(message("~forget replace"))
        // we want to make sure that the factoid doesn't exist before anything else
        assertEquals(0, response.size)

        response = addFactoidOperation.handleMessage(message("~replace is first entry"))
        assertEquals(OK, response[0].value)
        var factoid = factoidDao.getFactoid("replace")!!

        val updated = factoid.updated
        assertEquals(TEST_USER.nick, factoid.userName)

        response =
            addFactoidOperation.handleMessage(
                message("~$text, replace is <reply>second entry", user = TEST_NON_ADMIN_USER)
            )
        assertEquals(Sofia.ok(TEST_NON_ADMIN_USER.nick), response[0].value)

        factoid = factoidDao.getFactoid("replace")!!
        assertTrue(factoid.updated.isAfter(updated))
        assertEquals(TEST_NON_ADMIN_USER.nick, factoid.userName)

        response = getFactoidOperation.handleMessage(message("~replace"))
        assertEquals("second entry", response[0].value)

        response = forgetFactoidOperation.handleMessage(message("~forget replace"))
        assertEquals(Sofia.factoidForgotten("replace", TEST_USER.nick), response[0].value)

        response =
            addFactoidOperation.handleMessage(message("~$text, replace is <reply>second entry"))
        assertEquals(Sofia.factoidUnknown("replace"), response[0].value)
    }

    @Test
    fun rejectDuplicate() {
        // clear out factoid first
        forgetFactoidOperation.handleMessage(message("~forget epesh", user = TEST_NON_ADMIN_USER))

        var response =
            addFactoidOperation.handleMessage(message("~epesh is cool", user = TEST_NON_ADMIN_USER))
        assertEquals(1, response.size)
        assertEquals("OK, ${TEST_NON_ADMIN_USER.nick}.", response[0].value)
        val updated = factoidDao.getFactoid("epesh")!!.updated

        response =
            addFactoidOperation.handleMessage(
                message("~epesh is awesome", user = TEST_NON_ADMIN_USER)
            )
        assertEquals(1, response.size)
        assertEquals(Sofia.factoidExists("epesh", TEST_NON_ADMIN_USER.nick), response[0].value)
        assertFalse(factoidDao.getFactoid("epesh")!!.updated.isAfter(updated))

        response = getFactoidOperation.handleMessage(message("~epesh", user = TEST_NON_ADMIN_USER))
        assertEquals(1, response.size)
        assertEquals("${TEST_NON_ADMIN_USER.nick}, epesh is cool", response[0].value)

        response =
            forgetFactoidOperation.handleMessage(
                message("~forget epesh", user = TEST_NON_ADMIN_USER)
            )
        assertEquals(1, response.size)
        assertEquals(Sofia.factoidForgotten("epesh", TEST_NON_ADMIN_USER.nick), response[0].value)
    }

    @Test
    @Order(2)
    fun duplicateAdd() {
        assumeTrue(factoidAddSucceeded, "factoidAdd must pass first")
        val message = "~test pong is pong"
        var response = addFactoidOperation.handleMessage(message(message))
        assertEquals(OK, response[0].value)
        response = addFactoidOperation.handleMessage(message(message))
        assertEquals(Sofia.factoidExists("test pong", TEST_USER.nick), response[0].value)
        forgetFactoidOperation.handleMessage(message("~forget test pong"))
    }

    @Test
    fun blankValue() {
        val response = addFactoidOperation.handleMessage(message("~pong is"))
        assertEquals(0, response.size)
    }

    @Test
    fun addLog() {
        val response = addFactoidOperation.handleMessage(message("~12345 is 12345"))
        assertEquals(OK, response[0].value)
        assertTrue(
            changeDao.findLog(
                Sofia.factoidAdded(TEST_USER.nick, "12345", "12345", TEST_CHANNEL.name)
            )
        )
        forgetFactoidOperation.handleMessage(message("~forget 12345"))
    }

    @Test
    fun parensFactoids() {
        val factoid = "should be the full (/hi there) factoid"
        var response = addFactoidOperation.handleMessage(message("~asdf is <reply>$factoid"))
        assertEquals(OK, response[0].value)
        response = getFactoidOperation.handleMessage(message("~asdf"))
        assertEquals(factoid, response[0].value)
    }

    @Test
    fun privMessage() {
        bot.get()
            .processMessage(
                Message(TARGET_USER, System.currentTimeMillis().toString() + " is doh!")
            )
        assertEquals(Sofia.privmsgChange(), messages.get()[0])
    }
}
