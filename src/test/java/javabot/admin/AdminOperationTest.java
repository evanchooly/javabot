package javabot.admin;

import javabot.operations.BotOperation;
import org.testng.annotations.Test;

/**
 * Created Dec 21, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test
public class AdminOperationTest /*extends BaseOperationTest*/ {
//    @Override
    protected BotOperation createOperation() {
//        return new AdminOperation(getJavabot());
    return null;
    }

    public void addChannel() {
//        getTestBot().joinChannel("##javabot");
        System.out.println("oi!");
    }
}
