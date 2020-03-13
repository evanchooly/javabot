package javabot.dao

import com.mongodb.client.result.DeleteResult
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.experimental.filters.Filters
import javabot.dao.util.QueryParam
import javabot.model.Karma
import java.time.LocalDateTime
import javax.inject.Inject

class KarmaDao @Inject constructor(ds: Datastore, var changeDao: ChangeDao, var channelDao: ChannelDao) :
        BaseDao<Karma>(ds, Karma::class.java) {

    fun list(qp: QueryParam): List<Karma> {
        val options = FindOptions()
                .skip(qp.first)
                .limit(qp.count)
        if (qp.hasSort()) {
            options.sort(qp.toSort())
        }
        return ds.find(Karma::class.java).execute(options)
                .toList()
    }

    fun save(karma: Karma) {
        karma.updated = LocalDateTime.now()
        super.save(karma)
    }

    fun find(name: String): Karma? = ds.find(Karma::class.java)
            .filter(Filters.eq("upperName", name.toUpperCase())).first()

    fun count(): Long {
        return ds.find(Karma::class.java).count()
    }

    fun deleteAll(): DeleteResult {
        return ds.find(Karma::class.java).remove(DeleteOptions().multi(true))
    }
}
