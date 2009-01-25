package javabot.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javabot.dao.util.EntityNotFoundException;
import javabot.model.Persistent;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AbstractDaoImpl<T> implements BaseDao<T> {
    private final Class entityClass;
    private EntityManager entityManager;

    protected AbstractDaoImpl(final Class dataClass) {
        entityClass = dataClass;
    }

    @PersistenceContext
    public void setEntityManager(final EntityManager manager) {
        entityManager = manager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @SuppressWarnings("unchecked")
    public T find(final Long id) {
        return (T)getEntityManager().find(entityClass, id);
    }

    @SuppressWarnings("unchecked")
    private T loadChecked(final Long id) throws EntityNotFoundException {
        final T persistedObject = find(id);
        if(persistedObject == null) {
            throw new EntityNotFoundException(entityClass, id);
        }
        return persistedObject;
    }

    public void merge(final Persistent detachedObject) {
        getEntityManager().merge(detachedObject);
    }

    public void save(final Persistent persistedObject) {
        if(persistedObject.getId() == null) {
            entityManager.persist(persistedObject);
        } else {
            entityManager.persist(entityManager.merge(persistedObject));
        }
        entityManager.flush();
    }

    @Transactional
    public void delete(final Persistent persistedObject) {
        getEntityManager().remove(getEntityManager().merge(persistedObject));
    }

    public void delete(final Long id) {
        delete((Persistent)loadChecked(id));
    }
}