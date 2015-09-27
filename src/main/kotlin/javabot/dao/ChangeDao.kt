package javabot.dao

import com.antwerkz.sofia.Sofia
import com.mongodb.WriteResult
import javabot.dao.util.QueryParam
import javabot.model.Change
import javabot.model.criteria.ChangeCriteria
import org.mongodb.morphia.query.Query

import java.time.LocalDateTime

public class ChangeDao : BaseDao<Change>(Change::class.java) {

    public fun logChange(message: String) {
        val change = Change()
        change.message = message
        change.changeDate = LocalDateTime.now()
        save(change)
    }

    public fun logAdd(sender: String, key: String, value: String) {
        logChange(Sofia.factoidAdded(sender, key, value))
    }

    public fun findLog(message: String): Boolean {
        val criteria = ChangeCriteria(ds)
        criteria.message().equal(message)
        return criteria.query().countAll() !== 0L
    }

    public fun count(filter: Change): Long {
        return buildFindQuery(null, filter, true).countAll()
    }

    @SuppressWarnings("unchecked")
    public fun getChanges(qp: QueryParam, filter: Change): List<Change> {
        return buildFindQuery(qp, filter, true).asList()
    }

    private fun buildFindQuery(qp: QueryParam?, filter: Change, count: Boolean): Query<Change> {
        val criteria = ChangeCriteria(ds)
        if (filter.id != null) {
            criteria.id().equal(filter.id)
        }
        if (filter.message != null) {
            criteria.message().contains(filter.message)
        }
        if (filter.changeDate != null) {
            criteria.changeDate(filter.changeDate)
        }
        criteria.changeDate().order(false)
        if (!count && qp != null) {
            criteria.query().offset(qp.first)
            criteria.query().limit(qp.count)
        }
        return criteria.query()
    }

    public fun deleteAll(): WriteResult {
        return ds.delete(ds.createQuery(Change::class.java))
    }
}