package javabot.dao

import com.mongodb.client.result.DeleteResult
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.filters.Filters.eq
import jakarta.inject.Inject
import java.time.LocalDateTime
import java.util.Locale
import javabot.dao.util.QueryParam
import javabot.model.Karma

class KarmaDao
@Inject
constructor(ds: Datastore, var changeDao: ChangeDao, var channelDao: ChannelDao) :
    BaseDao<Karma>(ds, Karma::class.java) {

    fun list(qp: QueryParam): List<Karma> {
        val options = FindOptions().skip(qp.first).limit(qp.count)
        if (qp.hasSort()) {
            options.sort(qp.toSort())
        }
        return ds.find(Karma::class.java).iterator(options).toList()
    }

    fun save(karma: Karma) {
        karma.updated = LocalDateTime.now()
        super.save(karma)
    }

    fun find(name: String): Karma? =
        ds.find(Karma::class.java)
            .filter(eq("upperName", name.uppercase(Locale.getDefault())))
            .first()

    fun count(): Long {
        return ds.find(Karma::class.java).count()
    }

    fun deleteAll(): DeleteResult {
        return ds.find(Karma::class.java).delete(DeleteOptions().multi(true))
    }
}
