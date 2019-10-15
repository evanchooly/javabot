package javabot

import com.google.inject.Inject
import javabot.service.UrlCacheService
import org.testng.annotations.Test
import kotlin.test.assertNotNull

class ShortenerTest : BaseTest() {
    @Inject
    val urlCache: UrlCacheService? = null

    @Test
    fun shorten() {
        val service = urlCache
        assertNotNull(service)
        assertNotNull(service.shorten("http://www.cnn.com/"))
    }
}