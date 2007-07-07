package javabot.dao;

import javax.persistence.EntityManager;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javabot.dao.util.EntityNotFoundException;
import javabot.model.Persistent;

@Transactional
public class AbstractDaoHibernate<T> implements BaseDao<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractDaoHibernate.class);
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

    public void merge(Persistent detachedObject) {
        getEntityManager().merge(detachedObject);
    }

    public void save(Persistent persistedObject) {
        try {
            entityManager.persist(persistedObject);
        } catch(PersistenceException e) {
            Persistent persistent = entityManager.merge(persistedObject);
//            log.error(e.getMessage(), e);
//            throw e;
        }
    }

    public void delete(Persistent persistedObject) {
        getEntityManager().remove(persistedObject);
    }

    public void delete(Long id) {
        delete((Persistent)loadChecked(id));
    }
}