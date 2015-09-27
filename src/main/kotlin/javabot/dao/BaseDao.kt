package javabot.dao

import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Query
import com.google.inject.Inject
import javabot.dao.util.EntityNotFoundException
import javabot.model.Persistent
import org.bson.types.ObjectId

public open class BaseDao<T : Persistent>(val entityClass: Class<T>) {
    @Inject
    lateinit var ds: Datastore

    public fun getQuery(): Query<T> {
        return getQuery(entityClass)
    }

    public fun <U> getQuery(clazz: Class<U>): Query<U> {
        return ds.createQuery(clazz)
    }

    public fun find(id: ObjectId?): T? {
        return ds.createQuery(entityClass).filter("_id", id).get()
    }

    public open fun findAll(): List<T> {
        return ds.createQuery(entityClass).asList()
    }

    private fun loadChecked(id: ObjectId?): T {
        val persistedObject = find(id) ?: throw EntityNotFoundException(entityClass, id)
        return persistedObject
    }

    public open fun save(entity: Persistent) {
        ds.save(entity)
    }

    public fun delete(entity: Persistent?) {
        if (entity != null) {
            ds.delete<Persistent>(entity)
        }
    }

    public open fun delete(id: ObjectId?) {
        delete(loadChecked(id))
    }
}