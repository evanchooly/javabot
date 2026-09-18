package javabot.web.views

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@QuarkusTest
class JavadocAdminViewTest : ViewsTest() {

    @Test
    @Disabled
    fun render() {
        render(templateService.createJavadocAdminView(MockServletRequest(false)))
    }
}
