package javabot.dao

import com.mongodb.WriteResult
import javabot.dao.util.QueryParam
import javabot.model.Channel
import javabot.model.Karma
import javabot.model.criteria.KarmaCriteria
import dev.morphia.Datastore
import java.time.LocalDateTime
import javax.inject.Inject

class KarmaDao @Inject constructor(ds: Datastore, var changeDao: ChangeDao, var channelDao: ChannelDao) :
        BaseDao<Karma>(ds, Karma::class.java) {

    fun getKarmas(qp: QueryParam): List<Karma> {
        val query = ds.createQuery(Karma::class.java)
        if (qp.hasSort()) {
            query.order((if (qp.sortAsc) "" else "-") + qp.sort)
        }
        query.offset(qp.first)
        query.limit(qp.count)
        return query.asList()
    }

    fun save(karma: Karma) {
        karma.updated = LocalDateTime.now()
        super.save(karma)
    }

    fun find(name: String): Karma? {
        val criteria = KarmaCriteria(ds)
        criteria.upperName().equal(name.toUpperCase())
        return criteria.query().get()
    }

    fun count(): Long {
        return ds.createQuery(Karma::class.java).countAll()
    }

    fun deleteAll(): WriteResult {
        return ds.delete(ds.createQuery(Karma::class.java))
    }
}
