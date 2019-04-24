package javabot.dao

import com.antwerkz.sofia.Sofia

import com.google.inject.Inject
import com.mongodb.WriteResult
import javabot.dao.util.QueryParam
import javabot.model.Change
import javabot.model.criteria.ChangeCriteria
import dev.morphia.Datastore
import dev.morphia.query.FindOptions
import dev.morphia.query.Query
import java.time.LocalDateTime

class ChangeDao @Inject constructor(ds: Datastore) : BaseDao<Change>(ds, Change::class.java) {

    fun logFactoidAdded(sender: String, key: String, value: String, location: String) {
        save(Change(Sofia.factoidAdded(sender, key, value, location)))
    }

    fun logFactoidChanged(sender: String, key: String, oldValue: String, newValue: String, location: String) {
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
        val criteria = ChangeCriteria(ds)
        criteria.message().equal(message)
        return criteria.query().count() != 0L
    }

    fun count(message: String?, date: LocalDateTime?): Long {
        return buildFindQuery(message, date).count()
    }

    fun getChanges(qp: QueryParam, message: String?, date: LocalDateTime?): List<Change> {
        val findOptions = FindOptions()
        findOptions.skip(qp.first)
        findOptions.limit(qp.count)
        return buildFindQuery(message, date).find(findOptions).toList()
    }

    private fun buildFindQuery(message: String?, date: LocalDateTime?): Query<Change> {
        val criteria = ChangeCriteria(ds)
        if (message != null) {
            criteria.message().contains(message)
        }
        if (date != null) {
            criteria.changeDate(date)
        }
        criteria.changeDate().order(false)
        return criteria.query()
    }

    fun deleteAll(): WriteResult {
        return ds.delete(ds.createQuery(Change::class.java))
    }
}
