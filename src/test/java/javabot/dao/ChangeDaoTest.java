package javabot.dao;

import javabot.model.Change;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

// Author: joed

// Date  : Apr 15, 2007
public class ChangeDaoTest extends BaseServiceTest {

    @SpringBeanByType
    private ChangesDao changeDao;

    @Test
    public void testFilterCount() {

        String testing = System.currentTimeMillis() + "test";
        Change change = new Change();
        changeDao.logAdd(testing, testing, testing);
        Assert.assertTrue(1 < changeDao.getNumberOfChanges(change).intValue());

    }

    @Test
    public void findFilteredCount() {
        String testing = System.currentTimeMillis() + "test";
        Change change = new Change();
        changeDao.logAdd(testing, testing, testing);
        change.setMessage(testing);
        Assert.assertEquals(1, (long) changeDao.getNumberOfChanges(change));
    }

}