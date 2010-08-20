package javabot.operations;

import org.testng.annotations.Test;

@Test
public class SeenOperationTest extends BaseOperationTest {
    public void stringCase() {
        final TestBot testBot = getTestBot();
        String nick = testBot.getNick();
        send("MixedCase");
        send("lowercase");
        send("UPPERCASE");
        changeNick(nick);
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
        getTestBot().changeNick(nick);
    }
}