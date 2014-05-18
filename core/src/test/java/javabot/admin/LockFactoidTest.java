package javabot.admin;

import javabot.model.IrcUser;
import javabot.operations.BaseOperationTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class LockFactoidTest extends BaseOperationTest {
    @DataProvider(name = "factoids")
    public String[][] names() {
        return new String[][] {{"lock me"}, {"lockme"}, };
    }

    @Test(dataProvider = "factoids")
    public void lock(final String name) {
        try {
            sendMessage("~forget " + name);
            testMessage("~" + name + " is i should be locked", "OK, " + TEST_USER + ".");
            testMessage("~admin lock " + name, name + " locked.");
            final IrcUser bob = registerIrcUser("bob", "bob", "localhost");
            testMessageAs(bob, String.format("~forget %s", name), "Only admins can delete locked factoids, bob.");
            testMessage("~admin unlock " + name, name + " unlocked.");
            testMessageAs(bob, String.format("~forget %s", name), getForgetMessage(bob, name));

            testMessage("~" + name + " is i should be locked", "OK, " + TEST_USER + ".");
            testMessage("~admin lock " + name, name + " locked.");
            testMessage(String.format("~forget %s", name), getForgetMessage(name));

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            sendMessage("~forget " + name);
        }
    }

    public void spaces() {

    }
}
