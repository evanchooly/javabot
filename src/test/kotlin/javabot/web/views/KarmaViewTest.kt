package javabot.web.views

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import javabot.dao.KarmaDao
import javabot.model.Karma
import javax.inject.Inject
import net.htmlparser.jericho.Source
import org.testng.annotations.Test

class KarmaViewTest : ViewsTest() {
    @Inject protected lateinit var karmaDao: KarmaDao

    @Test
    fun karma() {
        createKarma(100)

        val output = ByteArrayOutputStream()
        val templateInstance = templateService.createKarmaView(MockServletRequest(false), 0)
        val html = templateInstance.render()
        output.write(html.toByteArray())

        val source = Source(ByteArrayInputStream(output.toByteArray()))

        previousDisabled(source)
        nextEnabled(source)

        checkRange(source, 1, 50, 100)
    }

    private fun createKarma(count: Int) {
        karmaDao.deleteAll()
        for (i in 0..count - 1) {
            val karma = Karma("name " + i, i, "userName " + i)
            karma.updated = LocalDateTime.now()
            karmaDao.save(karma)
        }
    }
}
