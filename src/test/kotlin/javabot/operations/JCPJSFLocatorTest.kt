package javabot.operations

import javabot.service.HttpService
import javabot.service.JCPJSRLocator
import org.testng.Assert
import org.testng.annotations.Test

@Test class JCPJSFLocatorTest {
    fun jsr315() {
        val locator = JCPJSRLocator(HttpService())
        Assert.assertEquals(locator.findInformation(315),
              "'JSR 315: Java Servlet 3.0 Specification' can be found at http://www.jcp.org/en/jsr/detail?id=315")
    }
}