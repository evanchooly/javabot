package javabot;

import com.google.inject.Inject;
import net.swisstech.bitly.BitlyClient;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.internal.Nullable;

public class ShortenerTest extends BaseTest {
    @Inject(optional = true)
    @Nullable
    private BitlyClient client;

    @Test
    public void shorten() {
        if (client != null) {
            Assert.assertNotNull(client.shorten()
                                       .setLongUrl("http://www.cnn.com")
                                       .call()
                                     .data.url);
        }
    }
}