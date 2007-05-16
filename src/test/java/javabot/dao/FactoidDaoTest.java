package javabot.dao;

import javabot.dao.model.Factoid;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

//

// Author: joed

// Date  : Apr 15, 2007
public class FactoidDaoTest extends BaseServiceTest {


    @SpringBeanByType
    private FactoidDao factoidDao;

    @SpringBeanByType
    private ChangesDao changesDao;

    @Test(groups = {"operations"})
    public void testInsertfactoid() {
        factoidDao.addFactoid("joed2", "test2", "#test", changesDao, "test");
        Assert.assertTrue(factoidDao.hasFactoid("test2"));
       //factoidDao.forgetFactoid("joed2", "test2", changesDao, "test");
        //Assert.assertFalse(factoidDao.hasFactoid("test2"));
    }

    @Test(groups = {"operations"})
    public void addAndForgetFactoid() {
        String key = "test factoid";
        String value = "test value";
        Long count = factoidDao.getNumberOfFactoids();
        factoidDao.addFactoid("cheeser", key, value, changesDao, "test");
        Factoid factoid = factoidDao.getFactoid(key);
        Assert.assertEquals(value, factoid.getValue(), factoidDao.getClass().getName() + ":  The factoid value should have matched");
        Assert.assertNotSame("test value2", factoid.getValue(), factoidDao.getClass().getName() + ":  The factoid value should have matched");
        factoidDao.forgetFactoid("cheeser", key, changesDao, "test");
    }

    @Test(groups = {"operations"})
    public void countFactoids() {
        String key = "test factoid";
        String value = "test value";
        Long count = factoidDao.getNumberOfFactoids();
        factoidDao.addFactoid("cheeser", key, value, changesDao, "test");
        Assert.assertEquals((long) factoidDao.getNumberOfFactoids(), (Long) count + 1, "Should have only 1 more factoid");
        factoidDao.forgetFactoid("cheeser", key, changesDao, "test");
    }

}

