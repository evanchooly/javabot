package javabot.dao;

import java.util.Date;
import java.util.List;

import javabot.model.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class LogDaoTest extends BaseServiceTest {
    @Autowired
    private LogsDao logsDao;

    public void findChannels() {
        final String nick = getTestBot().getNick();
        logsDao.logMessage(Logs.Type.MESSAGE, nick, getJavabotChannel(), "test");
        final List channels = logsDao.loggedChannels();
        Assert.assertEquals(channels.get(0), getJavabotChannel());
    }

    public void getDailyLog() {
        final List<Logs> logdata = logsDao.dailyLog(getJavabotChannel(), new Date());
        Assert.assertFalse(logdata.isEmpty(), "Should have log data");
    }
}