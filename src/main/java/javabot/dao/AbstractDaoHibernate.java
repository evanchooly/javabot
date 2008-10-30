package javabot.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javabot.dao.util.EntityNotFoundException;
import javabot.model.Persistent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AbstractDaoHibernate<T> implements BaseDao<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractDaoHibernate.class);
    private Class entityClass;
    private EntityManager entityManager;

    protected AbstractDaoHibernate(Class dataClass) {
        entityClass = dataClass;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager manager) {
        entityManager = manager;
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
        entityManager.persist(persistedObject);
        entityManager.flush();
    }

    public void delete(Persistent persistedObject) {
        getEntityManager().remove(persistedObject);
    }

    public void delete(Long id) {
        delete((Persistent)loadChecked(id));
    }
}