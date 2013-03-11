package javabot.dao;

import javax.inject.Inject;

import com.google.code.morphia.query.Query;
import javabot.javadoc.Api;
import javabot.javadoc.criteria.ApiCriteria;

public class ApiDao extends BaseDao<Api> {
  @Inject
  public ClazzDao classDao;

  protected ApiDao() {
    super(Api.class);
  }

  public Api find(final String name) {
    ApiCriteria criteria = new ApiCriteria(ds);
    criteria.upperName().equal(name.toUpperCase());
    Query<Api> query = criteria.query();
    return query.get();
  }

  public void delete(final Api api) {
    classDao.deleteFor(api);
    super.delete(api);
  }
}