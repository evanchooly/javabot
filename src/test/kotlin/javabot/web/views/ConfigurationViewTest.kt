package javabot.web.views

import freemarker.template.Configuration
import freemarker.template.Configuration.VERSION_2_3_32
import io.dropwizard.views.freemarker.FreemarkerViewRenderer
import javabot.dao.ConfigDao
import javabot.web.views.ConfigurationView
import net.htmlparser.jericho.Source
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNotNull
import org.testng.annotations.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.Arrays.asList
import java.util.Locale
import javax.inject.Inject

class ConfigurationViewTest : ViewsTest() {
    @Inject
    protected lateinit var configDao: ConfigDao

    @Test
    fun configuration() {
        var config = configDao.get()
        config.operations = mutableListOf()
        configDao.save(config)

        val renderer = FreemarkerViewRenderer(VERSION_2_3_32)
        var output = ByteArrayOutputStream()
        renderer.render(viewFactory.createConfigurationView(MockServletRequest(false)), Locale.getDefault(), output)
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
        renderer.render(viewFactory.createConfigurationView(MockServletRequest(false)), Locale.getDefault(), output)
        source = Source(ByteArrayInputStream(output.toByteArray()))

        enable = source.getElementById("enable" + operation)
        assertNotNull(enable, source.toString())
        assertEquals(enable.getAttributeValue("class"), "inactive")

        disable = source.getElementById("disable" + operation)
        assertNotNull(disable, source.toString())
        assertEquals(disable.getAttributeValue("class"), "active")

    }

}
