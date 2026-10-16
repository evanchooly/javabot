package javabot.web.views

import io.quarkus.test.junit.QuarkusTest
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import javabot.model.Change
import net.htmlparser.jericho.Source
import org.junit.jupiter.api.Test

@QuarkusTest
class ChangesViewTest : ViewsTest() {
    @Test
    fun changes() {
        createChanges(30)

        var output = ByteArrayOutputStream()
        var templateInstance =
            templateService.createChangesView(mockSessionToken(false), 0, null, null)
        var html = templateInstance.render()
        output.write(html.toByteArray())
        var source = Source(ByteArrayInputStream(output.toByteArray()))

        previousDisabled(source)
        nextDisabled(source)
        checkRange(source, 1, 30, 30)

        output = ByteArrayOutputStream()
        templateInstance =
            templateService.createChangesView(mockSessionToken(false), 0, "change 2", null)
        html = templateInstance.render()
        output.write(html.toByteArray())
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
