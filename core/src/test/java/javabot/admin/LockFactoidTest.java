package javabot.admin;

import javabot.operations.BaseOperationTest;
import org.testng.annotations.Test;

@Test
public class LockFactoidTest extends BaseOperationTest {
    public void lock() {
        try {
            final String lockme = "lockme";
            sendMessage("~forget " + lockme);
            testMessage("~lockme is i should be locked", "OK, " + TEST_USER + ".");
            testMessage("~admin lock " + lockme, lockme + " locked.");
            testMessageAs("bob", String.format("~forget %s", lockme), "Only admins can delete locked factoids, bob.");
            testMessage("~admin unlock " + lockme, lockme + " unlocked.");
            testMessageAs("bob", String.format("~forget %s", lockme), getForgetMessage("bob", lockme));

            testMessage("~lockme is i should be locked", "OK, " + TEST_USER + ".");
            testMessage("~admin lock " + lockme, lockme + " locked.");
            testMessage(String.format("~forget %s", lockme), getForgetMessage(lockme));

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            sendMessage("~forget lockme");
        }
    }
}
