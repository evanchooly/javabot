package javabot.web.views

import jakarta.servlet.http.HttpServletRequest
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import net.htmlparser.jericho.Source
import org.testng.Assert.fail

open class AdminViewTest : ViewsTest() {
    protected fun getRequest(): HttpServletRequest {
        return MockServletRequest(true)
    }

    protected fun render(): Source {
        //        val renderer = FreemarkerViewRenderer(VERSION_2_3_32)
        fail()
        val output = ByteArrayOutputStream()
        /*
                renderer.render(
                    viewFactory.createAdminIndexView(getRequest(), adminDao.findAll()[0], Admin()),
                    Locale.getDefault(),
                    output
                )
        */
        return Source(ByteArrayInputStream(output.toByteArray()))
    }
}
