package javabot.dao

import javabot.dao.util.EntityNotFoundException
import javabot.model.Persistent
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Query

abstract class BaseDao<T : Persistent>(var ds: Datastore, val entityClass: Class<T>) {
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
        val persistedObject = find(id) ?: throw EntityNotFoundException(entityClass, id)
        return persistedObject
    }

    open fun save(entity: Persistent) {
        ds.save(entity)
    }

    fun delete(entity: Persistent?) {
        if (entity != null) {
            ds.delete<Persistent>(entity)
        }
    }

    open fun delete(id: ObjectId?) {
        delete(loadChecked(id))
    }
}