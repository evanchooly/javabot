package javabot.dao;

import javabot.dao.model.factoids;
import javabot.dao.model.logs;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;
//

// Author: joed

// Date  : Apr 15, 2007
public class LogDaoTest extends BaseServiceTest {

    @SpringBeanByType
    private LogDao logDao;

    @Test
    public void addLogMessage() {
       logDao.logMessage("nick","test","test");

        logs log = logDao.getMessage("nick","test");

        Assert.assertEquals(log.getMessage(),"test");
    }


}