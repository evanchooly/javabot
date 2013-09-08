package javabot.migrator;

import java.sql.ResultSet;
import java.sql.SQLException;

import javabot.dao.JavadocClassDao;
import javabot.javadoc.JavadocClass;
import javabot.javadoc.JavadocField;
import org.bson.types.ObjectId;

public class FieldIterator extends TableIterator {
  private final JavadocClassDao javadocClassDao;

  public FieldIterator(final Migrator migrator, final JavadocClassDao javadocClassDao) throws SQLException {
    super(migrator, "fields", JavadocField.class);
    this.javadocClassDao = javadocClassDao;
  }

  public void migrate(final ResultSet resultSet) throws SQLException {
    ObjectId classId = migrator.lookupId("classes", resultSet.getLong("clazz_id"));
    JavadocClass javadocClass = javadocClassDao.find(classId);

    JavadocField field = new JavadocField();
    field.setId(migrator.mapId("fields", resultSet.getLong("id")));
    field.setDirectUrl(resultSet.getString("directurl"));
    field.setLongUrl(resultSet.getString("longurl"));
    field.setShortUrl(resultSet.getString("shorturl"));
    field.setName(resultSet.getString("name"));
    field.setType(resultSet.getString("type"));
    field.setJavadocClassId(javadocClass);
    field.setApiId(javadocClass.getApiId());
    javadocClassDao.save(field);
  }
}
