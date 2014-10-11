package javabot.operations;

import com.antwerkz.sofia.Sofia;
import javabot.BaseTest;
import javabot.Message;
import javabot.Messages;
import org.pircbotx.User;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

public abstract class BaseOperationTest extends BaseTest {
    protected void scanForResponse(final String message, final String target) {
        final Messages list = sendMessage(message);
        boolean found = false;
        for (final String response : list) {
            found |= response.contains(target);
        }
        Assert.assertTrue(found, format("Did not find \n'%s' in \n'%s'", target, list));
    }

    protected void testMessage(final String message, final String... responses) {
        compareResults(sendMessage(message), responses);
    }

    protected void testMessageAs(final User user, final String message, final String... responses) {
        compareResults(sendMessage(user, message), responses);
    }

    private void compareResults(final Messages messages, final String[] responses) {
        Assert.assertEquals(messages.size(), responses.length,
                            format("Should get expected response count back. "
                                   + "\n** expected: \n%s"
                                   + "\n** got: \n%s", Arrays.toString(responses), messages));
        for (final String response : responses) {
            Assert.assertEquals(messages.remove(0), response);
        }
        Assert.assertTrue(messages.isEmpty(), "All responses should be matched.");
    }

    protected Messages sendMessage(final String message) {
        return sendMessage(getTestUser(), message);
    }

    protected Messages sendMessage(final User testUser, final String message) {
        getJavabot().processMessage(new Message(getJavabotChannel(), testUser, message));
        return getMessages();
    }

    protected void testMessageList(final String message, final List<String> responses) {
        boolean found = false;
        for (final String response : sendMessage(message)) {
            found |= responses.contains(response);
        }
        Assert.assertTrue(found, format("Should get one response from the list of possibilities"
                                        + "\n** expected: \n%s"
                                        + "\n** got: \n%s", responses, sendMessage(message)));
    }

    protected String getFoundMessage(final String factoid, final String value) {
        return format("%s, %s is %s", getTestUser(), factoid, value);
    }

    protected void forgetFactoid(final String name) {
        testMessage(format("~forget %s", name), Sofia.factoidForgotten(name, getTestUser().getNick()));
    }
}