package javabot.web.views

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeDriverService
import org.testng.Assert
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS

class AdminSeleniumTest {
    lateinit private var driver: WebDriver
    lateinit private var baseUrl: String
    private val acceptNextAlert = true
    private val verificationErrors = StringBuffer()

    //    @BeforeClass
    @Throws(Exception::class) fun setUp() {
        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY,
                "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome")
        //        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        //        desiredCapabilities.setPlatform(Platform.MAC);
        driver = ChromeDriver()
        baseUrl = "http://localhost:8080/"
        val manage = driver.manage()
        manage.timeouts().implicitlyWait(30, SECONDS)
    }

    //    @AfterClass
    @Throws(Exception::class) fun tearDown() {
        driver.quit()
        val verificationErrorString = verificationErrors.toString()
        if ("" != verificationErrorString) {
            Assert.fail(verificationErrorString)
        }
    }

    @Throws(Exception::class) fun add() {
        /*
        selenium.open("/");
        selenium.click("link=Admins");
        selenium.waitForPageToLoad("30000");
        selenium.type("id=userName", "john@example.com");
        selenium.click("css=input[type=\"submit\"]");
        selenium.waitForPageToLoad("30000");
        selenium.click("css=img[alt=\"Delete\"]");
        selenium.waitForPageToLoad("30000");
*/
    }
}