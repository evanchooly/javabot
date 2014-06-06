package javabot;

import javax.inject.Inject;

import net.swisstech.bitly.BitlyClient;
import net.swisstech.bitly.model.Response;
import net.swisstech.bitly.model.v3.ShortenResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ShortenerTest extends BaseTest {
  @Inject
  private BitlyClient client;

  @Test
  public void shorten() {
    Response<ShortenResponse> respShort = client.shorten()
        .setLongUrl("http://www.cnn.com")
        .call();
    Assert.assertNotNull(respShort.data.url);
  }
}