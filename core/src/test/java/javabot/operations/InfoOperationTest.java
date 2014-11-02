package javabot.operations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.inject.Inject;
import javabot.BaseMessagingTest;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.testng.annotations.Test;

public class InfoOperationTest extends BaseMessagingTest {
  @Inject
  private FactoidDao factoidDao;

  @Test
  public void info() {
    Factoid factoid = null;
    String key = "whatwhat";
    String value = "ah, yeah";
    String user = "test";
    try {
      LocalDateTime now = LocalDateTime.now();
      factoid = factoidDao.addFactoid(user, key, value);
      factoid.setUpdated(now);
      factoidDao.save(factoid);
      testMessage("~info " + key, String.format("%s was added by: %s on %s and has a literal value of: %s", key, user,
          now.format(DateTimeFormatter.ofPattern(InfoOperation.INFO_DATE_FORMAT)), value));
    } finally {
      factoid = factoidDao.getFactoid(key);
      if (factoid != null) {
        factoidDao.delete(factoid);
      }
    }
  }
}
