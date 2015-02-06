package javabot.dao;

import java.util.List;
import javax.inject.Inject;

import javabot.javadoc.JavadocApi;
import javabot.criteria.JavadocApiCriteria;
import org.bson.types.ObjectId;

public class ApiDao extends BaseDao<JavadocApi> {
  @Inject
  public JavadocClassDao classDao;

  protected ApiDao() {
    super(JavadocApi.class);
  }

  public JavadocApi find(final String name) {
    JavadocApiCriteria criteria = new JavadocApiCriteria(ds);
    criteria.upperName().equal(name.toUpperCase());
    return criteria.query().get();
  }

  public void delete(final JavadocApi api) {
    classDao.deleteFor(api);
    super.delete(api);
  }

  public void delete(final ObjectId id) {
    delete(find(id));
  }

  @Override
  public List<JavadocApi> findAll() {
    return ds.createQuery(JavadocApi.class).order("name").asList();
  }
}