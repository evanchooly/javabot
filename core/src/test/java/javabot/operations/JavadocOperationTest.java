package javabot.operations;

import java.util.Arrays;
import java.util.List;

import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created Aug 9, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
@Test(dependsOnGroups = {"javadoc"})
public class JavadocOperationTest extends BaseOperationTest {
    @Autowired
    private ApiDao apiDao;
    @Autowired
    private ClazzDao clazzDao;

    public void string() {
        final String response = "http://is.gd/6UoM [java.lang.String]";

        testMessage("javadoc String", response);
        testMessage("javadoc java.lang.String", response);
    }

    public void methods() {
        testMessage("javadoc String.split(String)", "http://is.gd/eOPq [java.lang.String.split(String)]");
        testMessage("javadoc String.split(java.lang.String)", "http://is.gd/eOPq [java.lang.String.split(String)]");

        final TestBot bot = getTestBot();
        bot.sendMessage(getJavabotChannel(), String.format("%s %s", getJavabot().getNick(), "javadoc String.split(*)"));
        waitForResponses(bot, 1);
        final List<String> responses = Arrays.asList(
            "http://is.gd/eOPq [java.lang.String.split(String)]",
            "http://is.gd/eOPr [java.lang.String.split(String,int)]");
        System.out.println("responses = " + responses);
        final String response = bot.getOldestResponse().getMessage();
        System.out.println("response = " + response);
        Assert.assertTrue(response.contains(responses.get(0)));
        Assert.assertTrue(response.contains(responses.get(1)));
        Assert.assertTrue(response.split(",").length == 2, "Should only find 2 matches");
    }
}
