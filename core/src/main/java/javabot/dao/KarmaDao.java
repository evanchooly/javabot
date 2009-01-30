package javabot.dao;

import java.util.List;

import javabot.dao.util.QueryParam;
import javabot.model.Karma;
import org.springframework.transaction.annotation.Transactional;

public interface KarmaDao extends BaseDao<Karma> {
    String ALL = "Karma.all";
    String COUNT = "Karma.count";
    String BY_NAME = "Karma.byName";

    @Transactional
    Karma find(String nick);

    Karma find(Long id);

    List<Karma> findAll();

    List<Karma> getKarmas(QueryParam qp);

    Long getCount();

    @Transactional
    void save(Karma karma);
}