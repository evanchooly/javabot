package javabot.qtest.dao

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import javabot.BaseTest
import javabot.dao.FactoidDao
import javabot.dao.LogsDaoTest
import javabot.operations.GetFactoidOperation
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
class SeeLoopTest : BaseTest() {
    private val factoidDao: FactoidDao by lazy { injector.getInstance(FactoidDao::class.java) }
    private val operation: GetFactoidOperation by lazy {
        injector.getInstance(GetFactoidOperation::class.java)
    }

    @BeforeEach
    @AfterEach
    fun deleteSees() {
        factoidDao.delete("test", "see1", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete("test", "see2", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete("test", "see3", LogsDaoTest.CHANNEL_NAME)
    }

    @Test
    fun createCircularSee() {
        factoidDao.addFactoid(TEST_USER.nick, "see1", "<see>see2", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "see2", "<see>see3", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "see3", "<see>see1", LogsDaoTest.CHANNEL_NAME)
        var response = operation.handleMessage(message("~see1"))
        assertEquals(Sofia.factoidLoop("<see>see2"), response[0].value)
    }

    @Test
    fun followReferencesCorrectly() {
        factoidDao.addFactoid(TEST_USER.nick, "see1", "Bzzt \$who", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "see2", "<see>see1", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "see3", "<see>see2", LogsDaoTest.CHANNEL_NAME)
        var response = operation.handleMessage(message("~see3"))
        assertEquals("${TEST_USER}, see1 is Bzzt ${TEST_USER}", response[0].value)
    }

    @Test
    fun createNormalSee() {
        factoidDao.addFactoid(TEST_USER.nick, "see1", "<see>see2", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "see2", "<see>see3", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "see3", "w00t", LogsDaoTest.CHANNEL_NAME)
        var response = operation.handleMessage(message("~see1"))
        assertEquals("${TEST_USER}, see3 is w00t", response[0].value)
    }
}
