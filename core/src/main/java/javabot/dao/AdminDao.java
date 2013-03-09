package javabot.dao;

import java.util.Date;

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
    return adminCriteria.query().get();
  }

  public Admin getAdmin(final String userName) {
    AdminCriteria adminCriteria = new AdminCriteria(ds);
    adminCriteria.userName().equal(userName);
    return adminCriteria.query().get();
  }

  public void create(final String ircName, final String userName, final String hostName) {
    final Admin admin = new Admin();
    admin.setIrcName(ircName);
    admin.setUserName(userName);
    admin.setHostName(hostName);
    admin.setUpdated(new Date());
    save(admin);
  }
}