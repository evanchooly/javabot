package javabot.admin;

import javabot.operations.BaseOperationTest;
import org.testng.annotations.Test;

@Test
public class LockFactoidTest extends BaseOperationTest {
    public void lock() {
        try {
            testMessage("~lockme is i should be locked", "OK, " + TEST_USER + ".");
            testMessage("~admin lockFactoid lockme", "lockme locked.");
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            testMessage("~forget lockme", "I forgot about lockme, " + TEST_USER + ".");
        }
    }
}
