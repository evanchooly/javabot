package javabot.dao;

import javax.inject.Inject;

import javabot.javadoc.JavadocApi;
import javabot.javadoc.criteria.JavadocApiCriteria;

public class ApiDao extends BaseDao<JavadocApi> {
  @Inject
  public JavadocClassDao classDao;

  protected ApiDao() {
    super(JavadocApi.class);
  }

  public JavadocApi find(final String name) {
    JavadocApiCriteria criteria = new JavadocApiCriteria(ds);
    criteria.upperName().equal(name.toUpperCase());
    JavadocApi javadocApi = criteria.query().get();
    return javadocApi;
  }

  public void delete(final JavadocApi api) {
    classDao.deleteFor(api);
    super.delete(api);
  }
}