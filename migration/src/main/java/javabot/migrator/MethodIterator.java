package javabot.migrator;

import java.sql.ResultSet;
import java.sql.SQLException;

import javabot.dao.JavadocClassDao;
import javabot.javadoc.JavadocClass;
import javabot.javadoc.JavadocMethod;
import org.bson.types.ObjectId;

class MethodIterator extends TableIterator {
  private final Migrator migrator;

  private final JavadocClassDao javadocClassDao;

  public MethodIterator(final Migrator migrator, final JavadocClassDao javadocClassDao) throws SQLException {
    super(migrator, "methods");
    this.migrator = migrator;
    this.javadocClassDao = javadocClassDao;
  }

  public void migrate(final ResultSet resultSet) throws SQLException {
    ObjectId classId = migrator.lookupId("classes", resultSet.getLong("clazz_id"));
    JavadocClass javadocClass = javadocClassDao.find(classId);

    JavadocMethod method = new JavadocMethod();
    method.setId(migrator.mapId("methods", resultSet.getLong("id")));
    method.setLongUrl(resultSet.getString("longurl"));
    method.setShortUrl(resultSet.getString("shorturl"));
    method.setLongSignatureTypes(resultSet.getString("longsignaturestripped"));
    method.setName(resultSet.getString("methodname"));
    method.setParamCount(resultSet.getInt("paramcount"));
    method.setShortSignatureTypes(resultSet.getString("shortsignaturestripped"));
    method.setDirectUrl(resultSet.getString("directurl"));
    method.setJavadocClassId(javadocClass);
    method.setApiId(javadocClass.getApiId());
    javadocClassDao.save(method);
  }
}
