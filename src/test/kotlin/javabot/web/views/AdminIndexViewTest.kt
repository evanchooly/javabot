package javabot.web.views

import com.antwerkz.sofia.Sofia
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@QuarkusTest
class AdminIndexViewTest : AdminViewTest() {
    @Test
    @Disabled
    fun index() {
        val source = render()

        val count = adminDao.count()
        for (i in 0 until count) {
            assertNotNull(
                source.getElementById("admin" + i),
                "Trying to find admin${i}\n\n${source}",
            )
        }
        val labels = source.getAllElements("for", "irc", false)
        assertFalse(labels.isEmpty())
        val label = labels[0]
        assertEquals(Sofia.ircName(), label.content.toString())
    }
}
