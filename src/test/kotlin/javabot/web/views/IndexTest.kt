package javabot.web.views

import io.dropwizard.views.freemarker.FreemarkerViewRenderer
import javabot.kotlin.web.views.IndexView
import net.htmlparser.jericho.Source
import org.testng.Assert
import org.testng.annotations.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.String.format
import java.util.Locale

class IndexTest : ViewsTest() {
    @Test
    @Throws(IOException::class) fun index() {
        find(false)
        find(true)
    }

    @Throws(IOException::class)
    protected fun find(loggedIn: Boolean) {
        val renderer = FreemarkerViewRenderer()
        val output = ByteArrayOutputStream()

        renderer.render(viewFactory.createIndexView(MockServletRequest(loggedIn)), Locale.getDefault(), output)
        val source = Source(ByteArrayInputStream(output.toByteArray()))
        val a = source.getElementById("id")
        Assert.assertTrue(a == null || loggedIn, format("Should %sfind the newChannel link", if (loggedIn) "" else "not "))
    }
}