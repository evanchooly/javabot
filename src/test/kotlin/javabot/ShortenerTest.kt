package javabot

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import javabot.service.UrlCacheService
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@QuarkusTest
class ShortenerTest : BaseTest() {
    @Inject private lateinit var urlCache: UrlCacheService

    @Test
    fun shorten() {
        val service = urlCache
        assertNotNull(service)
        assertNotNull(service.shorten("http://www.cnn.com/"))
    }
}
