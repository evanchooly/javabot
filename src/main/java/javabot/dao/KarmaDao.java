package javabot.dao;

import javabot.dao.model.Karma;
import javabot.dao.util.QueryParam;

import java.util.Iterator;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:02 PM

//
public interface KarmaDao {

    boolean hasKarma(String key);

    void addKarma(String sender, String key, Integer value, ChangesDao c_dao, String htmlFile);

    Karma getKarma(String nick);

    Karma get(Long id);

    Iterator<Karma> getIterator();

    Iterator<Karma> getKarmas(QueryParam qp);

    Long getCount();

    void updateKarma(Karma karma, ChangesDao c_dao, String htmlFile);

}