package javabot.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import com.antwerkz.maven.SPI;
import javabot.Javabot;
import org.springframework.beans.factory.annotation.Autowired;

@SPI(UpgradeScript.class)
public class BaseIndexes implements UpgradeScript {
    @Autowired
    private DataSource dataSource;
    private Map<String, String> indexes = new LinkedHashMap<String, String>();

    public BaseIndexes() {
//        indexes.put("Logs", "CREATE INDEX \"Logs\" ON logs USING btree (channel, updated, nick);");
//        indexes.put("names", "CREATE INDEX \"names\" ON factoids USING btree (name);");
    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public void execute(final Javabot bot) {
        try {
            bot.inject(this);
            final Connection connection = dataSource.getConnection();
            try {
                ResultSet resultSet = connection.getMetaData().getTables(null, null, null, new String[]{"TABLE"});
                final List<String> tables = new ArrayList<String>();
                while(resultSet.next()) {
                    tables.add(resultSet.getString("TABLE_NAME"));
                }
                for (final String table : tables) {
                    resultSet = connection.getMetaData().getIndexInfo(null, null, table, false, false);
                    while(resultSet.next()) {
                        final String name = resultSet.getString("INDEX_NAME");
                        indexes.remove(name);
                    }
                }
                final Statement statement = connection.createStatement();
                for (String s : indexes.values()) {
                    statement.execute(s);
                }
                System.out.println("hi");
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
