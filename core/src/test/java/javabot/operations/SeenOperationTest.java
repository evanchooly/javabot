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
        lookFor("MixedCase");
        lookFor("lowercase");
        lookFor("UPPERCASE");
    }

    private void lookFor(final Object nick) {
        scanForResponse(String.format("seen %s", nick), String.format("%s was last seen at", nick));
    }

    private void send(final String nick) {
        changeNick(nick);
        getTestBot().sendMessage(getJavabotChannel(), String.format("my nick is %s!", nick));
        sleep();
    }

    private void changeNick(final String nick) {
        getTestBot().changeNick(nick);
    }

    private void sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
