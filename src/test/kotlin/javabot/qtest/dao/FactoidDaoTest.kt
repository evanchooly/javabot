package javabot.qtest.dao

import io.quarkus.test.junit.QuarkusTest
import javabot.dao.BaseServiceTest
import javabot.dao.FactoidDao
import javabot.model.Factoid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@QuarkusTest
class FactoidDaoTest : BaseServiceTest() {
    private val factoidDao: FactoidDao by lazy { injector.getInstance(FactoidDao::class.java) }

    @Test
    @Tag("operations")
    fun testInsertfactoid() {
        factoidDao.addFactoid("joed2", "test2", "##javabot", LogsDaoTest.CHANNEL_NAME)
        assertTrue(factoidDao.hasFactoid("test2"))
        assertEquals(1, factoidDao.countFiltered(Factoid.of("test2")))
        assertEquals(1, factoidDao.countFiltered(Factoid.of("test2 ")))
        assertEquals(1, factoidDao.countFiltered(Factoid.of(" test2 ")))
        assertEquals(1, factoidDao.countFiltered(Factoid.of(" test2")))
        assertEquals(0, factoidDao.countFiltered(Factoid.of("test 2")))

        factoidDao.delete("joed2", "test2", LogsDaoTest.CHANNEL_NAME)
        // Assert.assertFalse(factoidDao.hasFactoid("test2"));
    }

    @Test
    @Tag("operations")
    fun countFactoids() {
        val key = "test factoid"
        val value = "test value"
        val count = factoidDao.count()
        factoidDao.addFactoid("cheeser", key, value, LogsDaoTest.CHANNEL_NAME)
        val count2 = factoidDao.count()
        assertNotSame(count2, count, "Not the same")
        factoidDao.delete("cheeser", key, LogsDaoTest.CHANNEL_NAME)
    }

    @Test
    fun testLastUsed() {
        factoidDao.delete("cheeser", "testing last used", LogsDaoTest.CHANNEL_NAME)
        val factoid =
            factoidDao.addFactoid("cheeser", "testing last used", "'sup?", LogsDaoTest.CHANNEL_NAME)
        assertNotNull(factoid.lastUsed, "Should have recorded a date")
        val factoid1 = factoidDao.getFactoid("testing last used")
        assertNotSame(factoid1?.lastUsed, factoid.lastUsed, "Should have a new lastUsed value")
    }
}
