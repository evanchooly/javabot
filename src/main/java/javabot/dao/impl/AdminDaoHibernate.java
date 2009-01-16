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

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Admin> findAll() {
        return getEntityManager().createNamedQuery(AdminDao.FIND_ALL)
            .getResultList();
    }

    @Override
    public boolean isAdmin(final String user, final String hostName) {
        return findAll().isEmpty() || getAdmin(user, hostName) != null;
    }

    @Override
    public Admin getAdmin(final String userName) {
        return (Admin)getEntityManager().createNamedQuery(AdminDao.AUTHENTICATE)
            .setParameter("username", userName)
            .getSingleResult();
    }

    @Override
    public Admin getAdmin(final String userName, final String hostName) {
        return (Admin)getEntityManager().createNamedQuery(AdminDao.FIND_WITH_HOST)
            .setParameter("username", userName)
            .setParameter("hostName", hostName)
            .getSingleResult();
    }

    @Override
    public void setEntityManager(final EntityManager manager) {
        super.setEntityManager(manager);
    }

    @Override
    public void create(final String newAdmin, final String newPassword, final String newHostName) {
        final Admin admin = new Admin();
        admin.setUserName(newAdmin);
        admin.setPassword(newPassword);
        admin.setUpdated(new Date());
        admin.setHostName(newHostName);

        final Config config = configDao.get();
        admin.setConfig(config);
        save(admin);
        config.getAdmins().add(admin);
        configDao.save(config);
    }

    public ConfigDao getConfigDao() {
        return configDao;
    }
}