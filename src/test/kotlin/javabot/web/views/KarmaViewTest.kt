package javabot.web.views

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import javabot.dao.KarmaDao
import javabot.model.Karma
import net.htmlparser.jericho.Source
import org.junit.jupiter.api.Test

@QuarkusTest
class KarmaViewTest : ViewsTest() {
    @Inject private lateinit var karmaDao: KarmaDao

    @Test
    fun karma() {
        createKarma(100)

        val output = ByteArrayOutputStream()
        val templateInstance = templateService.createKarmaView(mockSessionToken(false), 0)
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
