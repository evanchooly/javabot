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
        factoidDao.forgetFactoid("joed2", "test2", changesDao, "test");
        //Assert.assertFalse(factoidDao.hasFactoid("test2"));
    }

    @Test(groups = {"operations"})
    public void countFactoids() {
        String key = "test factoid";
        String value = "test value";
        final Long count = factoidDao.getNumberOfFactoids();

        factoidDao.addFactoid("cheeser", key, value, changesDao, "test");
        final Long count2 = factoidDao.getNumberOfFactoids();

        Assert.assertNotSame(count, count2,"Not the same");

        factoidDao.forgetFactoid("cheeser", key, changesDao, "test");
    }

}

