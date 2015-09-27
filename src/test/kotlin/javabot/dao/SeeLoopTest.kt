package javabot.dao

import com.antwerkz.sofia.Sofia
import javabot.BaseMessagingTest
import org.testng.annotations.AfterTest
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test

import javax.inject.Inject

@Test
public class SeeLoopTest : BaseMessagingTest() {
    @Inject
    lateinit val factoidDao: FactoidDao

    public fun serial() {
        createCircularSee()
        createCircularSee2()
        createNormalSee()
        followReferencesCorrectly()
    }

    public fun createCircularSee() {
        deleteSees()
        testMessage("~see1 is <see>see2", ok)
        testMessage("~see2 is <see>see3", ok)
        testMessage("~see3 is <see>see1", ok)
        testMessage("~see1", Sofia.factoidLoop("<see>see2"))
        deleteSees()
    }

    @BeforeTest
    @AfterTest
    private fun deleteSees() {
        factoidDao.delete("test", "see1")
        factoidDao.delete("test", "see2")
        factoidDao.delete("test", "see3")
    }

    public fun createCircularSee2() {
        deleteSees()
        testMessage("~see1 is <see>see2", ok)
        testMessage("~see2 is <see>see3", ok)
        testMessage("~see3 is <see>see1", ok)
        testMessage("~see1", Sofia.factoidLoop("<see>see2"))
        deleteSees()
    }

    public fun followReferencesCorrectly() {
        deleteSees()
        testMessage("~see1 is Bzzt \$who", ok)
        testMessage("~see2 is <see>see1", ok)
        testMessage("~see3 is <see>see2", ok)
        testMessage("~see3", "${testUser}, see1 is Bzzt ${testUser}")
        deleteSees()
    }

    public fun createNormalSee() {
        deleteSees()
        testMessage("~see1 is <see>see2", ok)
        testMessage("~see2 is <see>see3", ok)
        testMessage("~see3 is w00t", ok)
        testMessage("~see1", "${testUser}, see3 is w00t")
        deleteSees()
    }
}