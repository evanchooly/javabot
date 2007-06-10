package javabot.dao;

import javabot.dao.model.Factoid;
import javabot.dao.util.QueryParam;

import java.util.Iterator;
import java.util.List;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:02 PM

// 
public interface FactoidDao {

    boolean hasFactoid(String key);

    void addFactoid(String sender, String key, String value, ChangesDao c_dao, String htmlFile);

    void forgetFactoid(String sender, String key, ChangesDao c_dao, String htmlFile);

    Factoid getFactoid(String key);

    Factoid get(Long id);

    Long getNumberOfFactoids();

    List<Factoid> getFactoids();

    Iterator<Factoid> getIterator();

    Iterator<Factoid> getFactoids(QueryParam qp);

    void updateFactoid(Factoid factoid, ChangesDao c_dao, String htmlFile);


}
