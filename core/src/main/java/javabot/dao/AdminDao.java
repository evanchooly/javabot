package javabot.dao;

import java.util.Date;
import java.util.List;

import javabot.model.Admin;
import javabot.model.criteria.AdminCriteria;

public class AdminDao extends BaseDao<Admin> {
    public AdminDao() {
        super(Admin.class);
    }

    public boolean isAdmin(final String user, final String hostName) {
        return findAll().isEmpty() || getAdmin(user, hostName) != null;
    }

    public Admin getAdmin(final String ircName, final String hostName) {
        AdminCriteria adminCriteria = new AdminCriteria(ds);
        adminCriteria.ircName().equal(ircName);
        adminCriteria.hostName().equal(hostName);
        final List<Admin> list = adminCriteria.query().asList();
        return list.isEmpty() ? null : list.get(0);
    }

    public Admin getAdmin(final String userName) {
        AdminCriteria adminCriteria = new AdminCriteria(ds);
        adminCriteria.userName().equal(userName);
        final List<Admin> list = adminCriteria.query().asList();
        return list.isEmpty() ? null : list.get(0);
    }

    public void create(final String newAdmin) {
        final Admin admin = new Admin();
        admin.setUserName(newAdmin);
        admin.setUpdated(new Date());
        save(admin);
    }
}