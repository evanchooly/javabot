package javabot.web.views.selenium;

import org.junit.After;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Driver;
import java.util.concurrent.TimeUnit;

public class AdminSeleniumTest {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

//    @BeforeClass
    public void setUp() throws Exception {
        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY,
                           "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
        //        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        //        desiredCapabilities.setPlatform(Platform.MAC);
        driver = new ChromeDriver();
        baseUrl = "http://localhost:8080/";
        Options manage = driver.manage();
        manage.timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

//    @AfterClass
     public void tearDown() throws Exception {
       driver.quit();
       String verificationErrorString = verificationErrors.toString();
       if (!"".equals(verificationErrorString)) {
         Assert.fail(verificationErrorString);
       }
     }

    public void add() throws Exception {
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