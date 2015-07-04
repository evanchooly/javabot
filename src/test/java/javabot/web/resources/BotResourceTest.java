package javabot.web.resources;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import javabot.BaseTest;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BotResourceTest extends BaseTest {

  private Client client;

  @BeforeMethod
  public void setUp() throws Exception {
    client = new Client();
  }

  @AfterMethod
  public void tearDown() throws Exception {

  }

  @Test
  public void badFactoidSearch() {
    ClientResponse response = client.resource("http://localhost:8080/factoids?name=Diet&value=rgnbxmj29.298%2C+%3Ca+"
        + "href%3D%22http%3A%2F%2F123diet-guide.com%2F%22%3EDiet%3C%2Fa%3E%2C+SqfUNDm%2C+%5Burl%3Dhttp%3A%2F%2F123"
        + "diet-guide.com%2F%5DDiet%5B%2Furl%5D%2C+vOJHJvT%2C+http%3A%2F%2F123diet-guide.com%2F+Diet%2C+CbJjvVt."
        + "&userName=Diet")
        .get(ClientResponse.class);

    final int status = response.getStatus();
    assertEquals(status, 200);
  }

  @Test
  public void badLogsDate() {
    ClientResponse response = client.resource("http://localhost:8080/logs/%23%23java/2011-11-0")
        .get(ClientResponse.class);

    assertEquals(response.getStatus(), 200);
  }
}