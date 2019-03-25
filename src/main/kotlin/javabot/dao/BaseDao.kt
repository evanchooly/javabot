package javabot.dao

import javabot.dao.util.EntityNotFoundException
import javabot.model.Persistent
import org.bson.types.ObjectId
import dev.morphia.Datastore
import dev.morphia.query.Query
import org.slf4j.LoggerFactory

abstract class BaseDao<T : Persistent>(var ds: Datastore, val entityClass: Class<T>) {
    companion object {
        private val LOG = LoggerFactory.getLogger(BaseDao::class.java)
    }

    fun getQuery(): Query<T> {
        return getQuery(entityClass)
    }

    fun <U> getQuery(clazz: Class<U>): Query<U> {
        return ds.createQuery(clazz)
    }

    fun find(id: ObjectId?): T? {
        return ds.createQuery(entityClass).filter("_id", id).get()
    }

    open fun findAll(): List<T> {
        return ds.createQuery(entityClass).asList()
    }

    private fun loadChecked(id: ObjectId?): T {
        return find(id) ?: throw EntityNotFoundException(entityClass, id)
    }

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
