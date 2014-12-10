package javabot.commands;

import com.google.inject.Inject;
import javabot.BaseMessagingTest;
import javabot.dao.ConfigDao;
import javabot.model.Config;
import org.testng.Assert;
import org.testng.annotations.Test;

import static java.lang.String.format;


@Test
public class ConfigureTest extends BaseMessagingTest {
    @Inject
    public ConfigDao configDao;

    public void change() {
        Config config = configDao.get();
        Integer throttleThreshold = config.getThrottleThreshold();
        Assert.assertNotNull(throttleThreshold);

        testMessage("~admin configure", config.toString());

        testMessage("~admin configure --property=throttleThreshold --value=15", format("Setting %s to %d", "throttleThreshold", 15));

        Assert.assertEquals(configDao.get().getThrottleThreshold(), new Integer(15));

        testMessage("~admin configure --property=throttleThreshold --value=10", format("Setting %s to %d", "throttleThreshold", 10));

        Assert.assertEquals(configDao.get().getThrottleThreshold(), new Integer(10));
    }
}