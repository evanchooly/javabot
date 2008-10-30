package javabot.dao.impl;

import java.util.List;
import java.util.Date;
import javax.persistence.EntityManager;

import javabot.dao.AbstractDaoHibernate;
import javabot.dao.AdminDao;
import javabot.dao.ConfigDao;
import javabot.model.Admin;
import javabot.model.Config;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminDaoHibernate extends AbstractDaoHibernate<Admin> implements AdminDao {
    @Autowired
    private ConfigDao configDao;

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

    public void create(String newAdmin, String newPassword) {
        Admin admin = new Admin();
        admin.setUserName(newAdmin);
        admin.setPassword(newPassword);
        admin.setUpdated(new Date());

        Config config = configDao.get();
        admin.setConfig(config);
        config.getAdmins().add(admin);
        configDao.save(config);
        save(admin);
    }

    public ConfigDao getConfigDao() {
        return configDao;
    }
}