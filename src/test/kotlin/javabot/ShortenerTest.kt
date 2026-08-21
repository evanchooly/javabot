package javabot

import jakarta.inject.Inject
import javabot.service.UrlCacheService
import kotlin.test.assertNotNull
import org.testng.annotations.Test

class ShortenerTest : BaseTest() {
    @Inject val urlCache: UrlCacheService? = null

    @Test
    fun shorten() {
        val service = urlCache
        assertNotNull(service)
        assertNotNull(service.shorten("http://www.cnn.com/"))
    }
}
