package javabot.web.views

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDateTime
import javabot.dao.FactoidDao
import javabot.model.Factoid
import javax.inject.Inject
import net.htmlparser.jericho.Source
import org.testng.annotations.Test

@Test(enabled = false)
class FactoidsViewTest : ViewsTest() {
    @Inject lateinit var factoidDao: FactoidDao

    fun singleFactoid() {
        createFactoids(1)
        val source = render(0, Factoid())
        previousDisabled(source)
        nextDisabled(source)
        checkRange(source, 1, 1, 1)
    }

    fun factoidFilter() {
        createFactoids(10)
        var source = render(0, Factoid("name 1", "", ""))

        previousDisabled(source)
        nextDisabled(source)
        checkRange(source, 1, 1, 1)

        source = render(0, Factoid("", "value 1", ""))
        previousDisabled(source)
        nextDisabled(source)
        checkRange(source, 1, 1, 1)

        source = render(0, Factoid("", "", "userName 1"))
        previousDisabled(source)
        nextDisabled(source)
        checkRange(source, 1, 1, 1)
    }

    fun factoidBadFilter() {
        createFactoids(10)
        val source = render(0, Factoid("bad filter", "", ""))

        previousDisabled(source)
        nextDisabled(source)
        checkRange(source, 0, 0, 0)
    }

    fun twoFactoidPages() {
        val itemCount = (TemplateService.ITEMS_PER_PAGE * 1.5).toInt()
        createFactoids(itemCount)

        var source = render(0, Factoid())
        previousDisabled(source)
        nextEnabled(source)
        checkRange(source, 1, TemplateService.ITEMS_PER_PAGE, itemCount)

        source = render(2, Factoid())
        previousEnabled(source)
        nextDisabled(source)
        checkRange(source, TemplateService.ITEMS_PER_PAGE + 1, itemCount, itemCount)

        source = render(3, Factoid())
        previousEnabled(source)
        nextDisabled(source)
        checkRange(source, TemplateService.ITEMS_PER_PAGE + 1, itemCount, itemCount)
    }

    @Throws(IOException::class)
    private fun render(page: Int, filter: Factoid): Source {
        val output = ByteArrayOutputStream()
        val templateInstance =
            templateService.createFactoidsView(MockServletRequest(false), page, filter)
        val html = templateInstance.render()
        output.write(html.toByteArray())
        return Source(ByteArrayInputStream(output.toByteArray()))
    }

    private fun createFactoids(count: Int) {
        factoidDao.deleteAll()
        for (i in 0 until count) {
            val factoid = Factoid("name $i", "value $i", "userName $i")
            factoid.updated = LocalDateTime.now()
            factoidDao.save(factoid)
        }
    }
}
