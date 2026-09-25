package javabot.web.views

import io.quarkus.qute.TemplateInstance
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import javabot.BaseTest
import javax.inject.Inject
import net.htmlparser.jericho.Source
import org.testng.Assert

public open class ViewsTest : BaseTest() {

    @Inject protected lateinit var templateService: TemplateService

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
            "Next page should not be disabled",
        )
    }

    protected fun nextDisabled(source: Source) {
        Assert.assertTrue(
            source
                .getElementById("nextPage")
                .startTag
                .getAttributeValue("class")
                .contains("disabled"),
            "Next page should be disabled",
        )
    }

    protected fun previousDisabled(source: Source) {
        Assert.assertTrue(
            source
                .getElementById("previousPage")
                .startTag
                .getAttributeValue("class")
                .contains("disabled"),
            "Previous page should be disabled",
        )
    }

    protected fun previousEnabled(source: Source) {
        Assert.assertFalse(
            source
                .getElementById("previousPage")
                .startTag
                .getAttributeValue("class")
                .contains("disabled"),
            "Previous page should not be disabled",
        )
    }

    @Throws(IOException::class)
    protected fun render(templateInstance: TemplateInstance): Source {
        val output = ByteArrayOutputStream()
        val html = templateInstance.render()
        output.write(html.toByteArray())
        return Source(ByteArrayInputStream(output.toByteArray()))
    }
}
