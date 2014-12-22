package javabot.database;

import javabot.dao.ConfigDao;
import javabot.model.Config;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;

public abstract class UpgradeScript {
    @Inject
    private ConfigDao configDao;

    public final void execute() {
        final Config config = configDao.get();
        if (config.getSchemaVersion() < id()) {
            try {
                doUpgrade();
                registerUpgrade();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    public void registerUpgrade() {
        final Config config = configDao.get();
        config.setSchemaVersion(id());
        configDao.save(config);
    }

    public abstract int id();

    public abstract void doUpgrade() throws SQLException, IOException;

    public final String toString() {
        return getClass().getName() + ":" + id();
    }
}
