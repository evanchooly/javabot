package javabot.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.AdminDao;
import javabot.model.Admin;

public class AdminDaoHibernate extends AbstractDaoHibernate<Admin> implements AdminDao {
    public AdminDaoHibernate() {
        super(Admin.class);
    }

    @SuppressWarnings({"unchecked"})
    public List<Admin> findAll() {
        return getEntityManager().createNamedQuery(AdminDao.FIND_ALL)
            .getResultList();
    }

    public boolean isAdmin(String key) {
        return getAdmin(key).getUserName() != null;
    }

    public Admin getAdmin(String username) {
        return (Admin)getEntityManager().createNamedQuery(AdminDao.AUTHENTICATE)
            .setParameter("username", username)
            .getSingleResult();
    }

    @Override
    public void setEntityManager(EntityManager manager) {
        super.setEntityManager(manager);
    }
}