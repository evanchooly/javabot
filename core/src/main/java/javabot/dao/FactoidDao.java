package javabot.dao;

import java.util.Date;
import java.util.List;

import com.google.code.morphia.query.Query;
import javabot.dao.util.QueryParam;
import javabot.model.Factoid;
import javabot.model.Persistent;
import javabot.model.criteria.FactoidCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings({"ConstantNamingConvention"})
public class FactoidDao extends BaseDao<Factoid> {
  private static final Logger log = LoggerFactory.getLogger(FactoidDao.class);
  //    @Autowired
  private ChangeDao changeDao;
  //    @Autowired
  private ConfigDao dao;

  public FactoidDao() {
    super(Factoid.class);
  }

  public List<Factoid> find(final QueryParam qp) {
    Query<Factoid> query = ds.createQuery(Factoid.class);
    if (qp.hasSort()) {
      query.order(qp.getSort() + (qp.isSortAsc() ? " 1" : " -1"));
    }
    query.offset(qp.getFirst());
    query.limit(qp.getCount());
    return query.asList();
  }

  public void save(final Persistent persistent) {
    Factoid factoid = (Factoid) persistent;
    super.save(persistent);
    changeDao.logChange(String.format("%s changed '%s' to '%s'",
        factoid.getUserName(), factoid.getName(), factoid.getValue()));
  }

  public boolean hasFactoid(final String key) {
    FactoidCriteria criteria = new FactoidCriteria(ds);
    criteria.upperName().equal(key.toUpperCase());
    return criteria.query().get() != null;
  }

  @Transactional
  public Factoid addFactoid(final String sender, final String key, final String value) {
    final Factoid factoid = new Factoid();
    factoid.setId(factoid.getId());
    factoid.setName(key);
    factoid.setValue(value);
    factoid.setUserName(sender);
    factoid.setUpdated(new Date());
    factoid.setLastUsed(new Date());
    save(factoid);
    changeDao.logAdd(sender, key, value);
    return factoid;
  }

  public void delete(final String sender, final String key) {
    final Factoid factoid = getFactoid(key);
    if (factoid != null) {
      delete(factoid.getId());
      changeDao.logChange(String.format("%s remove '%s' with a value of '%s'", sender, key, factoid.getValue()));
    }
  }

  public Factoid getFactoid(final String name) {
    FactoidCriteria criteria = new FactoidCriteria(ds);
    criteria.upperName().equal(name.toUpperCase());
    Factoid factoid = criteria.query().get();
    if (factoid != null) {
      factoid.setLastUsed(new Date());
      super.save(factoid);
    }
    return factoid;
  }

  public Factoid getParameterizedFactoid(final String name) {
    FactoidCriteria criteria = new FactoidCriteria(ds);
    criteria.or(
        criteria.upperName().equal(name.toUpperCase() + " $1"),
        criteria.upperName().equal(name.toUpperCase() + " $^"),
        criteria.upperName().equal(name.toUpperCase() + " $+")
    );
    Factoid factoid = criteria.query().get();
    if (factoid != null) {
      factoid.setLastUsed(new Date());
      super.save(factoid);
    }
    return factoid;
  }

  public Long count() {
    return ds.createQuery(Factoid.class).countAll();
  }

  public Long factoidCountFiltered(final Factoid filter) {
    return (Long) buildFindQuery(null, filter, true).countAll();
  }

  public List<Factoid> getFactoidsFiltered(final QueryParam qp, final Factoid filter) {
    return buildFindQuery(qp, filter, false).asList();
  }

  private Query<Factoid> buildFindQuery(final QueryParam qp, final Factoid filter, final boolean count) {
    FactoidCriteria criteria = new FactoidCriteria(ds);
    if (filter.getName() != null) {
      criteria.upperName().contains(filter.getName().toUpperCase());
    }
    if (filter.getUserName() != null) {
      criteria.upperUserName().contains(filter.getUserName().toUpperCase());
    }
    if (filter.getValue() != null) {
      criteria.value().contains(filter.getValue().toUpperCase());
    }
    if (!count && qp != null && qp.hasSort()) {
      criteria.query().order("upper" + qp.getSort() + (qp.isSortAsc() ? " 1" : " -1"));
      criteria.query().offset(qp.getFirst());
      criteria.query().limit(qp.getCount());
    }
    return criteria.query();
  }
}
