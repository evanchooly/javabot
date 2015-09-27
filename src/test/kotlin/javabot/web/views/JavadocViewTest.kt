package javabot.web.views

import javabot.kotlin.web.views.JavadocView
import org.testng.annotations.Test
import java.io.IOException

public class JavadocViewTest : ViewsTest() {

    @Test
    @Throws(IOException::class)
    public fun render() {
        val view = JavadocView(injector, MockServletRequest(false))
        render(view)
    }

}