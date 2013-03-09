package javabot.operations;

import javax.inject.Inject;

import javabot.BaseTest;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import org.testng.annotations.Test;

@Test(enabled=false)//(dependsOnGroups = {"javadoc"})
public class JavadocOperationTest extends BaseOperationTest {
  @Inject
  private ApiDao apiDao;
  @Inject
  private ClazzDao clazzDao;

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
    scanForResponse("~javadoc String.format(*)", "[JDK: java.lang.String.format(Locale,String,Object[])]");
  }

  public void doFinal() {
    scanForResponse("~javadoc Cipher.doFinal(*)",
        BaseTest.TEST_USER + ", too many results found.  Please see your private messages for results");
  }

  public void fields() {
    scanForResponse("~javadoc Integer.MAX_VALUE", "[JDK: java.lang.Integer.MAX_VALUE:int]");
    scanForResponse("~javadoc System.in", "[JDK: java.lang.System.in:java.io.InputStream]");
    scanForResponse("~javadoc -jdk System.in", "[JDK: java.lang.System.in:java.io.InputStream]");
  }

  public void inherited() {
    scanForResponse("~javadoc ArrayList.listIterator(*)", "[JDK: java.util.AbstractList.listIterator(int)]");
  }
}