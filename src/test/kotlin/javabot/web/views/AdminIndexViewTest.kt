package javabot.web.views

import com.antwerkz.sofia.Sofia
import javabot.dao.AdminDao
import net.htmlparser.jericho.Element
import net.htmlparser.jericho.Source
import org.testng.Assert
import org.testng.annotations.Test

import javax.inject.Inject
import java.io.IOException

public class AdminIndexViewTest : AdminViewTest() {
    @Test
    @Throws(IOException::class)
    public fun index() {
        val source = render()

        val count = adminDao.count()
        for (i in 0..count - 1) {
            Assert.assertNotNull(source.getElementById("admin" + i), "Trying to find admin${i}\n\n${source}")
        }
        val labels = source.getAllElements("for", "irc", false)
        Assert.assertFalse(labels.isEmpty())
        val label = labels.get(0)
        Assert.assertEquals(label.content.toString(), Sofia.ircName())
    }
}
