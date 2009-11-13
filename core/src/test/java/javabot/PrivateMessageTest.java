package javabot;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class PrivateMessageTest extends BaseTest {
    public void sendMessage() {
        getTestBot().sendMessage(getJavabot().getNick(), "noReply");
        validateResponses(getTestBot(), getTestBot().getNick() + ", noReply is I'm a reply!");
    }

    protected void validateResponses(final TestBot bot, final String... responses) {
        final int length = responses == null ? 0 : responses.length;
        waitForResponses(bot, length);
        for (final String response : responses) {
            Assert.assertEquals(bot.getOldestResponse().getMessage(), response);
        }
    }
}