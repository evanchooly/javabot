package javabot.dao;

import javabot.model.Config;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public interface ConfigDao extends BaseDao {
    String GET_CONFIG = "Config.get";

    Config get();

    Config create();
}
