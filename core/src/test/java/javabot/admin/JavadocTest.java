package javabot.admin;

import javax.inject.Inject;

import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import javabot.javadoc.Api;
import javabot.operations.BaseOperationTest;
import org.testng.annotations.Test;

@Test
public class JavadocTest extends BaseOperationTest {
  @Inject
  private ApiDao dao;
  @Inject
  private ClazzDao clazzDao;
  private static final String API_NAME = "Servlet";
  private static final String ZIP_LOCATION
      = "http://search.maven.org/remotecontent?filepath=javax/servlet/javax.servlet-api/3.0.1/javax.servlet-api-3.0.1.jar";
  private static final String API_URL_STRING = "http://tomcat.apache.org/tomcat-7.0-doc/servletapi/index.html";

  @Test
  public void reprocessNonExistentApi() {
    final Api api = dao.find(API_NAME);
    if (api != null) {
      dao.delete(api);
    }
    testMessage("~admin reprocessApi --name=" + API_NAME, "I don't know anything about " + API_NAME);
  }

  @Test(dependsOnMethods = "reprocessNonExistentApi")
  public void processApi() {
    scanForResponse("~admin addApi"
        + " --name=" + API_NAME
        + " --url=" + API_URL_STRING
        + " --zip=" + ZIP_LOCATION
        + " --packages=javax", "done adding javadoc for " + API_NAME);
  }

  @Test(dependsOnMethods = "processApi")
  public void dropApi() {
    scanForResponse("~admin dropApi --name=" + API_NAME, "done removing javadoc for " + API_NAME);
  }
}
