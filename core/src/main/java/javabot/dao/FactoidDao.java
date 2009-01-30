package javabot.dao;

import java.util.List;

import javabot.dao.util.QueryParam;
import javabot.model.Factoid;

@SuppressWarnings({"ConstantNamingConvention"})
public interface FactoidDao extends BaseDao<Factoid> {
    String ALL = "Factoid.all";
    String COUNT = "Factoid.count";
    String BY_NAME = "Factoid.byName";

    boolean hasFactoid(String key);

    void addFactoid(String sender, String key, String value);

    void delete(String sender, String key);

    Factoid getFactoid(String key);

    Factoid find(Long id);

    Long count();

    List<Factoid> getFactoids();

    List<Factoid> find(QueryParam qp);

    List<Factoid> getFactoidsFiltered(QueryParam qp, Factoid filter);

    Long factoidCountFiltered(Factoid filter);

}
