package javabot.web.resources

import java.net.http.HttpClient
import javabot.BaseTest
import javabot.JavabotConfig
import javabot.web.JavabotApplication
import javabot.web.JavabotConfiguration
import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import javax.inject.Inject
import org.glassfish.jersey.client.ClientResponse
@Test(enabled = false)
class BotResourceTest : BaseTest() {

    @Inject
    lateinit var application: JavabotApplication

    private var client = HttpClient.newHttpClient()

    @Inject
    lateinit var javabotConfig: JavabotConfig

    @Inject
    lateinit var configuration: JavabotConfiguration

    fun badFactoidSearch() {
        TODO()
/*
        if ( application.running ) {
            // TODO pull port from javabot.yml
            val response = client.executor().get()
                    "${javabotConfig.url()}/factoids?name=Diet&value=rgnbxmj29.298%2C+%3Ca+href%3D%22http%3A%2F%2F123diet-guide" +
                            ".com%2F%22%3EDiet%3C%2Fa%3E%2C+SqfUNDm%2C+%5Burl%3Dhttp%3A%2F%2F123diet-guide.com%2F%5DDiet%5B%2Furl%5D%2C" +
                            "+vOJHJvT%2C+http%3A%2F%2F123diet-guide.com%2F+Diet%2C+CbJjvVt.&userName=Diet").get(ClientResponse::class.java)

            assertEquals(response.status, 200)
        }
*/
    }

    fun badLogsDate() {
        TODO()
/*
        if ( application.running ) {
            val response = client.resource("${javabotConfig.url()}/logs/%23%23java/2011-11-0").get(ClientResponse::class.java)

            assertEquals(response.status, 200)
        }
*/
    }
}