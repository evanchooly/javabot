package javabot.dao;

import java.util.List;
import javax.inject.Inject;

import com.antwerkz.sofia.Sofia;
import org.mongodb.morphia.query.Query;
import javabot.dao.util.QueryParam;
import javabot.model.Karma;
import javabot.model.criteria.KarmaCriteria;
import java.time.LocalDateTime;

public class KarmaDao extends BaseDao<Karma> {
  @Inject
  private ChangeDao changeDao;

  public KarmaDao() {
    super(Karma.class);

  }

  public List<Karma> getKarmas(QueryParam qp) {
    Query<Karma> query = ds.createQuery(Karma.class);
    if (qp.hasSort()) {
      query.order(qp.getSort() + (qp.isSortAsc() ? " 1" : " -1"));
    }
    query.offset(qp.getFirst());
    query.limit(qp.getCount());
    return query.asList();
  }

  public void save(Karma karma) {
    karma.setUpdated(LocalDateTime.now());
    super.save(karma);
    changeDao.logChange(Sofia.karmaChanged(karma.getUserName(), karma.getName(), karma.getValue()));
  }

    public Karma find(String name) {
    KarmaCriteria criteria = new KarmaCriteria(ds);
    criteria.upperName().equal(name.toUpperCase());
    return criteria.query().get();
  }

  public Long getCount() {
    return ds.createQuery(Karma.class).countAll();
  }
}