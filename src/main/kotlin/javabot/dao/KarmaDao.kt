package javabot.dao

import com.mongodb.WriteResult
import dev.morphia.Datastore
import dev.morphia.query.FindOptions
import javabot.dao.util.QueryParam
import javabot.model.Karma
import javabot.model.criteria.KarmaCriteria
import java.time.LocalDateTime
import javax.inject.Inject

class KarmaDao @Inject constructor(ds: Datastore, var changeDao: ChangeDao, var channelDao: ChannelDao) :
        BaseDao<Karma>(ds, Karma::class.java) {

    fun getKarmas(qp: QueryParam): List<Karma> {
        val query = ds.createQuery(Karma::class.java)
        if (qp.hasSort()) {
            query.order(qp.toSort())
        }
        val options = FindOptions()
        options.skip(qp.first)
        options.limit(qp.count)
        return query.find(options).toList()
    }

    fun save(karma: Karma) {
        karma.updated = LocalDateTime.now()
        super.save(karma)
    }

    fun find(name: String): Karma? {
        val criteria = KarmaCriteria(ds)
        criteria.upperName().equal(name.toUpperCase())
        return criteria.query().first()
    }

    fun count(): Long {
        return ds.createQuery(Karma::class.java).count()
    }

    fun deleteAll(): WriteResult {
        return ds.delete(ds.createQuery(Karma::class.java))
    }
}
