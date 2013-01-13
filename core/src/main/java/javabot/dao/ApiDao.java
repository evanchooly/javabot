package javabot.dao;

import javabot.javadoc.Api;

public class ApiDao extends BaseDao<Api> {
    protected ApiDao() {
        super(Api.class);
    }

    public Api find(final String name) {
        return getQuery().filter("lower(name) = ", name.toLowerCase()).get();
    }
}