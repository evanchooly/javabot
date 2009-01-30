package javabot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FactoidDaoTest extends BaseServiceTest {
    @Autowired
    private FactoidDao factoidDao;

    @Test(groups = {"operations"})
    public void testInsertfactoid() {
        factoidDao.addFactoid("joed2", "test2", "##javabot");
        Assert.assertTrue(factoidDao.hasFactoid("test2"));
        factoidDao.delete("joed2", "test2");
        //Assert.assertFalse(factoidDao.hasFactoid("test2"));
    }

    @Test(groups = {"operations"})
    public void countFactoids() {
        String key = "test factoid";
        String value = "test value";
        Long count = factoidDao.count();
        factoidDao.addFactoid("cheeser", key, value);
        Long count2 = factoidDao.count();
        Assert.assertNotSame(count, count2, "Not the same");
        factoidDao.delete("cheeser", key);
    }
}