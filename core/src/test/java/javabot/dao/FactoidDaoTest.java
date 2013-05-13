package javabot.dao;

import javax.inject.Inject;

import javabot.model.Factoid;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FactoidDaoTest extends BaseServiceTest {
  @Inject
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
    final String key = "test factoid";
    final String value = "test value";
    final Long count = factoidDao.count();
    factoidDao.addFactoid("cheeser", key, value);
    final Long count2 = factoidDao.count();
    Assert.assertNotSame(count, count2, "Not the same");
    factoidDao.delete("cheeser", key);
  }

  @Test
  public void testLastUsed() {
    factoidDao.delete("cheeser", "testing last used");
    final Factoid factoid = factoidDao.addFactoid("cheeser", "testing last used", "'sup?");
    Assert.assertNotNull(factoid.getLastUsed(), "Should have recorded a date");
    final Factoid factoid1 = factoidDao.getFactoid("testing last used");
    Assert.assertNotSame(factoid.getLastUsed(), factoid1.getLastUsed(), "Should have a new lastUsed value");
  }

}