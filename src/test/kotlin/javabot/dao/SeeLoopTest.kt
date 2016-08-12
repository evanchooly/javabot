package javabot.dao

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.operations.GetFactoidOperation
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import javax.inject.Inject

@Test class SeeLoopTest : BaseTest() {
    @Inject
    protected lateinit var factoidDao: FactoidDao
    @Inject
    protected lateinit var operation: GetFactoidOperation

    @BeforeMethod
    @AfterMethod
    private fun deleteSees() {
        factoidDao.delete("test", "see1", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete("test", "see2", LogsDaoTest.CHANNEL_NAME)
        factoidDao.delete("test", "see3", LogsDaoTest.CHANNEL_NAME)
    }

    fun createCircularSee() {
        factoidDao.addFactoid(TEST_USER.nick, "see1", "<see>see2", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "see2", "<see>see3", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "see3", "<see>see1", LogsDaoTest.CHANNEL_NAME)
        var response = operation.handleMessage(message("~see1"))
        Assert.assertEquals(response[0].value, Sofia.factoidLoop("<see>see2"))
    }

    fun followReferencesCorrectly() {
        factoidDao.addFactoid(TEST_USER.nick, "see1", "Bzzt \$who", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "see2", "<see>see1", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "see3", "<see>see2", LogsDaoTest.CHANNEL_NAME)
        var response = operation.handleMessage(message("~see3"))
        Assert.assertEquals(response[0].value, "${TEST_USER}, see1 is Bzzt ${TEST_USER}")
    }

    fun createNormalSee() {
        factoidDao.addFactoid(TEST_USER.nick, "see1", "<see>see2", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "see2", "<see>see3", LogsDaoTest.CHANNEL_NAME)
        factoidDao.addFactoid(TEST_USER.nick, "see3", "w00t", LogsDaoTest.CHANNEL_NAME)
        var response = operation.handleMessage(message("~see1"))
        Assert.assertEquals(response[0].value, "${TEST_USER}, see3 is w00t")
    }
}