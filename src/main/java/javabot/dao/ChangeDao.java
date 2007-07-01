package javabot.dao;

import java.util.List;

import javabot.dao.util.QueryParam;
import javabot.model.Change;

public interface ChangeDao {

    void logAdd(String sender, String key, String value);

    void logChange(String message);

    public boolean findLog(String message);

    List<Change> getChanges(QueryParam qp, Change filter);

    Long count(Change filter);

    Change find(Long id);
}
