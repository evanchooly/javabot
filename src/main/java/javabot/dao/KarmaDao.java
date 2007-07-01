package javabot.dao;

import java.util.List;

import javabot.dao.util.QueryParam;
import javabot.model.Karma;

public interface KarmaDao {
    String ALL = "Karma.all";
    String COUNT = "Karma.count";
    String BY_NAME = "Karma.byName";

    boolean hasKarma(String key);

    void addKarma(String sender, String key, Integer value);

    Karma getKarma(String nick);

    Karma get(Long id);

    List<Karma> findAll();

    List<Karma> getKarmas(QueryParam qp);

    Long getCount();

    void updateKarma(Karma karma);

}