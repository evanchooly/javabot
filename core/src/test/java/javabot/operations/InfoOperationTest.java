package javabot.operations;

import com.google.inject.Inject;
import javabot.dao.FactoidDao;
import javabot.model.Factoid;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

public class InfoOperationTest extends BaseOperationTest {
  @Inject
  private FactoidDao factoidDao;

  @Test
  public void info() {
    Factoid factoid = null;
    String key = "whatwhat";
    String value = "ah, yeah";
    String user = "test";
    try {
      DateTime now = DateTime.now();
      factoid = factoidDao.addFactoid(user, key, value);
      factoid.setUpdated(now);
      factoidDao.save(factoid);
      testMessage("~info " + key,
          String.format("%s was added by: %s on %s and has a literal value of: %s", key, user,
              now.toString(InfoOperation.INFO_DATE_FORMAT), value));
    } finally {
      factoid = factoidDao.getFactoid(key);
      if (factoid != null) {
        factoidDao.delete(factoid);
      }
    }
  }
}
