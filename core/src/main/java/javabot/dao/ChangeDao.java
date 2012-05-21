package javabot.dao;

import java.util.List;

import javabot.dao.util.QueryParam;
import javabot.model.Change;
import org.springframework.transaction.annotation.Transactional;

public interface ChangeDao extends BaseDao {

    @Transactional
    void logAdd(String sender, String key, String value);

    @Transactional
    void logChange(String message);

    boolean findLog(String message);

    List<Change> getChanges(QueryParam qp, Change filter);

    Long count(Change filter);

    Change find(Long id);

    @SuppressWarnings({"unchecked"})
    List<Change> get(Change filter);
}
