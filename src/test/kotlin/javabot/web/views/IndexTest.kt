package javabot.web.views

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.String.format
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
        val output = ByteArrayOutputStream()
        val templateInstance = templateService.createIndexView(MockServletRequest(loggedIn))
        val html = templateInstance.render()
        output.write(html.toByteArray())

        val source = Source(ByteArrayInputStream(output.toByteArray()))
        val a = source.getElementById("id")
        Assert.assertTrue(
            a == null || loggedIn,
            format("Should %sfind the newChannel link", if (loggedIn) "" else "not "),
        )
    }
}
