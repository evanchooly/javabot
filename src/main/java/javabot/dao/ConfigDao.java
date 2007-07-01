package javabot.dao;

import javabot.model.Config;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public interface ConfigDao {
    String GET_CONFIG = "Config.get";

    Config get();

    void save(Config config);
}
