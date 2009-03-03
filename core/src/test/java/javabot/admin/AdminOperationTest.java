package javabot.admin;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

import javabot.Javabot;
import javabot.commands.Command;
import javabot.operations.AdminOperation;
import javabot.operations.BaseOperationTest;
import javabot.operations.BotOperation;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created Dec 21, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test
public class AdminOperationTest extends BaseOperationTest {
    public void commandList() {
        doScan(Command.class, AdminOperation.COMMANDS, Arrays.asList("Command.java", "OperationsCommand.java"),
            ".java");
    }

    public void operationsList() {
        doScan(BotOperation.class, Javabot.OPERATIONS,
            Arrays.asList("AdminOperation.java", "AddFactoidOperation.java", "BotOperation.java",
                "GetFactoidOperation.java", "ForgetFactoidOperation.java", "UrlOperation.java"), "Operation.java");
    }

    private void doScan(final Class baseClass, final List<String> list, final List<String> excluded,
        final String endTest) {
        File dir = new File("core/src/main/java/" + baseClass.getPackage().getName().replace(".", "/"));
        if(!dir.exists()) {
            dir = new File("src/main/java/" + baseClass.getPackage().getName().replace(".", "/"));
        }
        final File[] names = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File file) {
                final String name = file.getName();
                return name.endsWith(endTest)
                    && !excluded.contains(name);
            }
        });
        for (final File name : names) {
            final String command = name.getName().replace(endTest, "");
            Assert.assertTrue(list.contains(command), "Should have found " + command);
        }
    }
}
