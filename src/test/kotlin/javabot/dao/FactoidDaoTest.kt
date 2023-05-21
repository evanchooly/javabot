package javabot.dao

import javabot.model.Factoid
import javax.inject.Inject
import org.testng.Assert
import org.testng.annotations.Test

class FactoidDaoTest : BaseServiceTest() {
    @Inject protected lateinit var factoidDao: FactoidDao

    @Test(groups = arrayOf("operations"))
    fun testInsertfactoid() {
        factoidDao.addFactoid("joed2", "test2", "##javabot", LogsDaoTest.CHANNEL_NAME)
        Assert.assertTrue(factoidDao.hasFactoid("test2"))
        Assert.assertEquals(factoidDao.countFiltered(Factoid.of("test2")), 1)
        Assert.assertEquals(factoidDao.countFiltered(Factoid.of("test2 ")), 1)
        Assert.assertEquals(factoidDao.countFiltered(Factoid.of(" test2 ")), 1)
        Assert.assertEquals(factoidDao.countFiltered(Factoid.of(" test2")), 1)
        Assert.assertEquals(factoidDao.countFiltered(Factoid.of("test 2")), 0)

        factoidDao.delete("joed2", "test2", LogsDaoTest.CHANNEL_NAME)
        // Assert.assertFalse(factoidDao.hasFactoid("test2"));
    }

    @Test(groups = arrayOf("operations"))
    fun countFactoids() {
        val key = "test factoid"
        val value = "test value"
        val count = factoidDao.count()
        factoidDao.addFactoid("cheeser", key, value, LogsDaoTest.CHANNEL_NAME)
        val count2 = factoidDao.count()
        Assert.assertNotSame(count, count2, "Not the same")
        factoidDao.delete("cheeser", key, LogsDaoTest.CHANNEL_NAME)
    }

    @Test
    fun testLastUsed() {
        factoidDao.delete("cheeser", "testing last used", LogsDaoTest.CHANNEL_NAME)
        val factoid =
            factoidDao.addFactoid("cheeser", "testing last used", "'sup?", LogsDaoTest.CHANNEL_NAME)
        Assert.assertNotNull(factoid.lastUsed, "Should have recorded a date")
        val factoid1 = factoidDao.getFactoid("testing last used")
        Assert.assertNotSame(
            factoid.lastUsed,
            factoid1?.lastUsed,
            "Should have a new lastUsed value"
        )
    }
}
