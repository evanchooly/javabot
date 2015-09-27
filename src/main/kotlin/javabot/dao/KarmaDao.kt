package javabot.dao

import com.antwerkz.sofia.Sofia
import com.mongodb.WriteResult
import javabot.dao.util.QueryParam
import javabot.model.Karma
import javabot.model.criteria.KarmaCriteria
import java.time.LocalDateTime
import javax.inject.Inject

public class KarmaDao : BaseDao<Karma>(Karma::class.java) {
    @Inject
    lateinit val changeDao: ChangeDao

    public fun getKarmas(qp: QueryParam): List<Karma> {
        val query = ds.createQuery(Karma::class.java)
        if (qp.hasSort()) {
            query.order((if (qp.sortAsc) "" else "-") + qp.sort)
        }
        query.offset(qp.first)
        query.limit(qp.count)
        return query.asList()
    }

    public fun save(karma: Karma) {
        karma.updated = LocalDateTime.now()
        super.save(karma)
        changeDao.logChange(Sofia.karmaChanged(karma.userName, karma.name, karma.value))
    }

    public fun find(name: String): Karma? {
        val criteria = KarmaCriteria(ds)
        criteria.upperName().equal(name.toUpperCase())
        return criteria.query().get()
    }

    public fun count(): Long {
        return ds.createQuery(Karma::class.java).countAll()
    }

    public fun deleteAll(): WriteResult {
        return ds.delete(ds.createQuery(Karma::class.java))
    }
}