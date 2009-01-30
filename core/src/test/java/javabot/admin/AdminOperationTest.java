package javabot.admin;

import java.io.File;
import java.io.FileFilter;

import javabot.commands.Command;
import javabot.operations.AdminOperation;
import javabot.operations.BaseOperationTest;
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
        final File dir = new File("core/src/main/java/" + Command.class.getPackage().getName().replace(".", "/"));
        final File[] names = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File file) {
                final String name = file.getName();
                return name.endsWith(".java")
                    && !"Command.java".equals(name)
                    && !"OperationsCommand.java".equals(name);
            }
        });
        for (final File name : names) {
            final String command = name.getName().replace(".java", "");
            Assert.assertTrue(AdminOperation.COMMANDS.contains(command), "Should have found " + command);
        }
    }
}
