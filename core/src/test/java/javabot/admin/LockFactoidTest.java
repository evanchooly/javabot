package javabot.admin;

import com.antwerkz.sofia.Sofia;
import javabot.operations.BaseOperationTest;
import org.pircbotx.User;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class LockFactoidTest extends BaseOperationTest {
    @DataProvider(name = "factoids")
    public String[][] names() {
        return new String[][]{{"lock me"}, {"lockme"},};
    }

    @Test(dataProvider = "factoids")
    public void lock(final String name) {
        try {
            sendMessage("~forget " + name).get();
            testMessage("~" + name + " is i should be locked", "OK, " + getTestUser().getNick() + ".");
            testMessage("~admin lock " + name, name + " locked.");

            final User bob = registerIrcUser("bob", "bob", "localhost");
            testMessageAs(bob, String.format("~forget %s", name), Sofia.factoidDeleteLocked(bob.getNick()));

            testMessage("~admin unlock " + name, name + " unlocked.");
            testMessageAs(bob, String.format("~forget %s", name),Sofia.factoidForgotten(name, bob.getNick()));

            testMessage("~" + name + " is i should be locked", "OK, " + getTestUser().getNick() + ".");
            testMessage("~admin lock " + name, name + " locked.");
            testMessage(String.format("~forget %s", name), Sofia.factoidForgotten(name, getTestUser().getNick()));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sendMessage("~forget " + name).get();
        }
    }
}
