package javabot.dao;

import com.mongodb.WriteResult;
import javabot.dao.util.QueryParam;
import javabot.model.Factoid;
import javabot.model.Persistent;
import javabot.model.criteria.FactoidCriteria;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings({"ConstantNamingConvention"})
public class FactoidDao extends BaseDao<Factoid> {
    private static final Logger log = LoggerFactory.getLogger(FactoidDao.class);

    @Inject
    private ChangeDao changeDao;

    @Inject
    private ConfigDao dao;

    public FactoidDao() {
        super(Factoid.class);
    }

    public void save(final Persistent persistent) {
        Factoid factoid = (Factoid) persistent;
        Factoid old = find(persistent.getId());
        super.save(persistent);
        if (old != null) {
            changeDao.logChange(String.format("%s changed '%s' from '%s' to '%s'",
                                              factoid.getUserName(), factoid.getName(), old.getValue(), factoid.getValue()));
        } else {
            changeDao.logChange(String.format("%s added '%s' with '%s'", factoid.getUserName(), factoid.getName(),
                                              factoid.getValue()));
        }
    }

    public boolean hasFactoid(final String key) {
        FactoidCriteria criteria = new FactoidCriteria(ds);
        criteria.upperName().equal(key.toUpperCase());
        return criteria.query().get() != null;
    }

    public Factoid addFactoid(final String sender, final String key, final String value) {
        final Factoid factoid = new Factoid();
        factoid.setId(factoid.getId());
        factoid.setName(key);
        factoid.setValue(value);
        factoid.setUserName(sender);
        factoid.setUpdated(LocalDateTime.now());
        factoid.setLastUsed(LocalDateTime.now());
        save(factoid);
        changeDao.logAdd(sender, key, value);
        return factoid;
    }

    public void delete(final String sender, final String key) {
        final Factoid factoid = getFactoid(key);
        if (factoid != null) {
            delete(factoid.getId());
            changeDao.logChange(String.format("%s removed '%s' with a value of '%s'", sender, key, factoid.getValue()));
        }
    }

    public Factoid getFactoid(final String name) {
        FactoidCriteria criteria = new FactoidCriteria(ds);
        criteria.upperName().equal(name.toUpperCase());
        Factoid factoid = criteria.query().get();
        if (factoid != null) {
            factoid.setLastUsed(LocalDateTime.now());
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
            factoid.setLastUsed(LocalDateTime.now());
            super.save(factoid);
        }
        return factoid;
    }

    public Long count() {
        return ds.createQuery(Factoid.class).countAll();
    }

    public Long countFiltered(final Factoid filter) {
        return buildFindQuery(null, filter, true).countAll();
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
            criteria.upperValue().contains(filter.getValue().toUpperCase());
        }
        if (!count && qp != null && qp.hasSort()) {
            criteria.query().order((qp.isSortAsc() ? "" : "-") + "upper" + qp.getSort());
            criteria.query().offset(qp.getFirst());
            criteria.query().limit(qp.getCount());
        }
        return criteria.query();
    }

    public WriteResult deleteAll() {
        return ds.delete(ds.createQuery(Factoid.class));
    }
}
