package javabot.web.resources

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import javabot.BaseTest
import javabot.kotlin.web.JavabotApplication
import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import javax.inject.Inject

public class BotResourceTest : BaseTest() {

    @Inject
    lateinit var application: JavabotApplication

    private var client = Client()

    @Test
    public fun badFactoidSearch() {
        if ( application.running ) {
            val response = client.resource(
                    "http://localhost:8080/factoids?name=Diet&value=rgnbxmj29.298%2C+%3Ca+" + "href%3D%22http%3A%2F%2F123diet-guide.com%2F%22%3EDiet%3C%2Fa%3E%2C+SqfUNDm%2C+%5Burl%3Dhttp%3A%2F%2F123" + "diet-guide.com%2F%5DDiet%5B%2Furl%5D%2C+vOJHJvT%2C+http%3A%2F%2F123diet-guide.com%2F+Diet%2C+CbJjvVt." + "&userName=Diet").get(
                    ClientResponse::class.java)

            assertEquals(response.status, 200)
        }
    }

    @Test
    public fun badLogsDate() {
        if ( application.running ) {
            val response = client.resource("http://localhost:8080/logs/%23%23java/2011-11-0").get(ClientResponse::class.java)

            assertEquals(response.status, 200)
        }
    }
}