package javabot.dao;

import javabot.model.Config;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created Jun 21, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">Justin Lee</a>
 */
public interface ConfigDao {
    String GET_CONFIG = "Config.get";

    Config get();

    @Transactional
    void save(Config config);
}
