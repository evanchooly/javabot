package javabot.operations;

import java.util.Arrays;
import java.util.List;

import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = {"javadoc"})
public class JavadocOperationTest extends BaseOperationTest {
    private static final Logger log = LoggerFactory.getLogger(JavadocOperationTest.class);
    @Autowired
    private ApiDao apiDao;
    @Autowired
    private ClazzDao clazzDao;

    public void methods() {
        scanForResponse("~javadoc String.split(String)", "[JDK: java.lang.String.split(String)]");
        scanForResponse("~javadoc String.split(java.lang.String)", "[JDK: java.lang.String.split(String)]");
        scanForResponse(String.format("%s %s", getJavabot().getNick(), "javadoc String.split(*)"),
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
            getTestBot().getNick() + ", too many results found.  Please see your private messages for results");
    }

    public void fields() {
        scanForResponse("~javadoc Integer.MAX_VALUE", "[JDK: java.lang.Integer.MAX_VALUE:int]");
        scanForResponse("~javadoc System.in", "[JDK: java.lang.System.in:java.io.InputStream]");
    }

    protected void testList(final String message, final String target) {
        final TestBot bot = getTestBot();
        bot.sendMessage(getJavabotChannel(), String.format("%s %s", getJavabot().getNick(), message));
        waitForResponses(bot, 1);
        final String oldest = bot.getOldestResponse().getMessage();
        final String response = oldest.substring((getTestBot().getNick() + ": ").length());
        Assert.assertTrue(response.contains(target));
    }
}
