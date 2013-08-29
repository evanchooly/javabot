package javabot.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import com.jayway.awaitility.Duration;
import javabot.BaseTest;
import javabot.dao.ApiDao;
import javabot.dao.JavadocClassDao;
import javabot.javadoc.JavadocApi;
import javabot.model.ApiEvent;
import javabot.model.EventType;
import javabot.operations.BaseOperationTest;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class JavadocTest extends BaseOperationTest {
  @Inject
  private ApiDao apiDao;

  @Inject
  private JavadocClassDao javadocClassDao;

  private static final String API_NAME = "Servlet";

  private static final String ZIP_LOCATION
      = "http://search.maven.org/remotecontent?filepath=javax/servlet/javax.servlet-api/3.0.1/javax.servlet-api-3.0.1.jar";

  private static final String API_URL_STRING = "http://tomcat.apache.org/tomcat-7.0-doc/servletapi/";

  @Test
  public void addApi() throws IOException {
    dropTestApi();
    addTestApi(downloadZip());
  }

  @Test
  public void addJdk() {
    getJavabot();
    JavadocApi api = apiDao.find("JDK");
    if(Boolean.valueOf(System.getProperty("dropJDK", "false"))) {
      System.out.println("Dropping JDK API");
      apiDao.delete(api);
      api = null;
    }
    if (api == null) {
      ApiEvent event = new ApiEvent();
      event.setName("JDK");
      event.setType(EventType.ADD);
      event.setBaseUrl("http://docs.oracle.com/javase/7/docs/api");
      event.setFile(new File(System.getProperty("java.home"), "lib/rt.jar").getAbsolutePath());
      event.setRequestedBy(BaseTest.TEST_USER.getNick());
      eventDao.save(event);
      waitForEvent(event, "adding " + API_NAME, new Duration(30, TimeUnit.MINUTES));
      getJavabot().getMessages();
      api = apiDao.find("JDK");
    }
    Assert.assertEquals(javadocClassDao.getClass(api, "java.lang", "Integer").length, 1);
  }

  private void addTestApi(final File file) {
    ApiEvent event = new ApiEvent();
    event.setName(API_NAME);
    event.setType(EventType.ADD);
    event.setBaseUrl(API_URL_STRING);
    event.setFile(file.getAbsolutePath());
    event.setRequestedBy(BaseTest.TEST_USER.getNick());
    eventDao.save(event);
    waitForEvent(event, "adding " + API_NAME, new Duration(30, TimeUnit.MINUTES));
    getJavabot().getMessages();
    Assert.assertTrue(javadocClassDao.getClass(
        apiDao.find(API_NAME), "javax.servlet.http", "HttpServletRequest").length != 0);
    scanForResponse("~javadoc HttpServlet", "javax.servlet.http.HttpServlet");
    scanForResponse("~javadoc HttpServlet.doGet(*)", "javax.servlet.http.HttpServlet.doGet");
    scanForResponse("~javadoc HttpServletRequest", "javax.servlet.http.HttpServletRequest");
    scanForResponse("~javadoc HttpServletRequest.getMethod()", "javax.servlet.http.HttpServletRequest.getMethod");
  }

  private File downloadZip() throws IOException {
    File file = new File("/tmp/javax.servlet-api-3.0.1.jar");
    if (!file.exists()) {
      try (InputStream inputStream = new URL(ZIP_LOCATION).openStream();
           OutputStream fos = new FileOutputStream(file)) {
        byte[] bytes = new byte[8192];
        int read;
        while ((read = inputStream.read(bytes)) != -1) {
          fos.write(bytes, 0, read);
        }
      }
    }
    return file;
  }

  private void dropTestApi() {
    getJavabot();
    final ApiEvent event = new ApiEvent();
    event.setName(API_NAME);
    event.setType(EventType.DELETE);
    eventDao.save(event);
    waitForEvent(event, "dropping " + API_NAME, Duration.FIVE_MINUTES);
  }
}