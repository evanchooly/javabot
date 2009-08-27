package javabot.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javabot.model.Config;
import javabot.model.Logs;
import javabot.model.Logs.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class LogDaoTest extends BaseServiceTest {
    @Autowired
    private LogsDao logsDao;
    @Autowired
    private ConfigDao dao;

    public void getDailyLog() {
        final List<Logs> logdata = logsDao.dailyLog(getJavabotChannel(), new Date());
        Assert.assertFalse(logdata.isEmpty(), "Should have log data");
    }

    public void logPruning() {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.YEAR, 2007);
        final Config config = dao.get();
        config.setHistoryLength(null);
        dao.save(config);
        final Calendar today = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        while(cal.getTimeInMillis() < today.getTimeInMillis()) {
            final Logs message = new Logs();
            message.setType(Type.MESSAGE);
            message.setNick(getTestBot().getNick());
            message.setChannel(getJavabotChannel());
            message.setMessage("test message " + cal.getTimeInMillis());
            message.setUpdated(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
            logsDao.save(message);
        }
        today.add(Calendar.MONTH, -1);
        today.add(Calendar.DAY_OF_YEAR, -1);
        Assert.assertFalse(logsDao.dailyLog(getJavabotChannel(), today.getTime()).isEmpty(), "History should be there");
        logsDao.pruneHistory();
        Assert.assertFalse(logsDao.dailyLog(getJavabotChannel(), today.getTime()).isEmpty(), "History should be there");
        config.setHistoryLength(1);
        dao.save(config);

        logsDao.pruneHistory();
        Assert.assertTrue(logsDao.dailyLog(getJavabotChannel(), today.getTime()).isEmpty(), "History should be gone");
    }
}