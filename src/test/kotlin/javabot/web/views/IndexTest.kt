package javabot.web.views

import io.quarkus.test.junit.QuarkusTest
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.String.format
import net.htmlparser.jericho.Source
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@QuarkusTest
class IndexTest : ViewsTest() {
    @Test
    fun index() {
        find(false)
        find(true)
    }

    @Throws(IOException::class)
    protected fun find(loggedIn: Boolean) {
        val output = ByteArrayOutputStream()
        val templateInstance = templateService.createIndexView(mockSessionToken(loggedIn))
        val html = templateInstance.render()
        output.write(html.toByteArray())

        val source = Source(ByteArrayInputStream(output.toByteArray()))
        val a = source.getElementById("id")
        assertTrue(
            a == null || loggedIn,
            format("Should %sfind the newChannel link", if (loggedIn) "" else "not "),
        )
    }
}
