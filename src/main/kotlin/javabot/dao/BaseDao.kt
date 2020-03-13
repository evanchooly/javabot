package javabot.dao

import dev.morphia.Datastore
import dev.morphia.query.Query
import dev.morphia.query.experimental.filters.Filters.eq
import javabot.dao.util.EntityNotFoundException
import javabot.model.Persistent
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory

abstract class BaseDao<T : Persistent>(val ds: Datastore, val entityClass: Class<T>) {
    companion object {
        private val LOG = LoggerFactory.getLogger(BaseDao::class.java)
    }

    fun getQuery(): Query<T> = getQuery(entityClass)

    fun <U> getQuery(clazz: Class<U>): Query<U> = ds.find(clazz)

    fun find(id: ObjectId?): T? = ds.find(entityClass)
            .filter(eq("_id", id))
            .first()

    open fun findAll(): List<T> = ds.find(entityClass).execute().toList()

    private fun loadChecked(id: ObjectId?) = find(id) ?: throw EntityNotFoundException(entityClass, id)

    open fun save(entity: Persistent) {
        ds.save(entity)
    }

    fun delete(entity: Persistent?) {
        entity?.let {
            LOG.debug("deleting entity:  $entity")
            ds.delete(it)
        }
    }

    open fun delete(id: ObjectId?) {
        delete(loadChecked(id))
    }
}
