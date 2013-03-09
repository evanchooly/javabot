package javabot.dao;

import javabot.javadoc.Api;
import javabot.javadoc.criteria.ApiCriteria;

public class ApiDao extends BaseDao<Api> {
  protected ApiDao() {
    super(Api.class);
  }

  public Api find(final String name) {
    ApiCriteria criteria = new ApiCriteria(ds);
    criteria.upperName().equal(name.toUpperCase());
    return criteria.query().get();
  }
}