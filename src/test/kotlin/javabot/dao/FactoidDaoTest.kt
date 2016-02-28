package javabot.dao

import javabot.model.Factoid
import org.testng.Assert
import org.testng.annotations.Test
import javax.inject.Inject

class FactoidDaoTest : BaseServiceTest() {
    @Inject
    protected lateinit var factoidDao: FactoidDao

    @Test(groups = arrayOf("operations")) fun testInsertfactoid() {
        factoidDao.addFactoid("joed2", "test2", "##javabot")
        Assert.assertTrue(factoidDao.hasFactoid("test2"))
        factoidDao.delete("joed2", "test2")
        //Assert.assertFalse(factoidDao.hasFactoid("test2"));
    }

    @Test(groups = arrayOf("operations")) fun countFactoids() {
        val key = "test factoid"
        val value = "test value"
        val count = factoidDao.count()
        factoidDao.addFactoid("cheeser", key, value)
        val count2 = factoidDao.count()
        Assert.assertNotSame(count, count2, "Not the same")
        factoidDao.delete("cheeser", key)
    }

    @Test fun testLastUsed() {
        factoidDao.delete("cheeser", "testing last used")
        val factoid = factoidDao.addFactoid("cheeser", "testing last used", "'sup?")
        Assert.assertNotNull(factoid.lastUsed, "Should have recorded a date")
        val factoid1 = factoidDao.getFactoid("testing last used")
        Assert.assertNotSame(factoid.lastUsed, factoid1?.lastUsed, "Should have a new lastUsed value")
    }

    @Test
    fun changes() {
        val key = "test factoid"
        val value = "test value"

        factoidDao.delete("cheeser", key)

        val factoid = Factoid(key, value, "testng")
        factoidDao.save(factoid)

        var updated = factoid.updated
        var used = factoid.lastUsed
        Assert.assertEquals(factoid.updated, updated);
        Assert.assertEquals(factoid.lastUsed, used);

        factoidDao.save(factoid)
        Assert.assertNotEquals(factoid.updated, updated);
        Assert.assertTrue(factoid.updated.isAfter(updated));
        Assert.assertEquals(factoid.lastUsed, used);

        factoid.value = "new value"
        Assert.assertNotEquals(factoid.updated, updated);
        Assert.assertTrue(factoid.updated.isAfter(updated));
        Assert.assertEquals(factoid.lastUsed, used);
    }
}