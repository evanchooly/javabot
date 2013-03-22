package javabot.operations;

import java.util.Arrays;
import java.util.List;

import javabot.BaseTest;
import javabot.IrcEvent;
import javabot.IrcUser;
import javabot.Message;
import org.testng.Assert;

public abstract class BaseOperationTest extends BaseTest {
  protected void scanForResponse(final String message, final String target) {
    final List<Message> list = sendMessage(message);
    StringBuilder returned = new StringBuilder();
    boolean found = false;
    for (final Message response : list) {
      returned.append(response.getMessage())
        .append("\n");
      found |= response.getMessage().contains(target);
    }
    Assert.assertTrue(found, String.format("Did not find '%s' in '%s'", target, list));
  }

  protected void testMessage(final String message, final String... responses) {
    compareResults(sendMessage(message), responses);
  }

  protected void testMessageAs(final IrcUser user, final String message, final String... responses) {
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
    return sendMessage(BaseTest.TEST_USER, message);
  }

  private List<Message> sendMessage(final IrcUser testUser, final String message) {
    getJavabot().processMessage(new IrcEvent(getJavabotChannel(), testUser, message));
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

  public String getForgetMessage(final IrcUser testUser, final String factoid) {
    return String.format("I forgot about %s, %s.", factoid, testUser);
  }

  protected String getFoundMessage(final String factoid, final String value) {
    return String.format("%s, %s is %s", BaseTest.TEST_USER, factoid, value);
  }

  protected void forgetFactoid(final String name) {
    testMessage(String.format("~forget %s", name), getForgetMessage(name));
  }
}