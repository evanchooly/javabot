package javabot.web.views

import jakarta.inject.Inject
import java.io.IOException
import javabot.BaseTest
import net.htmlparser.jericho.Source
import org.testng.Assert

open class ViewsTest : BaseTest() {

    @Inject protected lateinit var viewFactory: ViewFactory

    protected fun checkRange(source: Source, from: Int, to: Int, of: Int) {
        val content = source.getElementById("currentPage").content.toString().trim()
        Assert.assertEquals(content, "Displaying ${from} to ${to} of ${of}")
    }

    protected fun nextEnabled(source: Source) {
        Assert.assertFalse(
            source
                .getElementById("nextPage")
                .startTag
                .getAttributeValue("class")
                .contains("disabled"),
            "Next page should not be disabled"
        )
    }

    protected fun nextDisabled(source: Source) {
        Assert.assertTrue(
            source
                .getElementById("nextPage")
                .startTag
                .getAttributeValue("class")
                .contains("disabled"),
            "Next page should be disabled"
        )
    }

    protected fun previousDisabled(source: Source) {
        Assert.assertTrue(
            source
                .getElementById("previousPage")
                .startTag
                .getAttributeValue("class")
                .contains("disabled"),
            "Previous page should be disabled"
        )
    }

    protected fun previousEnabled(source: Source) {
        Assert.assertFalse(
            source
                .getElementById("previousPage")
                .startTag
                .getAttributeValue("class")
                .contains("disabled"),
            "Previous page should not be disabled"
        )
    }

    @Throws(IOException::class)
    protected fun render(view: Any): Source {
        //        val renderer = FreemarkerViewRenderer(VERSION_2_3_32)
        //        val output = ByteArrayOutputStream()
        //        renderer.render(view, Locale.getDefault(), output)
        //        return Source(ByteArrayInputStream(output.toByteArray()))
        return Source("")
    }
}
