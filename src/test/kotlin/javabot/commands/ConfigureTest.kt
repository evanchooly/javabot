package javabot.commands

import com.google.inject.Inject
import javabot.BaseTest
import javabot.dao.ConfigDao
import org.testng.Assert
import org.testng.annotations.Test
import java.lang.String.format


@Test
class ConfigureTest : BaseTest() {
    @Inject
    private lateinit var configDao: ConfigDao
    @Inject
    private lateinit var operation: Configure

    fun change() {
        val config = configDao.get()
        val throttleThreshold = config.throttleThreshold
        Assert.assertNotNull(throttleThreshold)

        var response = operation.handleMessage(message("~admin configure"))
        Assert.assertEquals(response[0].value, config.toString())

        response = operation.handleMessage(message("~admin configure --property=throttleThreshold --value=15"))
        Assert.assertEquals(response[0].value, format("Setting %s to %d", "throttleThreshold", 15))

        Assert.assertEquals(configDao.get().throttleThreshold, 15)

        response = operation.handleMessage(message("~admin configure --property=throttleThreshold --value=10"))
        Assert.assertEquals(response[0].value, format("Setting %s to %d", "throttleThreshold", 10))

        Assert.assertEquals(configDao.get().throttleThreshold, 10)
    }
}