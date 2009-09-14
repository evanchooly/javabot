package javabot.operations;

import org.testng.annotations.Test;
import org.testng.Assert;

@Test(enabled = false)
public class TellOperationTest extends BaseOperationTest {

    public void shortcut() {
        final TestBot bot = getTestBot();
        final String nick = getJavabot().getNick();
        bot.sendMessage(getJavabotChannel(), String.format("%s shortcut is <reply>shortcut", nick));
        waitForResponses(bot, 1);
        bot.getOldestResponse();
        bot.sendMessage(getJavabotChannel(), String.format("~~ %s shortcut", getTestBot().getNick()));
        waitForResponses(bot, 1);
        Assert.assertEquals(bot.getOldestResponse().getMessage(), String.format("%s, shortcut", nick));

    }
}
