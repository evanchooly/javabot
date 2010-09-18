package javabot.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import com.antwerkz.maven.SPI;
import javabot.Javabot;
import org.springframework.beans.factory.annotation.Autowired;

@SPI(UpgradeScript.class)
public class BaseIndexes implements UpgradeScript {
    @Autowired
    private DataSource dataSource;

    @Override
    public int id() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void execute(final Javabot bot) {
        try {
            bot.inject(this);
            final Connection connection = dataSource.getConnection();
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
                connection.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
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
