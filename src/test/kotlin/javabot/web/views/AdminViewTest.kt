package javabot.web.views

import jakarta.servlet.http.HttpServletRequest
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javabot.model.Admin
import net.htmlparser.jericho.Source

open class AdminViewTest : ViewsTest() {
    protected fun getRequest(): HttpServletRequest {
        return MockServletRequest(true)
    }

    protected fun render(): Source {
        val output = ByteArrayOutputStream()
        val templateInstance =
            templateService.createAdminIndexView(getRequest(), adminDao.findAll()[0], Admin())
        val html = templateInstance.render()
        output.write(html.toByteArray())
        return Source(ByteArrayInputStream(output.toByteArray()))
    }
}
