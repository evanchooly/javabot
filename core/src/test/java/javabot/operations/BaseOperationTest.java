package javabot.operations;

import java.util.Arrays;
import java.util.List;

import javabot.BaseTest;
import javabot.Message;
import org.schwering.irc.lib.IRCUser;
import org.testng.Assert;
import org.unitils.spring.annotation.SpringApplicationContext;

@SpringApplicationContext("classpath:applicationContext.xml")
public abstract class BaseOperationTest extends BaseTest {
    protected void scanForResponse(final String message, final String target) {
        final List<Message> list = sendMessage(message);
        boolean found = false;
        for (final Message response : list) {
            found |= response.getMessage().contains(target);
        }

        Assert.assertTrue(found, String.format("Should find the response in the responses."
            + "\n** expected: \n%s"
            + "\n** got: \n%s", target, list));
    }

    protected void testMessage(final String message, final String... responses) {
        compareResults(sendMessage(message), responses);
    }

    protected void testMessageAs(final IRCUser user, final String message, final String... responses) {
        compareResults(sendMessage(user, message), responses);
    }

    private void compareResults(final List<Message> list, final String[] responses) {
        Assert.assertEquals(list.size(), responses.length, String.format("Should get expected response count back. "
                + "\n** expected: \n%s"
                + "\n** got: \n%s", Arrays.toString(responses), list));
        for (final String response : responses) {
            Assert.assertEquals(list.remove(0).getMessage(), response);
        }
        Assert.assertTrue(list.isEmpty(), "All responses should be matched.");
    }

    protected List<Message> sendMessage(final String message) {
        return sendMessage(TEST_USER, message);
    }

    private List<Message> sendMessage(final IRCUser testUser, final String message) {
        getJavabot().getListener().processMessage(getJavabotChannel(), message, testUser);
        return getJavabot().getMessages();
    }

    protected void testMessageList(final String message, final List<String> responses) {
        final List<Message> list = sendMessage(message);
        boolean found = false;
        for (final Message response : list) {
            found |= responses.contains(response.getMessage());
        }

        Assert.assertTrue(found, String.format("Should get one response from the list of possibilities"
            + "\n** expected: \n%s"
            + "\n** got: \n%s", responses, list));
    }

    public String getForgetMessage(final String factoid) {
        return getForgetMessage(BaseTest.TEST_USER, factoid);
    }

    public String getForgetMessage(final IRCUser testUser, final String factoid) {
        return String.format("I forgot about %s, %s.", factoid, testUser);
    }

    protected String getFoundMessage(final String factoid, final String value) {
        return String.format("%s, %s is %s", BaseTest.TEST_USER, factoid, value);
    }

    protected void forgetFactoid(final String name) {
        testMessage(String.format("~forget %s", name), getForgetMessage(name));
    }
}