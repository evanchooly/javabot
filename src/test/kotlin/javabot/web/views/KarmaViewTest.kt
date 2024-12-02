package javabot.web.views

import jakarta.inject.Inject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import javabot.dao.KarmaDao
import javabot.model.Karma
import net.htmlparser.jericho.Source
import org.testng.Assert.fail
import org.testng.annotations.Test

class KarmaViewTest : ViewsTest() {
    @Inject protected lateinit var karmaDao: KarmaDao

    @Test
    fun karma() {
        createKarma(100)

        //        val renderer = FreemarkerViewRenderer(VERSION_2_3_32)
        val output = ByteArrayOutputStream()
        fail()
        /*
                renderer.render(
                    viewFactory.createKarmaView(MockServletRequest(false), 0),
                    Locale.getDefault(),
                    output
                )
        */
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
