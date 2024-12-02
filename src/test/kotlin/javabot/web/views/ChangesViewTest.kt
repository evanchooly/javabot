package javabot.web.views

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import javabot.model.Change
import net.htmlparser.jericho.Source
import org.testng.Assert.fail
import org.testng.annotations.Test

class ChangesViewTest : ViewsTest() {
    @Test
    fun changes() {
        createChanges(30)

        var output = ByteArrayOutputStream()
        fail()
        /*
                renderer.render(
                    viewFactory.createChangesView(MockServletRequest(false), 0),
                    Locale.getDefault(),
                    output
                )
        */
        var source = Source(ByteArrayInputStream(output.toByteArray()))

        previousDisabled(source)
        nextDisabled(source)
        checkRange(source, 1, 30, 30)

        output = ByteArrayOutputStream()
        fail()
        /*
                renderer.render(
                    viewFactory.createChangesView(MockServletRequest(false), 0, "change 2"),
                    Locale.getDefault(),
                    output
                )
        */
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
