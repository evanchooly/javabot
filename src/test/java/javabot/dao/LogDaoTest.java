package javabot.dao;

import java.util.Date;
import java.util.List;

import javabot.model.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LogDaoTest extends BaseServiceTest {
    @Autowired
    private LogsDao logsDao;

    @Test
    public void addLogMessage() {
        final String nick = System.currentTimeMillis() + "test";
        logsDao.logMessage(Logs.Type.MESSAGE, nick, "##javabot", "test");
        final Logs log = logsDao.getMessage(nick, "##javabot");
        Assert.assertEquals(log.getMessage(), "test");
    }

    @Test(dependsOnMethods = {"addLogMessage"})
    public void findChannels() {
        final String nick = System.currentTimeMillis() + "test";
        logsDao.logMessage(Logs.Type.MESSAGE, nick, "##javabot", "test");
        final List channels = logsDao.loggedChannels();
        Assert.assertEquals(channels.get(0), "##javabot");
    }

    @Test(dependsOnMethods = {"addLogMessage"})
    public void getDailyLog() {
        final List<Logs> logdata = logsDao.dailyLog("##javabot", new Date());
        Assert.assertFalse(logdata.isEmpty(), "Should have log data");
    }
}