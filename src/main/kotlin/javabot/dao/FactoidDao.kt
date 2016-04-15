package javabot.dao

import com.antwerkz.sofia.Sofia
import com.mongodb.WriteResult
import javabot.dao.util.QueryParam
import javabot.model.Factoid
import javabot.model.Persistent
import javabot.model.criteria.FactoidCriteria
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Query
import java.time.LocalDateTime
import java.util.regex.PatternSyntaxException
import javax.inject.Inject

class FactoidDao @Inject constructor(ds: Datastore, var changeDao: ChangeDao, var configDao: ConfigDao) :
        BaseDao<Factoid>(ds, Factoid::class.java) {

    override fun save(entity: Persistent) {
        val factoid = entity as Factoid
        if (factoid.name == "") {
            throw IllegalArgumentException(Sofia.factoidCantBeBlank("name"))
        }
        if (factoid.value == "") {
            throw IllegalArgumentException(Sofia.factoidCantBeBlank("value"))
        }
        if (factoid.userName == "") {
            throw IllegalArgumentException(Sofia.factoidCantBeBlank("user name"))
        }
        super.save(entity)
    }

    fun hasFactoid(key: String): Boolean {
        val criteria = FactoidCriteria(ds)
        criteria.upperName().equal(key.toUpperCase())
        return criteria.query().get() != null
    }

    fun addFactoid(sender: String, key: String, value: String): Factoid {
        return addFactoid(sender, key, value, LocalDateTime.now())
    }

    fun addFactoid(sender: String, key: String, value: String, updated: LocalDateTime): Factoid {
        val factoid = Factoid(key, value, sender)
        factoid.updated = updated
        factoid.lastUsed = LocalDateTime.now()
        save(factoid)
        changeDao.logFactoidAdded(sender, key, value)
        return factoid
    }

    fun delete(sender: String, key: String) {
        val factoid = getFactoid(key)
        if (factoid != null) {
            delete(factoid.id)
            changeDao.logFactoidRemoved(sender, key, factoid.value)
        }
    }

    fun getFactoid(name: String): Factoid? {
        val criteria = FactoidCriteria(ds)
        criteria.upperName().equal(name.toUpperCase())
        val factoid = criteria.query().get()
        if (factoid != null) {
            factoid.lastUsed = LocalDateTime.now()
            super.save(factoid)
        }
        return factoid
    }

    fun getParameterizedFactoid(name: String): Factoid? {
        val criteria = FactoidCriteria(ds)
        criteria.or(
                criteria.upperName().equal(name.toUpperCase() + " \$1"),
                criteria.upperName().equal(name.toUpperCase() + " \$^"),
                criteria.upperName().equal(name.toUpperCase() + " \$+"))
        val factoid = criteria.query().get()
        if (factoid != null) {
            factoid.lastUsed = LocalDateTime.now()
            super.save(factoid)
        }
        return factoid
    }

    fun count(): Long {
        return ds.createQuery(Factoid::class.java).countAll()
    }

    fun countFiltered(filter: Factoid): Long {
        return buildFindQuery(null, filter, true).countAll()
    }

    fun getFactoidsFiltered(qp: QueryParam, filter: Factoid): List<Factoid> {
        return buildFindQuery(qp, filter, false).asList()
    }

    private fun buildFindQuery(qp: QueryParam?, filter: Factoid, count: Boolean): Query<Factoid> {
        val criteria = FactoidCriteria(ds)
        if (filter.name != "") {
            try {
                criteria.upperName().contains(filter.name.toUpperCase())
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.name)
            }
        }

        if (filter.userName != "") {
            try {
                criteria.upperUserName().contains(filter.userName.toUpperCase())
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.userName)
            }
        }

        if (filter.value != "") {
            try {
                criteria.upperValue().contains(filter.value.toUpperCase())
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.value)
            }
        }

        if (!count && qp != null && qp.hasSort()) {
            criteria.query().order((if (qp.sortAsc) "" else "-") + "upper" + qp.sort)
            criteria.query().offset(qp.first)
            criteria.query().limit(qp.count)
        }
        return criteria.query()
    }

    fun deleteAll(): WriteResult {
        return ds.delete(ds.createQuery(Factoid::class.java))
    }
}
