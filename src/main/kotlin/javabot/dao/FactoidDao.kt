package javabot.dao

import com.antwerkz.sofia.Sofia
import com.mongodb.WriteResult
import javabot.dao.util.CleanHtmlConverter
import javabot.dao.util.QueryParam
import javabot.model.Factoid
import javabot.model.Persistent
import javabot.model.criteria.FactoidCriteria
import org.mongodb.morphia.query.Query
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject
import java.time.LocalDateTime
import java.util.regex.PatternSyntaxException

SuppressWarnings("ConstantNamingConvention")
public class FactoidDao : BaseDao<Factoid>(Factoid::class.java) {

    Inject
    private val changeDao: ChangeDao? = null

    Inject
    private val dao: ConfigDao? = null

    override fun save(persistent: Persistent) {
        val factoid = persistent as Factoid
        val old = find(persistent.id)
        super.save(persistent)
        val formattedValue = CleanHtmlConverter.convert(factoid.value) { s -> Sofia.logsAnchorFormat(s, s) }
        if (old != null) {
            changeDao!!.logChange(String.format("%s changed '%s' from '%s' to '%s'",
                  factoid.userName, factoid.name,
                  CleanHtmlConverter.convert(old.value) { s -> Sofia.logsAnchorFormat(s, s) },
                  formattedValue))
        } else {
            changeDao!!.logChange(String.format("%s added '%s' with '%s'", factoid.userName, factoid.name,
                  formattedValue))
        }
    }

    public fun hasFactoid(key: String): Boolean {
        val criteria = FactoidCriteria(ds)
        criteria.upperName().equal(key.toUpperCase())
        return criteria.query().get() != null
    }

    public fun addFactoid(sender: String, key: String, value: String): Factoid {
        val factoid = Factoid()
        factoid.id = factoid.id
        factoid.name = key
        factoid.value = value
        factoid.userName = sender
        factoid.updated = LocalDateTime.now()
        factoid.lastUsed = LocalDateTime.now()
        save(factoid)
        changeDao!!.logAdd(sender, key, value)
        return factoid
    }

    public fun delete(sender: String, key: String) {
        val factoid = getFactoid(key)
        if (factoid != null) {
            delete(factoid.id)
            changeDao!!.logChange(String.format("%s removed '%s' with a value of '%s'", sender, key, factoid.value))
        }
    }

    public fun getFactoid(name: String): Factoid? {
        val criteria = FactoidCriteria(ds)
        criteria.upperName().equal(name.toUpperCase())
        val factoid = criteria.query().get()
        if (factoid != null) {
            factoid!!.lastUsed = LocalDateTime.now()
            super.save(factoid)
        }
        return factoid
    }

    public fun getParameterizedFactoid(name: String): Factoid {
        val criteria = FactoidCriteria(ds)
        criteria.or(
              criteria.upperName().equal(name.toUpperCase() + " $1"),
              criteria.upperName().equal(name.toUpperCase() + " $^"),
              criteria.upperName().equal(name.toUpperCase() + " $+"))
        val factoid = criteria.query().get()
        if (factoid != null) {
            factoid!!.lastUsed = LocalDateTime.now()
            super.save(factoid)
        }
        return factoid
    }

    public fun count(): Long? {
        return ds.createQuery(Factoid::class.java).countAll()
    }

    public fun countFiltered(filter: Factoid): Long? {
        return buildFindQuery(null, filter, true).countAll()
    }

    public fun getFactoidsFiltered(qp: QueryParam, filter: Factoid): List<Factoid> {
        return buildFindQuery(qp, filter, false).asList()
    }

    private fun buildFindQuery(qp: QueryParam?, filter: Factoid, count: Boolean): Query<Factoid> {
        val criteria = FactoidCriteria(ds)
        if (filter.name != null) {
            try {
                criteria.upperName().contains(filter.name.toUpperCase())
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.value)
            }

        }
        if (filter.userName != null) {
            try {
                criteria.upperUserName().contains(filter.userName.toUpperCase())
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.value)
            }

        }
        if (filter.value != null) {
            try {
                criteria.upperValue().contains(filter.value.toUpperCase())
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.value)
            }

        }
        if (!count && qp != null && qp.hasSort()) {
            criteria.query().order((if (qp.isSortAsc) "" else "-") + "upper" + qp.sort)
            criteria.query().offset(qp.first)
            criteria.query().limit(qp.count)
        }
        return criteria.query()
    }

    public fun deleteAll(): WriteResult {
        return ds.delete(ds.createQuery(Factoid::class.java))
    }

    companion object {
        private val log = LoggerFactory.getLogger(FactoidDao::class.java)
    }
}
