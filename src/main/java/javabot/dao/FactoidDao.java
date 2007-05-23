package javabot.dao;

import javabot.dao.model.Factoid;

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

    Long getNumberOfFactoids();

    List<Factoid> getFactoids();

    void updateFactoid(Factoid factoid, ChangesDao c_dao, String htmlFile);


}
