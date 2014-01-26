package javabot.dao;

import javax.inject.Inject;

import javabot.BaseTest;
import javabot.model.Logs.Type;
import org.junit.Assert;
import org.testng.annotations.Test;

public class LogsDaoTest extends BaseTest {
  @Inject
  private LogsDao dao;

  @Test
  public void seen() {
    dao.logMessage(Type.MESSAGE, "ChattyCathy", "#watercooler", "test message");

    Assert.assertNotNull(dao.getSeen("chattycathy", "#watercooler"));
  }
}
