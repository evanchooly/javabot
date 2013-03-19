package javabot.dao;

import javax.inject.Inject;

import com.google.code.morphia.query.Query;
import javabot.javadoc.JavadocApi;
import javabot.javadoc.criteria.ApiCriteria;

public class ApiDao extends BaseDao<JavadocApi> {
  @Inject
  public JavadocClassDao classDao;

  protected ApiDao() {
    super(JavadocApi.class);
  }

  public JavadocApi find(final String name) {
    ApiCriteria criteria = new ApiCriteria(ds);
    criteria.upperName().equal(name.toUpperCase());
    Query<JavadocApi> query = criteria.query();
    return query.get();
  }

  public void delete(final JavadocApi api) {
    classDao.deleteFor(api);
    super.delete(api);
  }
}