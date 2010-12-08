package javabot.operations;

import javabot.BaseTest;
import org.testng.annotations.Test;

@Test
public class SeenOperationTest extends BaseOperationTest {
    public void stringCase() {
        send("MixedCase");
        send("lowercase");
        send("UPPERCASE");
        changeNick(BaseTest.TEST_USER.getNick());
    }

    private void lookFor(final Object nick) {
        scanForResponse(String.format("~seen %s", nick), String.format("%s was last seen at", nick));
    }

    private void send(final String nick) {
        changeNick(nick);
        final String message = String.format("arr!  my nick be %s now!", nick);
        testMessage(message);
        lookFor(nick);
    }

    private void changeNick(final String nick) {
//        getTestBot().changeNick(nick);
    }
}