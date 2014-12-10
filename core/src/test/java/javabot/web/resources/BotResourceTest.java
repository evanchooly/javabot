package javabot.web.resources;

import com.google.inject.Guice;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import javabot.JavabotModule;
import javabot.web.JavabotApplication;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BotResourceTest {

    private Client client;

    @BeforeMethod
    public void setUp() throws Exception {
        ClientConfig clientConfig = new DefaultClientConfig();

        client = new Client();
        Guice.createInjector(new JavabotModule())
             .getInstance(JavabotApplication.class)
             .run(new String[]{"server", "javabot.yml"});
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }
}