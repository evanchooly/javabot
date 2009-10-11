package javabot.operations;

import java.util.List;

import javabot.BaseTest;
import org.testng.Assert;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringApplicationContext("classpath:test-application-config.xml")
public abstract class BaseOperationTest extends BaseTest /*extends UnitilsTestNG*/ {
    private static final Logger log = LoggerFactory.getLogger(BaseOperationTest.class);
    
    protected void testMessageList(final String message, final List<String> responses) {
        final TestBot bot = getTestBot();
        bot.sendMessage(getJavabotChannel(), String.format("%s %s", getJavabot().getNick(), message));
        waitForResponses(bot, 1);
        final String response = bot.getOldestResponse().getMessage();
        Assert.assertTrue(responses.contains(response));
    }

    protected void testMessage(final String message, final String... responses) {
        final TestBot bot = getTestBot();
        bot.sendMessage(getJavabotChannel(), String.format("%s %s", getJavabot().getNick(), message));
        validateResponses(bot, responses);
    }

    protected void validateResponses(final TestBot bot, final String... responses) {
        final int length = responses == null ? 0 : responses.length;
        waitForResponses(bot, length);
        for (final String response : responses) {
            Assert.assertEquals(bot.getOldestResponse().getMessage(), response);
        }
    }

    protected void waitForResponses(final TestBot bot, final int length) {
        int count = 10;
        while(length != 0 && count != 0 && bot.getResponseCount() != length) {
            try {
                Thread.sleep(1000);
                count--;
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        Assert.assertEquals(bot.getResponseCount(), length);
    }

    protected void waitForResponse(final String response) {
        final long now = System.currentTimeMillis();
        while (!response.equals(getTestBot().getOldestMessage()) && System.currentTimeMillis() - now < 300000) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    protected void testChannelMessage(final String message, final String... responses) {
        final TestBot bot = getTestBot();
        bot.sendMessage(getJavabotChannel(), message);
        validateResponses(bot, responses);
    }

    public String getForgetMessage(final String factoid) {
        return String.format("I forgot about %s, %s.", factoid, getTestBot().getNick());
    }

    protected String getFoundMessage(final String factoid, final String value) {
        return String.format("%s, %s is %s", getTestBot().getNick(), factoid, value);
    }

    protected void forgetFactoid(final String name) {
        testMessage(String.format("forget %s", name), getForgetMessage(name));
    }

    protected void scanForResponse(final String message, final String target) {
        final TestBot bot = getTestBot();
        bot.sendMessage(getJavabotChannel(), String.format("%s %s", getJavabot().getNick(), message));
        waitForResponses(bot, 1);
        final String response = bot.getOldestResponse().getMessage();
        Assert.assertTrue(response.contains(target),
            String.format("Should have found '%s' in '%s' in response to '%s'", target, response, message));
    }
}