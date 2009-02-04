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
        testList("javadoc String", response);
        testMessage("javadoc java.lang.String", getTestBot().getNick() + ": " + response);
    }

    public void methods() {
        testMessage("javadoc String.split(String)",
            getTestBot().getNick() + ": http://is.gd/eOPq [java.lang.String.split(String)]");
        testMessage("javadoc String.split(java.lang.String)",
            getTestBot().getNick() + ": http://is.gd/eOPq [java.lang.String.split(String)]");
        final TestBot bot = getTestBot();
        bot.sendMessage(getJavabotChannel(), String.format("%s %s", getJavabot().getNick(), "javadoc String.split(*)"));
        waitForResponses(bot, 1);
        final List<String> responses = Arrays.asList(
            "http://is.gd/eOPq [java.lang.String.split(String)]",
            "http://is.gd/eOPr [java.lang.String.split(String,int)]");
        final String response = bot.getOldestResponse().getMessage();
        Assert.assertTrue(response.contains(responses.get(0)));
        Assert.assertTrue(response.contains(responses.get(1)));
        final List<String> strings = Arrays
            .asList(response.substring((getTestBot().getNick() + ": ").length()).split(";"));
        Assert.assertEquals(strings.size(), 2);
    }

    @SuppressWarnings({"StringContatenationInLoop"})
    public void doFinal() throws InterruptedException {
        final TestBot bot = getTestBot();
        bot.sendMessage(getJavabotChannel(),
            String.format("%s %s", getJavabot().getNick(), "javadoc Cipher.doFinal(*)"));
        waitForResponses(bot, 1);
        final List<String> responses = Arrays.asList(
            "http://is.gd/igel [javax.crypto.Cipher.doFinal()]",
            "http://is.gd/igep [javax.crypto.Cipher.doFinal(ByteBuffer,ByteBuffer)]",
            "http://is.gd/igen [javax.crypto.Cipher.doFinal(byte[])]",
            "http://is.gd/igem [javax.crypto.Cipher.doFinal(byte[],int)]",
            "http://is.gd/igeq [javax.crypto.Cipher.doFinal(byte[],int,int)]",
            "http://is.gd/igeo [javax.crypto.Cipher.doFinal(byte[],int,int,byte[])]",
            "http://is.gd/iger [javax.crypto.Cipher.doFinal(byte[],int,int,byte[],int)]"
        );
        String response = bot.getOldestResponse().getMessage();
        Thread.sleep(5000);
        Assert.assertEquals(response, getTestBot().getNick() + ", too many results found.  Please see your"
            + " private messages for results");
        System.out.println("response = " + response);
        final StringBuilder hits = new StringBuilder();
        while ((response = bot.getOldestMessage()) != null) {
            hits.append(" " + response);
        }
        final List<String> strings = Arrays.asList(hits.substring((getTestBot().getNick() + ": ").length()).split(";"));
        System.out.println("strings = " + strings);
        Assert.assertEquals(strings.size(), responses.size());
    }

    protected void testList(final String message, final String target) {
        final TestBot bot = getTestBot();
        bot.sendMessage(getJavabotChannel(), String.format("%s %s", getJavabot().getNick(), message));
        waitForResponses(bot, 1);
        final String oldest = bot.getOldestResponse().getMessage();
        final String response = oldest.substring((getTestBot().getNick() + ": ").length());
        System.out.println("response = " + response);
        Assert.assertTrue(response.contains(target));
    }
}
