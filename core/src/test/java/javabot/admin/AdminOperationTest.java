package javabot.admin;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

import javabot.Javabot;
import javabot.javadoc.Api;
import javabot.commands.Command;
import javabot.dao.ApiDao;
import javabot.operations.AdminOperation;
import javabot.operations.BaseOperationTest;
import javabot.operations.BotOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javadoc.JavadocParserTest;

/**
 * Created Dec 21, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
@Test
public class AdminOperationTest extends BaseOperationTest {
    private static final Logger log = LoggerFactory.getLogger(AdminOperationTest.class);
    
    @Autowired
    private ApiDao dao;

    public void commandList() {
        doScan(Command.class, AdminOperation.COMMANDS, Arrays.asList("Command.java", "OperationsCommand.java"),
            ".java");
    }

    public void addApi() {
        final Api api = dao.find(JavadocParserTest.API_NAME);
        if(api != null) {
            log.debug("deleting old api " + JavadocParserTest.API_NAME);
            dao.delete(api);
        }
        getTestBot().sendMessage(getJavabotChannel(), getJavabot().getNick() + " admin addApi " + JavadocParserTest.API_NAME + " "
            + JavadocParserTest.API_URL_STRING + " " + JavadocParserTest.PACKAGES);
        long start = System.currentTimeMillis();
        while(dao.find(JavadocParserTest.API_NAME) == null && System.currentTimeMillis() < start + 300000) {
            log.debug("waiting for javadoc");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
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
