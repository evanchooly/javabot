package javabot.dao;

import javabot.model.Change;
import javabot.dao.util.QueryParam;

import java.util.Iterator;

// Author: joed
// Date  : Apr 12, 2007

public interface ChangesDao {

    void logAdd(String sender, String key, String value);

    void logChange(String message);

    public boolean findLog(String message);

    Iterator<Change> getChanges(QueryParam qp, Change filter);

    Long getNumberOfChanges(Change filter);

    Change get(Long id);
}
