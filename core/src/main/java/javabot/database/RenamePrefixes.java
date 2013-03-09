package javabot.database;

import java.io.IOException;
import java.sql.SQLException;

import com.antwerkz.maven.SPI;

@SPI(UpgradeScript.class)
public class RenamePrefixes extends UpgradeScript {
    @Override
    public int id() {
        return 1;
    }

    @Override
    public void doUpgrade() throws SQLException, IOException {
    }
}
