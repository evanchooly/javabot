package javabot.web.views

import freemarker.template.TemplateModelException
import io.dropwizard.views.freemarker.FreemarkerViewRenderer
import javabot.model.Admin
import net.htmlparser.jericho.Source
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.Locale
import javax.servlet.http.HttpServletRequest

open class AdminViewTest : ViewsTest() {
    protected fun getRequest(): HttpServletRequest {
        return MockServletRequest(true)
    }


    @Throws(IOException::class)
    protected fun render(): Source {
        val renderer = FreemarkerViewRenderer()
        val output = ByteArrayOutputStream()
        renderer.render(viewFactory.createAdminIndexView(getRequest(), adminDao.findAll()[0], Admin()), Locale.getDefault(), output)
        return Source(ByteArrayInputStream(output.toByteArray()))
    }

}
