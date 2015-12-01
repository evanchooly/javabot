package javabot.dao

import org.testng.Assert
import org.testng.annotations.Test
import javax.inject.Inject

public class FactoidDaoTest : BaseServiceTest() {
    @Inject
    protected lateinit var factoidDao: FactoidDao

    @Test(groups = arrayOf("operations"))
    public fun testInsertfactoid() {
        factoidDao.addFactoid("joed2", "test2", "##javabot")
        Assert.assertTrue(factoidDao.hasFactoid("test2"))
        factoidDao.delete("joed2", "test2")
        //Assert.assertFalse(factoidDao.hasFactoid("test2"));
    }

    @Test(groups = arrayOf("operations"))
    public fun countFactoids() {
        val key = "test factoid"
        val value = "test value"
        val count = factoidDao.count()
        factoidDao.addFactoid("cheeser", key, value)
        val count2 = factoidDao.count()
        Assert.assertNotSame(count, count2, "Not the same")
        factoidDao.delete("cheeser", key)
    }

    @Test
    public fun testLastUsed() {
        factoidDao.delete("cheeser", "testing last used")
        val factoid = factoidDao.addFactoid("cheeser", "testing last used", "'sup?")
        Assert.assertNotNull(factoid.lastUsed, "Should have recorded a date")
        val factoid1 = factoidDao.getFactoid("testing last used")
        Assert.assertNotSame(factoid.lastUsed, factoid1?.lastUsed, "Should have a new lastUsed value")
    }

}