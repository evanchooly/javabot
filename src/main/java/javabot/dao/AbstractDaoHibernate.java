package javabot.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import javabot.dao.util.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AbstractDaoHibernate<T> {

    private Class entityClass;
    private EntityManager entityManager;

    protected AbstractDaoHibernate(Class dataClass) {
        this.entityClass = dataClass;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager manager) {
        this.entityManager = manager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @SuppressWarnings("unchecked")
    public T find(Long id) {
        return (T)getEntityManager().find(entityClass, id);
    }

    @SuppressWarnings("unchecked")
    private T loadChecked(Long id) throws EntityNotFoundException {
        T persistedObject = find(id);
        if(persistedObject == null) {
            throw new EntityNotFoundException(entityClass, id);
        }
        return persistedObject;
    }

    public void merge(T detachedObject) {
        getEntityManager().merge(detachedObject);
    }

    public void save(T persistedObject) {
        try {
            getEntityManager().persist(persistedObject);
        } catch(PersistenceException e) {
            getEntityManager().persist(getEntityManager().merge(persistedObject));
        }
    }

    private void delete(T persistedObject) {
        getEntityManager().remove(persistedObject);
    }

    public void delete(Long id) {
        delete(loadChecked(id));
    }
}