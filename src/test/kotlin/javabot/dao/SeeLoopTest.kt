package javabot.dao

import com.antwerkz.sofia.Sofia
import javabot.BaseTest
import javabot.operations.GetFactoidOperation
import org.testng.Assert
import org.testng.annotations.AfterTest
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import javax.inject.Inject

@Test
public class SeeLoopTest : BaseTest() {
    @Inject
    protected lateinit var factoidDao: FactoidDao
    @Inject
    protected lateinit var operation: GetFactoidOperation

    @BeforeTest
    @AfterTest
    private fun deleteSees() {
        factoidDao.delete("test", "see1")
        factoidDao.delete("test", "see2")
        factoidDao.delete("test", "see3")
    }

    public fun serial() {
        createCircularSee()
        createNormalSee()
        followReferencesCorrectly()
    }

    public fun createCircularSee() {
        factoidDao.addFactoid(testUser.nick, "see1", "<see>see2")
        factoidDao.addFactoid(testUser.nick, "see2", "<see>see3")
        factoidDao.addFactoid(testUser.nick, "see3", "<see>see1")
        var response = operation.handleMessage(message("see1"))
        Assert.assertEquals(response[0].value, Sofia.factoidLoop("<see>see2"))
    }

    public fun followReferencesCorrectly() {
        factoidDao.addFactoid(testUser.nick, "see1", "Bzzt \$who")
        factoidDao.addFactoid(testUser.nick, "see2", "<see>see1")
        factoidDao.addFactoid(testUser.nick, "see3", "<see>see2")
        var response = operation.handleMessage(message("see3"))
        Assert.assertEquals(response[0].value, "${testUser}, see1 is Bzzt ${testUser}")
    }

    public fun createNormalSee() {
        factoidDao.addFactoid(testUser.nick, "see1", "<see>see2")
        factoidDao.addFactoid(testUser.nick, "see2", "<see>see3")
        factoidDao.addFactoid(testUser.nick, "see3", "w00t")
        var response = operation.handleMessage(message("see1"))
        Assert.assertEquals(response[0].value, "${testUser}, see3 is w00t")
    }
}