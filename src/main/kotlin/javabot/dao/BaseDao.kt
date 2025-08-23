package javabot.dao

import dev.morphia.Datastore
import dev.morphia.query.FindOptions
import dev.morphia.query.Query
import dev.morphia.query.filters.Filters.eq
import javabot.dao.util.EntityNotFoundException
import javabot.model.Persistent
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory

abstract class BaseDao<T : Persistent>(val ds: Datastore, val entityClass: Class<T>) {
    companion object {
        private val LOG = LoggerFactory.getLogger(BaseDao::class.java)
    }

    fun getQuery(options: FindOptions = FindOptions()): Query<T> = getQuery(entityClass, options)

    fun <U> getQuery(clazz: Class<U>, options: FindOptions = FindOptions()): Query<U> =
        ds.find(clazz, options)

    fun find(id: ObjectId?): T? = ds.find(entityClass).filter(eq("_id", id)).first()

    open fun findAll(): List<T> = ds.find(entityClass).iterator().toList()

    private fun loadChecked(id: ObjectId?) =
        find(id) ?: throw EntityNotFoundException(entityClass, id)

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
