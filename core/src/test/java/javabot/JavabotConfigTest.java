package javabot;

import com.google.inject.Inject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JavabotConfigTest extends BaseTest {

    @Inject
    private JavabotConfig javabotConfig;

    @Test
    public void testConfig() {
        Assert.assertNotEquals(javabotConfig.nick(), "javabot");
        Assert.assertNotEquals(javabotConfig.databaseName(), "javabot");
    }
}