package javabot.web.views

import freemarker.template.Configuration.VERSION_2_3_32
import io.dropwizard.views.freemarker.FreemarkerViewRenderer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Locale
import javabot.model.Admin
import javax.servlet.http.HttpServletRequest
import net.htmlparser.jericho.Source

open class AdminViewTest : ViewsTest() {
    protected fun getRequest(): HttpServletRequest {
        return MockServletRequest(true)
    }

    protected fun render(): Source {
        val renderer = FreemarkerViewRenderer(VERSION_2_3_32)
        val output = ByteArrayOutputStream()
        renderer.render(
            viewFactory.createAdminIndexView(getRequest(), adminDao.findAll()[0], Admin()),
            Locale.getDefault(),
            output,
        )
        return Source(ByteArrayInputStream(output.toByteArray()))
    }
}
