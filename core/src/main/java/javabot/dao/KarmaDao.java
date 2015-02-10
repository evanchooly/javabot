package javabot.dao;

import com.antwerkz.sofia.Sofia;
import com.mongodb.WriteResult;
import javabot.dao.util.QueryParam;
import javabot.model.Karma;
import javabot.model.criteria.KarmaCriteria;
import org.mongodb.morphia.query.Query;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

public class KarmaDao extends BaseDao<Karma> {
    @Inject
    private ChangeDao changeDao;

    public KarmaDao() {
        super(Karma.class);

    }

    public List<Karma> getKarmas(QueryParam qp) {
        Query<Karma> query = ds.createQuery(Karma.class);
        if (qp.hasSort()) {
            query.order((qp.isSortAsc() ? "" : "-") + qp.getSort());
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

    public Long count() {
        return ds.createQuery(Karma.class).countAll();
    }

    public WriteResult deleteAll() {
        return ds.delete(ds.createQuery(Karma.class));
    }
}