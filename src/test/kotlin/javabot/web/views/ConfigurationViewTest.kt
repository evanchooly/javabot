package javabot.web.views

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Arrays.asList
import javabot.dao.ConfigDao
import javax.inject.Inject
import net.htmlparser.jericho.Source
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNotNull
import org.testng.annotations.Test

class ConfigurationViewTest : ViewsTest() {
    @Inject protected lateinit var configDao: ConfigDao

    @Test(enabled = false)
    fun configuration() {
        var config = configDao.get()
        config.operations = mutableListOf()
        configDao.save(config)

        var output = ByteArrayOutputStream()
        var templateInstance = templateService.createConfigurationView(MockServletRequest(false))
        var html = templateInstance.render()
        output.write(html.toByteArray())
        var source = Source(ByteArrayInputStream(output.toByteArray()))

        val operation = "Javadoc"
        var enable = source.getElementById("enable" + operation)
        assertNotNull(enable, source.toString())
        assertEquals(enable.getAttributeValue("class"), "active")

        var disable = source.getElementById("disable" + operation)
        assertNotNull(disable, source.toString())
        assertEquals(disable.getAttributeValue("class"), "inactive")

        config = configDao.get()
        config.operations = asList(operation)
        configDao.save(config)

        output = ByteArrayOutputStream()
        templateInstance = templateService.createConfigurationView(MockServletRequest(false))
        html = templateInstance.render()
        output.write(html.toByteArray())
        source = Source(ByteArrayInputStream(output.toByteArray()))

        enable = source.getElementById("enable" + operation)
        assertNotNull(enable, source.toString())
        assertEquals(enable.getAttributeValue("class"), "inactive")

        disable = source.getElementById("disable" + operation)
        assertNotNull(disable, source.toString())
        assertEquals(disable.getAttributeValue("class"), "active")
    }
}
