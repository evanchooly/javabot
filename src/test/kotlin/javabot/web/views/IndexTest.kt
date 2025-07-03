package javabot.web.views

import freemarker.template.Configuration.VERSION_2_3_32
import io.dropwizard.views.freemarker.FreemarkerViewRenderer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.String.format
import java.util.Locale
import net.htmlparser.jericho.Source
import org.testng.Assert
import org.testng.annotations.Test

class IndexTest : ViewsTest() {
    @Test
    fun index() {
        find(false)
        find(true)
    }

    @Throws(IOException::class)
    protected fun find(loggedIn: Boolean) {
        val renderer = FreemarkerViewRenderer(VERSION_2_3_32)
        val output = ByteArrayOutputStream()

        renderer.render(
            viewFactory.createIndexView(MockServletRequest(loggedIn)),
            Locale.getDefault(),
            output,
        )
        val source = Source(ByteArrayInputStream(output.toByteArray()))
        val a = source.getElementById("id")
        Assert.assertTrue(
            a == null || loggedIn,
            format("Should %sfind the newChannel link", if (loggedIn) "" else "not "),
        )
    }
}
