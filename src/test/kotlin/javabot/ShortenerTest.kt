package javabot

import com.google.inject.Inject
import net.swisstech.bitly.BitlyClient
import org.testng.Assert
import org.testng.annotations.Test
import org.testng.internal.Nullable

class ShortenerTest : BaseTest() {
    @Inject(optional = true)
    @Nullable
    var client: BitlyClient? = null

    @Test fun shorten() {
        if (client != null) {
            Assert.assertNotNull(client!!.shorten().setLongUrl("http://www.cnn.com").call().data.url)
        }
    }
}