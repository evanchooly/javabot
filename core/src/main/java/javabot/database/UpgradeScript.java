package javabot.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;
import javax.inject.Inject;
import javax.sql.DataSource;

import javabot.Javabot;
import javabot.dao.ConfigDao;
import javabot.model.Config;

public abstract class UpgradeScript {
  @Inject
  private DataSource dataSource;
  @Inject
  private ConfigDao configDao;

  public final void execute(final Javabot bot) {
    final Config config = configDao.get();
    if (config.getSchemaVersion() < id()) {
      Connection connection;
      try {
        connection = dataSource.getConnection();
        try {
          doUpgrade(bot, connection);
          registerUpgrade(connection);
        } finally {
          connection.close();
        }
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }
  }

  public void registerUpgrade(final Connection connection) {
    final Config config = configDao.get();
    config.setSchemaVersion(id());
    configDao.save(config);
  }

  public abstract int id();

  public abstract void doUpgrade(Javabot bot, Connection connection) throws SQLException, IOException;

  public static Set<UpgradeScript> loadScripts() {
    final Set<UpgradeScript> set = new TreeSet<UpgradeScript>(new ScriptComparator());
    for (final UpgradeScript script : ServiceLoader.load(UpgradeScript.class)) {
      set.add(script);
    }
    return set;
  }

  public final String toString() {
    return getClass().getName() + ":" + id();
  }

  static class ScriptComparator implements Comparator<UpgradeScript> {
    @Override
    public int compare(final UpgradeScript o1, final UpgradeScript o2) {
      if (o1 == o2) {
        return 0;
      }
      if (o1.id() < o2.id()) {
        return -1;
      }
      if (o1.id() > o2.id()) {
        return 1;
      }
      throw new RuntimeException(String.format("%s and %s have the same priority: %d",
          o1.getClass().getName(), o2.getClass().getName(), o1.id()));
    }
  }
}
