package javabot.web.views

import javabot.web.views.JavadocView
import org.testng.annotations.Test
import java.io.IOException

class JavadocViewTest : ViewsTest() {

    @Test
    fun render() {
        render(viewFactory.createJavadocView(MockServletRequest(false)))
    }

}