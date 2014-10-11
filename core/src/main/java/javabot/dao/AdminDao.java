package javabot.dao;

import java.time.LocalDateTime;
import java.util.List;
import javax.inject.Inject;

import javabot.model.Admin;
import javabot.model.Config;
import javabot.model.EventType;
import javabot.model.OperationEvent;
import javabot.model.criteria.AdminCriteria;
import org.pircbotx.User;

public class AdminDao extends BaseDao<Admin> {
  @Inject
  private ConfigDao configDao;

  public AdminDao() {
    super(Admin.class);
  }

  @Override
  public List<Admin> findAll() {
    return ds.createQuery(Admin.class).order("userName").asList();
  }

  public boolean isAdmin(final User user) {
    return findAll().isEmpty() || getAdmin(user) != null;
  }

  public Admin getAdmin(final String ircName, final String hostName) {
    AdminCriteria adminCriteria = new AdminCriteria(ds);
    adminCriteria.ircName().equal(ircName);
    adminCriteria.hostName().equal(hostName);
    return adminCriteria.query().get();
  }

  public Admin getAdmin(final User user) {
    AdminCriteria adminCriteria = new AdminCriteria(ds);
    adminCriteria.ircName(user.getNick());
    return adminCriteria.query().get();
  }

  public void create(final String ircName, final String userName, final String hostName) {
    final Admin admin = new Admin();
    admin.setIrcName(ircName);
    admin.setUserName(userName);
    admin.setHostName(hostName);
    admin.setUpdated(LocalDateTime.now());
    admin.setBotOwner(findAll().isEmpty());
    save(admin);
  }

  public void enableOperation(String name, User user) {
    Admin admin = getAdmin(user);
    OperationEvent event = new OperationEvent();
    event.setOperation(name);
    event.setRequestedOn(LocalDateTime.now());
    event.setType(EventType.ADD);
    event.setRequestedBy(admin.getUserName());
    save(event);
    Config config = configDao.get();
    config.getOperations().add(name);
    configDao.save(config);
  }

  public void disableOperation(String name, User user) {
    Admin admin = getAdmin(user);
    OperationEvent event = new OperationEvent();
    event.setOperation(name);
    event.setRequestedOn(LocalDateTime.now());
    event.setType(EventType.DELETE);
    event.setRequestedBy(admin.getUserName());
    save(event);
    Config config = configDao.get();
    config.getOperations().remove(name);
    configDao.save(config);
  }
}