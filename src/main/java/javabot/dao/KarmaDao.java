package javabot.dao;

import java.util.List;

import javabot.dao.util.QueryParam;
import javabot.model.Karma;

public interface KarmaDao {
    String ALL = "Karma.all";
    String COUNT = "Karma.count";
    String BY_NAME = "Karma.byName";

    Karma find(String nick);

    Karma find(Long id);

    List<Karma> findAll();

    List<Karma> getKarmas(QueryParam qp);

    Long getCount();

    void save(Karma karma);
}