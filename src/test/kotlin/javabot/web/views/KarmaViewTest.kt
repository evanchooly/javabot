package javabot.web.views

import io.dropwizard.views.freemarker.FreemarkerViewRenderer
import javabot.dao.KarmaDao
import javabot.kotlin.web.views.KarmaView
import javabot.model.Karma
import net.htmlparser.jericho.Source
import org.testng.annotations.Test

import javax.inject.Inject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.util.Locale

public class KarmaViewTest : ViewsTest() {
    @Inject
    private val karmaDao: KarmaDao? = null

    @Test
    @Throws(IOException::class)
    public fun karma() {
        createKarma(100)

        val renderer = FreemarkerViewRenderer()
        val output = ByteArrayOutputStream()
        renderer.render(KarmaView(injector, MockServletRequest(false), 0), Locale.getDefault(), output)
        val source = Source(ByteArrayInputStream(output.toByteArray()))

        previousDisabled(source)
        nextEnabled(source)

        checkRange(source, 1, 50, 100)
    }

    private fun createKarma(count: Int) {
        karmaDao!!.deleteAll()
        for (i in 0..count - 1) {
            val karma = Karma("name " + i, i, "userName " + i)
            karma.updated = LocalDateTime.now()
            karmaDao.save(karma)
        }
    }

}
