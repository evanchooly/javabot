package javabot.dao;

import javabot.dao.model.factoids;

import java.util.List;

import org.jdom.Element;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:02 PM

// 
public interface FactoidDao {

    boolean hasFactoid(String key);

    void addFactoid(String sender, String key, String value, ChangesDao c_dao, String htmlFile);

    void forgetFactoid(String sender, String key, ChangesDao c_dao, String htmlFile);

    factoids getFactoid(String key);

    Long getNumberOfFactoids();

    List<factoids> getFactoids();

    void updateFactoid(factoids factoid,ChangesDao c_dao, String htmlFile);


}
