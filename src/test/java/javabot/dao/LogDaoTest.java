package javabot.dao;

import java.util.Date;
import java.util.List;

import javabot.model.Logs;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

public class LogDaoTest extends BaseServiceTest {
    @SpringBeanByType
    private LogsDao logsDao;

    @Test
    public void addLogMessage() {
        String nick = System.currentTimeMillis() + "test";
        logsDao.logMessage(Logs.Type.MESSAGE, nick, "#test", "test");
        Logs log = logsDao.getMessage(nick, "#test");
        Assert.assertEquals(log.getMessage(), "test");
    }

    @Test
    public void findChannels() {
        String nick = System.currentTimeMillis() + "test";
        logsDao.logMessage(Logs.Type.MESSAGE, nick, "#test", "test");
        List channels = logsDao.loggedChannels();
        Assert.assertEquals("#test", channels.get(0));
    }

    @Test
    public void getDailyLog() {
        List<Logs> logdata = logsDao.dailyLog("#test", new Date());
        Assert.assertFalse(logdata.isEmpty(), "Should have log data");
    }
}