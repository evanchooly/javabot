package javabot.dao;

import java.util.Iterator;
import java.util.List;

import javabot.model.Factoid;
import javabot.dao.util.QueryParam;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:02 PM

// 
public interface FactoidDao {

    boolean hasFactoid(String key);

    void addFactoid(String sender, String key, String value, ChangesDao changesDao);

    void forgetFactoid(String sender, String key, ChangesDao changesDao);

    Factoid getFactoid(String key);

    Factoid get(Long id);

    Long getNumberOfFactoids();

    List<Factoid> getFactoids();

    Iterator<Factoid> getIterator();

    Iterator<Factoid> getFactoids(QueryParam qp);

    void updateFactoid(Factoid factoid, ChangesDao changesDao);

    Iterator<Factoid> getFactoidsFiltered(QueryParam qp, Factoid filter);

    Long factoidCountFiltered(Factoid filter);

}
