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
        save(Change(message))
    }

    public fun logAdd(sender: String, key: String, value: String) {
        logChange(Sofia.factoidAdded(sender, key, value))
    }

    public fun findLog(message: String): Boolean {
        val criteria = ChangeCriteria(ds)
        criteria.message().equal(message)
        return criteria.query().countAll() !== 0L
    }

    public fun count(message: String?, date: LocalDateTime?): Long {
        return buildFindQuery(null, true, message, date).countAll()
    }

    @SuppressWarnings("unchecked")
    public fun getChanges(qp: QueryParam, message: String?, date: LocalDateTime?): List<Change> {
        return buildFindQuery(qp, true, message, date).asList()
    }

    private fun buildFindQuery(qp: QueryParam?, count: Boolean, message: String?, date: LocalDateTime?): Query<Change> {
        val criteria = ChangeCriteria(ds)
        if (message != null) {
            criteria.message().contains(message)
        }
        if (date != null) {
            criteria.changeDate(date)
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