package javabot.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.Duration;
import javabot.BaseTest;
import javabot.Message;
import javabot.TestJavabot;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.dao.EventDao;
import javabot.javadoc.Clazz;
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
  private ClazzDao clazzDao;

  @Inject
  private EventDao eventDao;

  private static final String API_NAME = "Servlet";

  private static final String ZIP_LOCATION
      = "http://search.maven.org/remotecontent?filepath=javax/servlet/javax.servlet-api/3.0.1/javax.servlet-api-3.0.1.jar";

  private static final String API_URL_STRING = "http://tomcat.apache.org/tomcat-7.0-doc/servletapi/";

  @Test
  public void addApi() throws IOException {
    TestJavabot javabot = getJavabot();
    dropTestApi();
    final ApiEvent event;
    File file = downloadZip();
    addTestApi(file);
  }

  @Test
  public void addJdk() {
    TestJavabot javabot = getJavabot();
    if (apiDao.find("JDK") == null) {
      Properties properties = System.getProperties();
      File file = new File(System.getProperty("java.home"));
      final ApiEvent event;
      event = new ApiEvent();
      event.setName("JDK");
      event.setType(EventType.ADD);
      event.setBaseUrl("http://docs.oracle.com/javase/7/docs/api");
      event.setPackages("java,javax,org.w3c,org.xml");
      event.setFile(new File(file, "lib/rt.jar").getAbsolutePath());
      event.setRequestedBy(BaseTest.TEST_USER.getNick());
      eventDao.save(event);
      Awaitility.await("adding " + API_NAME)
          .atMost(new Duration(30, TimeUnit.MINUTES))
          .until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
              return eventDao.find(event.getId()).getProcessed();
            }
          });

    }
    List<Message> messages = getJavabot().getMessages();
    Clazz[] classes = clazzDao.getClass("java.lang", "Integer");
    Assert.assertTrue(classes.length == 1);
  }

  private void addTestApi(final File file) {
    final ApiEvent event;
    event = new ApiEvent();
    event.setName(API_NAME);
    event.setType(EventType.ADD);
    event.setBaseUrl(API_URL_STRING);
    event.setPackages("javax");
    event.setFile(file.getAbsolutePath());
    event.setRequestedBy(BaseTest.TEST_USER.getNick());
    eventDao.save(event);
    Awaitility.await("adding " + API_NAME)
        .atMost(new Duration(30, TimeUnit.MINUTES))
        .until(new Callable<Boolean>() {
          @Override
          public Boolean call() throws Exception {
            return eventDao.find(event.getId()).getProcessed();
          }
        });

    List<Message> messages = getJavabot().getMessages();
    Clazz[] classes = clazzDao.getClass("javax.servlet.http", "HttpServletRequest");
    Assert.assertTrue(classes.length == 1);

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
    final ApiEvent event = new ApiEvent();
    event.setName(API_NAME);
    event.setType(EventType.DELETE);
    eventDao.save(event);
    Awaitility.await("dropping " + API_NAME)
        .pollDelay(Duration.FIVE_SECONDS)
        .atMost(Duration.FIVE_MINUTES)
        .until(new Callable<Boolean>() {
          @Override
          public Boolean call() throws Exception {
            return eventDao.find(event.getId()).getProcessed();
          }
        });
  }

  @Test(dependsOnMethods = "addApi")
  public void dropApi() {
    dropTestApi();
  }
}
