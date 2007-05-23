package javabot.dao;


import javabot.dao.util.EntityNotFoundException;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

//
// User: joed
// Date: Apr 11, 2007
// Time: 2:42:42 PM

/*                     
* Created by: Andrew Lombardi
 * Copyright 2006 Mystic Coders, LLC
 */


public class AbstractDaoHibernate<T> extends HibernateDaoSupport {

    // MEMBERS

    private Class entityClass;
    private SessionFactory sessionFactory;


    // CONSTRUCTORS

    protected AbstractDaoHibernate(Class dataClass) {
        super();
        this.entityClass = dataClass;
    }


    // METHODS

    @SuppressWarnings("unchecked")
    public T load(Long id) {
        return (T) getSession().get(entityClass, id);
    }

    @SuppressWarnings("unchecked")
    public T loadChecked(Long id) throws EntityNotFoundException {
        T persistedObject = load(id);

        if (persistedObject == null) {
            throw new EntityNotFoundException(entityClass, id);
        }

        return persistedObject;
    }

    public void merge(T detachedObject) {
        getSession().merge(detachedObject);
    }

    public void save(T persistedObject) {
        getSession().saveOrUpdate(persistedObject);
    }

    public void delete(T persistedObject) {
        getSession().delete(persistedObject);
    }

    public void delete(Long id) {
        delete(loadChecked(id));
    }

}
