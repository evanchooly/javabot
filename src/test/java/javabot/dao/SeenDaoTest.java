package javabot.dao;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.spring.annotation.SpringBeanByType;

//

// Author: joed

// Date  : Apr 15, 2007
public class SeenDaoTest extends BaseServiceTest {

    @SpringBeanByType
    private SeenDao seenDao;

    @Test
    public void addSeen() {
        seenDao.logSeen("nick", "channel", "message");
        Assert.assertTrue(seenDao.isSeen("nick", "channel"));
    }


}