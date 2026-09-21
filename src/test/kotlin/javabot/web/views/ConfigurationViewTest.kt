package javabot.web.views

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Arrays.asList
import javabot.dao.ConfigDao
import net.htmlparser.jericho.Source
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@QuarkusTest
class ConfigurationViewTest : ViewsTest() {
    @Inject private lateinit var configDao: ConfigDao

    @Test
    @Disabled
    fun configuration() {
        var config = configDao.get()
        config.operations = mutableListOf()
        configDao.save(config)

        var output = ByteArrayOutputStream()
        var templateInstance = templateService.createConfigurationView(mockSessionToken(false))
        var html = templateInstance.render()
        output.write(html.toByteArray())
        var source = Source(ByteArrayInputStream(output.toByteArray()))

        val operation = "Javadoc"
        var enable = source.getElementById("enable" + operation)
        assertNotNull(enable, source.toString())
        assertEquals("active", enable.getAttributeValue("class"))

        var disable = source.getElementById("disable" + operation)
        assertNotNull(disable, source.toString())
        assertEquals("inactive", disable.getAttributeValue("class"))

        config = configDao.get()
        config.operations = asList(operation)
        configDao.save(config)

        output = ByteArrayOutputStream()
        templateInstance = templateService.createConfigurationView(mockSessionToken(false))
        html = templateInstance.render()
        output.write(html.toByteArray())
        source = Source(ByteArrayInputStream(output.toByteArray()))

        enable = source.getElementById("enable" + operation)
        assertNotNull(enable, source.toString())
        assertEquals("inactive", enable.getAttributeValue("class"))

        disable = source.getElementById("disable" + operation)
        assertNotNull(disable, source.toString())
        assertEquals("active", disable.getAttributeValue("class"))
    }
}
