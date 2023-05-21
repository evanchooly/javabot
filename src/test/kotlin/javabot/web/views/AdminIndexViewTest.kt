package javabot.web.views

import com.antwerkz.sofia.Sofia
import org.testng.Assert
import org.testng.annotations.Test

class AdminIndexViewTest : AdminViewTest() {
    @Test
    fun index() {
        val source = render()

        val count = adminDao.count()
        for (i in 0 until count) {
            Assert.assertNotNull(
                source.getElementById("admin" + i),
                "Trying to find admin${i}\n\n${source}"
            )
        }
        val labels = source.getAllElements("for", "irc", false)
        Assert.assertFalse(labels.isEmpty())
        val label = labels[0]
        Assert.assertEquals(label.content.toString(), Sofia.ircName())
    }
}
