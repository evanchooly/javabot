package javabot.web.views

import org.testng.annotations.Test

class JavadocAdminViewTest : ViewsTest() {

    @Test(enabled = false)
    fun render() {
        render(templateService.createJavadocAdminView(MockServletRequest(false)))
    }
}
