package javabot.web.views

import org.testng.annotations.Test

class JavadocAdminViewTest : ViewsTest() {

    @Test
    fun render() {
        render(viewFactory.createJavadocAdminView(MockServletRequest(false)))
    }

}