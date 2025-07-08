package javabot.dao

import com.antwerkz.sofia.Sofia
import com.google.inject.Inject
import com.mongodb.client.result.DeleteResult
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.Query
import dev.morphia.query.Sort.descending
import dev.morphia.query.filters.Filters.eq
import dev.morphia.query.filters.Filters.regex
import java.time.LocalDateTime
import javabot.dao.util.QueryParam
import javabot.model.Change

class ChangeDao @Inject constructor(ds: Datastore) : BaseDao<Change>(ds, Change::class.java) {

    fun logFactoidAdded(sender: String, key: String, value: String, location: String) {
        save(Change(Sofia.factoidAdded(sender, key, value, location)))
    }

    fun logFactoidChanged(
        sender: String,
        key: String,
        oldValue: String,
        newValue: String,
        location: String,
    ) {
        save(Change(Sofia.factoidChanged(sender, key, oldValue, newValue, location)))
    }

    fun logFactoidRemoved(sender: String, key: String, value: String, location: String) {
        save(Change(Sofia.factoidRemoved(sender, key, value, location)))
    }

    fun logKarmaChanged(sender: String, target: String, value: Int, location: String) {
        save(Change(Sofia.karmaChanged(sender, target, value, location)))
    }

    fun logChangingLockedFactoid(nick: String, key: String, channel: String) {
        save(Change(Sofia.factoidChangingLocked(nick, key, channel)))
    }

    fun findLog(message: String): Boolean {
        val query = ds.find(Change::class.java).filter(eq("message", message))

        return query.count() != 0L
    }

    fun count(message: String?, date: LocalDateTime?): Long {
        return buildFindQuery(message, date).count()
    }

    fun getChanges(qp: QueryParam, message: String?, date: LocalDateTime?): List<Change> {
        return buildFindQuery(message, date)
            .iterator(FindOptions().skip(qp.first).limit(qp.count).sort(descending("changeDate")))
            .toList()
    }

    private fun buildFindQuery(message: String?, date: LocalDateTime?): Query<Change> {
        val query = ds.find(Change::class.java)
        if (message != null) {
            query.filter(regex("message").pattern(message))
        }
        if (date != null) {
            query.filter(eq("changeDate", date))
        }
        return query
    }

    fun deleteAll(): DeleteResult {
        return ds.find(Change::class.java).delete(DeleteOptions().multi(true))
    }
}
