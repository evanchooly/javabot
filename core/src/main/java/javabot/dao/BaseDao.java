package javabot.dao;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import com.google.inject.Inject;
import javabot.dao.util.EntityNotFoundException;
import javabot.model.Persistent;
import org.bson.types.ObjectId;

public class BaseDao<T extends Persistent> {
  private final Class<T> entityClass;
  @Inject
  protected Datastore ds;

  protected BaseDao(final Class<T> dataClass) {
    entityClass = dataClass;
  }

  public Query<T> getQuery() {
    return getQuery(entityClass);
  }

  public <U> Query<U> getQuery(Class<U> clazz) {
    return ds.createQuery(clazz);
  }

  public T find(final ObjectId id) {
    return ds.createQuery(entityClass).filter("_id", id).get();
  }

  public List<T> findAll() {
    return ds.createQuery(entityClass).asList();
  }

  private T loadChecked(final ObjectId id) {
    final T persistedObject = find(id);
    if (persistedObject == null) {
      throw new EntityNotFoundException(entityClass, id);
    }
    return persistedObject;
  }

  public void save(final Persistent object) {
    ds.save(object);
  }

  public void delete(final Persistent object) {
    if(object != null) {
      ds.delete(object);
    }
  }

  public void delete(final ObjectId id) {
    delete(loadChecked(id));
  }
}