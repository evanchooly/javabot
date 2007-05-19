package javabot.dao;

import javabot.dao.model.Logs;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

import java.util.List;

//

// Author: joed

// Date  : Apr 15, 2007
public class LogDaoTest extends BaseServiceTest {

    @SpringBeanByType
    private LogDao logDao;

    @Test
    public void addLogMessage() {
        logDao.logMessage("nick", "#test", "test");
        Logs log = logDao.getMessage("nick", "test");
        Assert.assertEquals(log.getMessage(), "test");
    }

    @Test
    public void findChannels(){
        List channels = logDao.loggedChannels();
        Assert.assertEquals("#test",channels.get(0));

    }



}