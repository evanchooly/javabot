package javabot.migrator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javabot.dao.ApiDao;
import javabot.dao.JavadocClassDao;
import javabot.javadoc.JavadocClass;
import org.bson.types.ObjectId;

public class ClassIterator extends TableIterator {
  private final List<Long> deferredList = new ArrayList<>();

  private final JavadocClassDao javadocClassDao;

  private ApiDao apiDao;

  public ClassIterator(final Migrator migrator, final JavadocClassDao javadocClassDao, final ApiDao apiDao)
      throws SQLException {
    super(migrator, "classes");
    this.javadocClassDao = javadocClassDao;
    this.apiDao = apiDao;
  }

  @Override
  public Object call() throws Exception {
    super.call();
    while (!deferredList.isEmpty()) {
      Iterator<Long> iterator = deferredList.iterator();
      while (iterator.hasNext()) {
        final Long aLong = iterator.next();
        if (process(aLong)) {
          iterator.remove();
        }
      }
    }
    return null;
  }

  private boolean process(Long id) {

    try (Connection connection = migrator.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(String.format("select superclass_id from"
             + " classes where id = " + id))) {
      if (resultSet.next()) {
        ObjectId parentId = migrator.lookupId("classes", resultSet.getLong("superclass_id"));
        if (parentId != null) {
          JavadocClass javadocClass = javadocClassDao.find(migrator.lookupId("classes", id));
          javadocClass.setSuperClassId(javadocClassDao.find(parentId));
          javadocClassDao.save(javadocClass);
          return true;
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    return false;
  }

  public void migrate(final ResultSet resultSet) throws SQLException {
    JavadocClass javadocClass = new JavadocClass();
    long id = resultSet.getLong("id");
    javadocClass.setId(migrator.mapId(table, id));
    javadocClass.setLongUrl(resultSet.getString("longurl"));
    javadocClass.setShortUrl(resultSet.getString("shorturl"));
    javadocClass.setName(resultSet.getString("classname"));
    javadocClass.setPackageName(resultSet.getString("packagename"));
    setSuperClassId(resultSet, javadocClass, id);
    ObjectId apiId = migrator.lookupId("apis", resultSet.getLong("api_id"));
    javadocClass.setApi(apiDao.find(apiId));
    javadocClassDao.save(javadocClass);
  }

  private void setSuperClassId(final ResultSet resultSet, final JavadocClass javadocClass, final long id)
      throws SQLException {
    long parentId = resultSet.getLong("superclass_id");
    if (!resultSet.wasNull()) {
      ObjectId objectId = migrator.lookupId(table, parentId);
      if (objectId == null) {
        defer(id);
      } else {
        javadocClass.setSuperClassId(javadocClassDao.find(objectId));
      }
    }
  }

  private void defer(final Long id) {
    deferredList.add(id);
  }
}
