package javabot.operations;

import javabot.BaseTest;
import javabot.dao.ChangeDao;
import javabot.dao.FactoidDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

@Test(groups = {"operations"})
public class ForgetFactoidOperationTest extends BaseOperationTest {
    @Autowired
    private FactoidDao factoidDao;
    @Autowired
    private ChangeDao changeDao;

    public void forgetFactoid() {
        if (!factoidDao.hasFactoid("afky")) {
            factoidDao.addFactoid(TEST_USER.getNick(), "afky", "test");
        }
        testMessage("~forget afky", "I forgot about afky, " + BaseTest.TEST_USER + ".");
    }

    public void nonexistantFactoid() {
        testMessage("~forget asdfghjkl",
            String.format("I never knew about asdfghjkl anyway, %s.", BaseTest.TEST_USER));
    }
}