package javabot.database;

import java.io.IOException;
import java.sql.SQLException;

public class RenamePrefixes extends UpgradeScript {
    @Override
    public int id() {
        return 1;
    }

    @Override
    public void doUpgrade() throws SQLException, IOException {
    }
}
