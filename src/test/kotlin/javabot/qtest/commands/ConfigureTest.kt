package javabot.qtest.commands

import io.quarkus.test.junit.QuarkusTest
import java.lang.String.format
import javabot.BaseTest
import javabot.commands.Configure
import javabot.dao.ConfigDao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@QuarkusTest
class ConfigureTest : BaseTest() {
    private val configDao: ConfigDao by lazy { injector.getInstance(ConfigDao::class.java) }
    private val operation: Configure by lazy { injector.getInstance(Configure::class.java) }

    @Test
    fun change() {
        val config = configDao.get()
        val throttleThreshold = config.throttleThreshold
        assertNotNull(throttleThreshold)

        var response = operation.handleMessage(message("~admin configure"))
        assertEquals(config.toString(), response[0].value)

        response =
            operation.handleMessage(
                message("~admin configure --property=throttleThreshold --value=15")
            )
        assertEquals(format("Setting %s to %d", "throttleThreshold", 15), response[0].value)

        assertEquals(15, configDao.get().throttleThreshold)

        response =
            operation.handleMessage(
                message("~admin configure --property=throttleThreshold --value=10")
            )
        assertEquals(format("Setting %s to %d", "throttleThreshold", 10), response[0].value)

        assertEquals(10, configDao.get().throttleThreshold)
    }
}
