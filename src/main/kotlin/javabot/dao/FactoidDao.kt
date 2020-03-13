package javabot.dao

import com.antwerkz.sofia.Sofia
import com.mongodb.client.result.DeleteResult
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.Query
import dev.morphia.query.experimental.filters.Filters.eq
import dev.morphia.query.experimental.filters.Filters.or
import javabot.dao.util.QueryParam
import javabot.model.Factoid
import javabot.model.Persistent
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

    fun hasFactoid(key: String) = ds.find(Factoid::class.java)
            .filter(eq("upperName", key.toUpperCase()))
            .first() != null

    fun addFactoid(sender: String, key: String, value: String, location: String): Factoid {
        return addFactoid(sender, key, value, location, LocalDateTime.now())
    }

    fun addFactoid(sender: String, key: String, value: String, location: String, updated: LocalDateTime): Factoid {
        val factoid = Factoid(key, value, sender)
        factoid.updated = updated
        factoid.lastUsed = LocalDateTime.now()
        save(factoid)
        changeDao.logFactoidAdded(sender, key, value, location)
        return factoid
    }

    fun delete(sender: String, key: String, location: String) {
        val factoid = getFactoid(key)
        if (factoid != null) {
            delete(factoid.id)
            changeDao.logFactoidRemoved(sender, key, factoid.value, location)
        }
    }

    fun getFactoid(name: String): Factoid? {
        val criteria = ds.find(Factoid::class.java)
                .filter(eq("upperName", name.toUpperCase()))
        val factoid = criteria.first()
        if (factoid != null) {
            factoid.lastUsed = LocalDateTime.now()
            super.save(factoid)
        }
        return factoid
    }

    fun getParameterizedFactoid(name: String): Factoid? {
        val factoid = ds.find(Factoid::class.java)
                .filter(or(
                        eq("upperName", name.toUpperCase() + " \$1"),
                        eq("upperName", name.toUpperCase() + " \$^"),
                        eq("upperName", name.toUpperCase() + " \$+")))
                .first()
        if (factoid != null) {
            factoid.lastUsed = LocalDateTime.now()
            super.save(factoid)
        }
        return factoid
    }

    fun count(): Long {
        return ds.find(Factoid::class.java).count()
    }

    fun countFiltered(filter: Factoid): Long {
        return buildFindQuery(filter).count()
    }

    fun getFactoidsFiltered(qp: QueryParam, filter: Factoid): List<Factoid> {
        val query = buildFindQuery(filter)
        val options = FindOptions()
                .skip(qp.first)
                .limit(qp.count)
        if (qp.hasSort()) {
            options.sort(qp.toSort("upper"))
        }
        return query
                .execute(options)
                .toList()
    }

    private fun buildFindQuery(filter: Factoid): Query<Factoid> {
        val query = ds.find(Factoid::class.java)
        if (filter.name != "") {
            try {
                query.filter(eq("upperName", filter.name.toUpperCase()))
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.name)
            }
        }

        if (filter.userName != "") {
            try {
                query.filter(eq("upperUserName", filter.userName.toUpperCase()))
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.userName)
            }
        }

        if (filter.value != "") {
            try {
                query.filter(eq("upperValue", filter.value.toUpperCase()))
            } catch (e: PatternSyntaxException) {
                Sofia.logFactoidInvalidSearchValue(filter.value)
            }
        }

        return query
    }

    fun deleteAll(): DeleteResult {
        return ds.find(Factoid::class.java)
                .remove(DeleteOptions().multi(true))
    }
}
