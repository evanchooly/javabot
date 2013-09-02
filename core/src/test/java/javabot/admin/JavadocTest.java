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

  @Test
  public void addServletApi() throws IOException {
    String apiName = "Servlet";
    dropServletApi(apiName);
    addTestApi(downloadZip("javax.servlet-api-3.0.1.jar",
        "http://search.maven.org/remotecontent?filepath=javax/servlet/javax.servlet-api/3.0.1/javax.servlet-api-3.0.1.jar"),
        "Servlet", "http://tomcat.apache.org/tomcat-7.0-doc/servletapi/");
    Assert.assertTrue(javadocClassDao.getClass(
        apiDao.find(apiName), "javax.servlet.http", "HttpServletRequest").length != 0);
    scanForResponse("~javadoc HttpServlet", "javax.servlet.http.HttpServlet");
    scanForResponse("~javadoc HttpServlet.doGet(*)", "javax.servlet.http.HttpServlet.doGet");
    scanForResponse("~javadoc HttpServletRequest", "javax.servlet.http.HttpServletRequest");
    scanForResponse("~javadoc HttpServletRequest.getMethod()", "javax.servlet.http.HttpServletRequest.getMethod");
  }

  @Test
  public void addEEApi() throws IOException {
    String apiName = "JavaEE7";
    dropServletApi(apiName);
    addTestApi(downloadZip("javaee-api-7.0.jar",
        "http://search.maven.org/remotecontent?filepath=javax/javaee-api/7.0/javaee-api-7.0.jar"),
        apiName, "http://docs.oracle.com/javaee/7/api/");
    scanForResponse("~javadoc Annotated", "javax.enterprise.inject.spi.Annotated");
    scanForResponse("~javadoc Annotated.getAnnotation(*)", "javax.enterprise.inject.spi.Annotated.getAnnotation");

    scanForResponse("~javadoc ContextService", "javax.enterprise.concurrent.ContextService");
    scanForResponse("~javadoc ContextService.createContextualProxy(*)", "createContextualProxy(Object, Class...)");
    scanForResponse("~javadoc ContextService.createContextualProxy(*)",
        "createContextualProxy(Object, Map<String, String>, Class...)");
    scanForResponse("~javadoc ContextService.createContextualProxy(*)", "createContextualProxy(T, Class<T>)");
    scanForResponse("~javadoc ContextService.createContextualProxy(*)",
        "createContextualProxy(T, Map<String, String>, Class<T>)");

    scanForResponse("~javadoc PartitionPlan", "[JavaEE7: javax.batch.api.partition.PartitionPlan]");
    scanForResponse("~javadoc PartitionPlan.setPartitionProperties(Properties[])",
        "javax.batch.api.partition.PartitionPlan.setPartitionProperties(Properties[])");
  }

  @Test
  public void addJdk() {
    getJavabot();
    JavadocApi api = apiDao.find("JDK");
    if (Boolean.valueOf(System.getProperty("dropJDK", "false"))) {
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
      waitForEvent(event, "adding JDK", new Duration(30, TimeUnit.MINUTES));
      getJavabot().getMessages();
      api = apiDao.find("JDK");
    }
    Assert.assertEquals(javadocClassDao.getClass(api, "java.lang", "Integer").length, 1);
  }

  private void addTestApi(final File file, final String apiName, final String apiUrlString) {
    ApiEvent event = new ApiEvent();
    event.setName(apiName);
    event.setType(EventType.ADD);
    event.setBaseUrl(apiUrlString);
    event.setFile(file.getAbsolutePath());
    event.setRequestedBy(BaseTest.TEST_USER.getNick());
    eventDao.save(event);
    waitForEvent(event, "adding " + apiName, new Duration(30, TimeUnit.MINUTES));
    getJavabot().getMessages();
  }

  private File downloadZip(final String fileName, final String zipURL) throws IOException {
    File file = new File("/tmp/" + fileName);
    if (!file.exists()) {
      try (InputStream inputStream = new URL(zipURL).openStream();
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

  private void dropServletApi(final String apiName) {
    getJavabot();
    final ApiEvent event = new ApiEvent();
    event.setName(apiName);
    event.setType(EventType.DELETE);
    eventDao.save(event);
    waitForEvent(event, "dropping " + apiName, Duration.FIVE_MINUTES);
  }
}