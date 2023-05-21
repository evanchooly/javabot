package javabot.web.views

import freemarker.template.Configuration.VERSION_2_3_32
import io.dropwizard.views.freemarker.FreemarkerViewRenderer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.Locale
import javabot.model.Change
import net.htmlparser.jericho.Source
import org.testng.annotations.Test

class ChangesViewTest : ViewsTest() {
    @Test
    fun changes() {
        createChanges(30)

        val renderer = FreemarkerViewRenderer(VERSION_2_3_32)
        var output = ByteArrayOutputStream()
        renderer.render(
            viewFactory.createChangesView(MockServletRequest(false), 0),
            Locale.getDefault(),
            output
        )
        var source = Source(ByteArrayInputStream(output.toByteArray()))

        previousDisabled(source)
        nextDisabled(source)
        checkRange(source, 1, 30, 30)

        output = ByteArrayOutputStream()
        renderer.render(
            viewFactory.createChangesView(MockServletRequest(false), 0, "change 2"),
            Locale.getDefault(),
            output
        )
        source = Source(ByteArrayInputStream(output.toByteArray()))

        previousDisabled(source)
        nextDisabled(source)
        checkRange(source, 1, 11, 11)
    }

    private fun createChanges(count: Int) {
        changeDao.deleteAll()
        for (i in 0..count - 1) {
            val change = Change("change " + i)
            change.changeDate = LocalDateTime.now()
            changeDao.save(change)
        }
    }
}
