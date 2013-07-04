package javabot.dao;

import java.util.List;

import javabot.model.Admin;
import javabot.model.criteria.AdminCriteria;
import org.joda.time.DateTime;

public class AdminDao extends BaseDao<Admin> {
  public AdminDao() {
    super(Admin.class);
  }

  @Override
  public List<Admin> findAll() {
    return ds.createQuery(Admin.class).order("userName").asList();
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
    admin.setUpdated(new DateTime());
    admin.setBotOwner(findAll().isEmpty());
    save(admin);
  }
}