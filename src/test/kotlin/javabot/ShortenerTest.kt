package javabot

import io.quarkus.test.junit.QuarkusTest
import javabot.service.UrlCacheService
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Test

@QuarkusTest
class ShortenerTest : BaseTest() {
    private val urlCache: UrlCacheService by lazy {
        injector.getInstance(UrlCacheService::class.java)
    }

    @Test
    fun shorten() {
        val service = urlCache
        assertNotNull(service)
        assertNotNull(service.shorten("http://www.cnn.com/"))
    }
}
