package javabot.operations

import javabot.service.HttpService
import javabot.service.JCPJSRLocator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JCPJSFLocatorTest {
    @Test
    fun jsr315() {
        val locator = JCPJSRLocator(HttpService())
        assertEquals(
            "'JSR 315: Java Servlet 3.0 Specification' can be found at http://www.jcp.org/en/jsr/detail?id=315",
            locator.findInformation(315),
        )
    }
}
