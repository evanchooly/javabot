package javabot.dao;

import java.util.List;

import javabot.javadoc.Api;

/**
 * Created Oct 29, 2008
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public interface ApiDao extends BaseDao<Api> {
    String FIND_BY_NAME = "Api.findByName";
    String LIST_NAMES = "Api.listNames";

    Api find(String name);

    List<String> listNames();
}
