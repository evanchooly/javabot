package javabot.commands

import com.google.inject.Inject
import javabot.BaseMessagingTest
import javabot.dao.ConfigDao
import org.testng.Assert
import org.testng.annotations.Test
import java.lang.String.format


@Test
public class ConfigureTest : BaseMessagingTest() {
    @Inject
    lateinit var configDao: ConfigDao

    public fun change() {
        val config = configDao.get()
        val throttleThreshold = config.throttleThreshold
        Assert.assertNotNull(throttleThreshold)

        testMessage("~admin configure", config.toString())

        testMessage("~admin configure --property=throttleThreshold --value=15", format("Setting %s to %d", "throttleThreshold", 15))

        Assert.assertEquals(configDao.get().throttleThreshold, Integer(15))

        testMessage("~admin configure --property=throttleThreshold --value=10", format("Setting %s to %d", "throttleThreshold", 10))

        Assert.assertEquals(configDao.get().throttleThreshold, Integer(10))
    }
}