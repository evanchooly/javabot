package javabot.dao;

import javabot.model.Config;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public interface ConfigDao extends BaseDao<Config> {
    String GET_CONFIG = "Config.get";

    Config get();
}
