package javabot.admin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
  public void servlets() throws IOException {
    String apiName = "Servlet";
    dropApi(apiName);
    addApi("Servlet", "http://tomcat.apache.org/tomcat-7.0-doc/servletapi/",
        "http://search.maven.org/remotecontent?filepath=javax/servlet/javax.servlet-api/3.0.1/javax.servlet-api-3.0.1.jar");
    checkServlets(apiName);
  }

  @Test(dependsOnMethods = "servlets")
  public void reloadServlets() {
    String apiName = "Servlet";
    ApiEvent event = new ApiEvent(EventType.RELOAD, BaseTest.TEST_USER.getNick(), apiDao.find(apiName).getId());
    eventDao.save(event);
    waitForEvent(event, "reloading " + apiName, new Duration(30, TimeUnit.MINUTES));
    getJavabot().getMessages();
    checkServlets(apiName);
  }

  private void checkServlets(final String apiName) {
    Assert.assertEquals(
        javadocClassDao.getClass(apiDao.find(apiName), "javax.servlet.http", "HttpServletRequest").length, 1);
    scanForResponse("~javadoc HttpServlet", "javax.servlet.http.HttpServlet");
    scanForResponse("~javadoc HttpServlet.doGet(*)", "javax.servlet.http.HttpServlet.doGet");
    scanForResponse("~javadoc HttpServletRequest", "javax.servlet.http.HttpServletRequest");
    scanForResponse("~javadoc HttpServletRequest.getMethod()", "javax.servlet.http.HttpServletRequest.getMethod");
  }

  @Test
  public void javaee() throws IOException {
    String apiName = "JavaEE7";
    dropApi(apiName);
    addApi(apiName, "http://docs.oracle.com/javaee/7/api/",
        "http://search.maven.org/remotecontent?filepath=javax/javaee-api/7.0/javaee-api-7.0.jar");
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
  public void jdk() throws MalformedURLException {
    getJavabot();
    if (Boolean.valueOf(System.getProperty("dropJDK", "false"))) {
      System.out.println("Dropping JDK API");
      dropApi("JDK");
    }
    JavadocApi api = apiDao.find("JDK");
    if (api == null) {
      ApiEvent event = new ApiEvent(BaseTest.TEST_USER.getNick(), "JDK", "http://docs.oracle.com/javase/7/docs/api",
          new File(System.getProperty("java.home"), "lib/rt.jar").toURI().toURL().toString());
      eventDao.save(event);
      waitForEvent(event, "adding JDK", new Duration(30, TimeUnit.MINUTES));
      getJavabot().getMessages();
      api = apiDao.find("JDK");
    }
    Assert.assertEquals(javadocClassDao.getClass(api, "java.lang", "Integer").length, 1);
  }

  private void addApi(final String apiName, final String apiUrlString, final String downloadUrlString) {
    ApiEvent event = new ApiEvent(BaseTest.TEST_USER.getNick(), apiName, apiUrlString, downloadUrlString);
    eventDao.save(event);
    waitForEvent(event, "adding " + apiName, new Duration(30, TimeUnit.MINUTES));
    getJavabot().getMessages();
  }

  private void dropApi(final String apiName) {
    getJavabot();
    final ApiEvent event = new ApiEvent(EventType.DELETE, BaseTest.TEST_USER.getNick(), apiName);
    eventDao.save(event);
    waitForEvent(event, "dropping " + apiName, Duration.FIVE_MINUTES);
  }
}