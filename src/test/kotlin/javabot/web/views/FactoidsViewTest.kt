package javabot.web.views

import io.dropwizard.views.freemarker.FreemarkerViewRenderer
import javabot.dao.FactoidDao
import javabot.kotlin.web.views.FactoidsView
import javabot.kotlin.web.views.PagedView
import javabot.model.Factoid
import net.htmlparser.jericho.Source
import org.testng.annotations.Test

import javax.inject.Inject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.util.Locale

class FactoidsViewTest : ViewsTest() {
    @Inject
    protected lateinit var factoidDao: FactoidDao

    @Test
    @Throws(IOException::class) fun singleFactoid() {
        createFactoids(1)
        val source = render(0, Factoid())
        previousDisabled(source)
        nextDisabled(source)
        checkRange(source, 1, 1, 1)
    }

    @Test
    @Throws(IOException::class) fun factoidFilter() {
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

    @Test
    @Throws(IOException::class) fun factoidBadFilter() {
        createFactoids(10)
        val source = render(0, Factoid("bad filter", "", ""))

        previousDisabled(source)
        nextDisabled(source)
        checkRange(source, 0, 0, 0)
    }

    @Test
    @Throws(IOException::class) fun twoFactoidPages() {
        val itemCount = (PagedView.ITEMS_PER_PAGE * 1.5).toInt()
        createFactoids(itemCount)

        var source = render(0, Factoid())
        previousDisabled(source)
        nextEnabled(source)
        checkRange(source, 1, PagedView.ITEMS_PER_PAGE, itemCount)

        source = render(2, Factoid())
        previousEnabled(source)
        nextDisabled(source)
        checkRange(source, PagedView.ITEMS_PER_PAGE + 1, itemCount, itemCount)

        source = render(3, Factoid())
        previousEnabled(source)
        nextDisabled(source)
        checkRange(source, PagedView.ITEMS_PER_PAGE + 1, itemCount, itemCount)
    }

    @Throws(IOException::class)
    protected fun render(page: Int, filter: Factoid): Source {
        val renderer = FreemarkerViewRenderer()
        val output = ByteArrayOutputStream()
        renderer.render(viewFactory.createFactoidsView(MockServletRequest(false), page, filter), Locale.getDefault(), output)
        return Source(ByteArrayInputStream(output.toByteArray()))
    }

    private fun createFactoids(count: Int) {
        factoidDao.deleteAll()
        for (i in 0..count - 1) {
            val factoid = Factoid("name " + i, "value " + i, "userName " + i)
            factoid.updated = LocalDateTime.now()
            factoidDao.save(factoid)
        }
    }
}