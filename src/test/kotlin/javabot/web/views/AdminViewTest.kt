package javabot.web.views

import io.dropwizard.views.freemarker.FreemarkerViewRenderer
import javabot.kotlin.web.views.AdminIndexView
import net.htmlparser.jericho.Source

import javax.servlet.http.HttpServletRequest
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Locale

public open class AdminViewTest : ViewsTest() {
    protected fun getRequest(): HttpServletRequest {
        return MockServletRequest(true)
    }


    @Throws(IOException::class)
    protected fun render(): Source {
        val renderer = FreemarkerViewRenderer()
        val output = ByteArrayOutputStream()
        renderer.render(AdminIndexView(injector, getRequest()), Locale.getDefault(), output)
        return Source(ByteArrayInputStream(output.toByteArray()))
    }

}
