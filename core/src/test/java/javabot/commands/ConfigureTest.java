package javabot.commands;

import com.google.inject.Inject;
import com.jayway.awaitility.Awaitility;
import javabot.BaseMessagingTest;
import javabot.dao.ConfigDao;
import javabot.model.Config;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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

        Assert.assertEquals(new Integer(15), configDao.get().getThrottleThreshold());

        testMessage("~admin configure --property=throttleThreshold --value=10", format("Setting %s to %d", "throttleThreshold", 10));

        Assert.assertEquals(new Integer(10), configDao.get().getThrottleThreshold());
    }
}