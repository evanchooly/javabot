package javabot.dao;

import javabot.model.Persistent;

/**
 * Created Jul 6, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public interface BaseDao<T> {
    void save(Persistent persistent);

    public void delete(Long id);
}
