package javabot.operations;

import javax.inject.Inject;

import javabot.BaseTest;
import javabot.dao.ApiDao;
import javabot.dao.JavadocClassDao;
import org.testng.annotations.Test;

@Test(dependsOnMethods = {"javabot.admin.JavadocTest.addJdk"})
public class JavadocOperationTest extends BaseOperationTest {
  @Inject
  private ApiDao apiDao;
  @Inject
  private JavadocClassDao javadocClassDao;

  public void constructors() {
    scanForResponse("~javadoc java.lang.String(char[])", "[JDK: java.lang.String.String(char[])]");
    scanForResponse("~javadoc String(char[])", "[JDK: java.lang.String.String(char[])]");
  }

  public void methods() {
    scanForResponse("~javadoc String.split(String)", "[JDK: java.lang.String.split(String)]");
    scanForResponse("~javadoc -jdk String.split(String)", "[JDK: java.lang.String.split(String)]");
    scanForResponse("~javadoc String.split(java.lang.String)", "[JDK: java.lang.String.split(String)]");
    scanForResponse(String.format("%s %s", getJavabot().getPircBot().getNick(), "javadoc String.split(*)"),
        "[JDK: java.lang.String.split(String)]");
  }

  public void nestedClasses() {
    scanForResponse("~javadoc Map.Entry", "[JDK: java.util.Map.Entry]");
  }

  public void format() {
    scanForResponse("~javadoc String.format(*)", "[JDK: java.lang.String.format(Locale, String, Object...)]");
  }

  @Test
  public void doFinal() {
    scanForResponse("~javadoc String.valueOf(*)",
        BaseTest.TEST_USER + ", too many results found.  Please see your private messages for results");
  }

  public void fields() {
    scanForResponse("~javadoc System.in", "[JDK: java.lang.System#in:java.io.InputStream]");
    scanForResponse("~javadoc Integer.MAX_VALUE", "[JDK: java.lang.Integer#MAX_VALUE:int]");
    scanForResponse("~javadoc -jdk System.in", "[JDK: java.lang.System#in:java.io.InputStream]");
  }

  public void inherited() {
    scanForResponse("~javadoc ArrayList.listIterator(*)", "[JDK: java.util.ArrayList.listIterator(int)]");
  }
}