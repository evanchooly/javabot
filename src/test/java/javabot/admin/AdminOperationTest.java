package javabot.admin;

import javabot.operations.AdminOperation;
import javabot.operations.BaseOperationTest;
import javabot.operations.BotOperation;
import org.testng.annotations.Test;
import org.jibble.pircbot.PircBot;

/**
 * Created Dec 21, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test
public class AdminOperationTest extends BaseOperationTest {
    @Override
    protected BotOperation createOperation() {
        return new AdminOperation(getJavabot());
    }

    public void addChannel() {
        final PircBot bot = getTestBot();
        bot.sendMessage(CHANNEL, "~hi");
    }
}
