package javabot.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.antwerkz.maven.SPI;
import javabot.Javabot;

@SPI(UpgradeScript.class)
public class UpdateIndexes extends UpgradeScript {
    @Override
    public int id() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void registerUpgrade(final Connection connection) {
    }

    @Override
    public void doUpgrade(final Javabot bot, final Connection connection) throws SQLException, IOException {
        final Statement statement = connection.createStatement();
        try {
            final ResultSet resultSet = connection.getMetaData().getTables(null, null, null, new String[]{"TABLE"});
            while (resultSet.next()) {
                final String table = resultSet.getString("TABLE_NAME");
                final Map<String, String> indexes = loadIndexes(table);
                if (!indexes.isEmpty()) {
                    final ResultSet indexRS = connection.getMetaData().getIndexInfo(null, null, table, false, false);
                    while (indexRS.next()) {
                        indexes.remove(indexRS.getString("INDEX_NAME"));
                    }
                    for (final String sql : indexes.values()) {
                        statement.execute(sql);
                    }
                }
            }
        } finally {
            statement.close();
        }
    }

    private Map<String, String> loadIndexes(final String table) throws IOException {
        final InputStream stream = getClass().getResourceAsStream("/postgresql/" + table + ".indexes");
        final HashMap<String, String> map = new HashMap<String, String>();
        if (stream != null) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            while(reader.ready()) {
                final String[] entry = reader.readLine().split(":");
                map.put(entry[0], entry[1]);
            }
            stream.close();
        }
        return map;
    }
}