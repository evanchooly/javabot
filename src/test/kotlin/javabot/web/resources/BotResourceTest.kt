package javabot.web.resources

import io.quarkus.test.common.http.TestHTTPResource
import io.quarkus.test.junit.QuarkusTest
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import javabot.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@QuarkusTest
class BotResourceTest : BaseTest() {

    @TestHTTPResource("/") lateinit var baseUri: URI

    private val client = HttpClient.newHttpClient()

    private fun get(path: String): HttpResponse<String> {
        val request = HttpRequest.newBuilder(baseUri.resolve(path)).GET().build()
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    @Test
    fun badFactoidSearch() {
        val response =
            get(
                "/factoids?name=Diet&value=rgnbxmj29.298%2C+%3Ca+href%3D%22http%3A%2F%2F123diet-guide" +
                    ".com%2F%22%3EDiet%3C%2Fa%3E%2C+SqfUNDm%2C+%5Burl%3Dhttp%3A%2F%2F123diet-guide.com%2F%5DDiet%5B%2Furl%5D%2C" +
                    "+vOJHJvT%2C+http%3A%2F%2F123diet-guide.com%2F+Diet%2C+CbJjvVt.&userName=Diet"
            )
        assertEquals(200, response.statusCode())
    }

    @Test
    fun badLogsDate() {
        val response = get("/logs/%23%23java/2011-11-0")
        assertEquals(200, response.statusCode())
    }
}
