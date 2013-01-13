package javabot.dao;

import java.util.List;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.query.Query;
import javabot.dao.util.EntityNotFoundException;
import javabot.model.Persistent;

public class BaseDao<T extends Persistent> {
    private final Class<T> entityClass;
    protected Datastore ds;

    protected BaseDao(final Class<T> dataClass) {
        entityClass = dataClass;
    }

    public Query<T> getQuery() {
        return ds.createQuery(entityClass);
    }

    public T find(final Long id) {
        return ds.<T>createQuery(entityClass).filter("_id", id).get();
    }

    public List<T> findAll() {
        return ds.createQuery(entityClass).asList();
    }

    private T loadChecked(final Long id) {
        final T persistedObject = find(id);
        if(persistedObject == null) {
            throw new EntityNotFoundException(entityClass, id);
        }
        return persistedObject;
    }

    public void save(final Persistent object) {
        ds.save(object);
    }

    public void delete(final Persistent object) {
        ds.delete(object);
    }

    public void delete(final Long id) {
        delete(loadChecked(id));
    }
}