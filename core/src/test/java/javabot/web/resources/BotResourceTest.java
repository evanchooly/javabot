package javabot.web.resources;

import javax.inject.Inject;

import com.google.inject.Guice;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import io.dropwizard.testing.junit.ResourceTestRule;
import javabot.BaseTest;
import javabot.JavabotModule;
import javabot.web.JavabotApplication;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BotResourceTest extends BaseTest {

  private Client client;

  @BeforeMethod
  public void setUp() throws Exception {
    client = new Client();
/*
    Guice.createInjector(new JavabotModule())
        .getInstance(JavabotApplication.class)
        .run(new String[]{"server", "javabot.yml"});
*/
  }

  @AfterMethod
  public void tearDown() throws Exception {

  }

  @Test
  public void factoids() {
    ClientResponse response = client.resource("http://localhost:8080/factoids?name=Diet&value=rgnbxmj29.298%2C+%3Ca+"
        + "href%3D%22http%3A%2F%2F123diet-guide.com%2F%22%3EDiet%3C%2Fa%3E%2C+SqfUNDm%2C+%5Burl%3Dhttp%3A%2F%2F123"
        + "diet-guide.com%2F%5DDiet%5B%2Furl%5D%2C+vOJHJvT%2C+http%3A%2F%2F123diet-guide.com%2F+Diet%2C+CbJjvVt."
        + "&userName=Diet")
        .get(ClientResponse.class);

    final int status = response.getStatus();
    assertEquals(status, 200);
  }
}