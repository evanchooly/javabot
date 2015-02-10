package javabot.dao;

import javabot.model.Admin;
import javabot.model.Config;
import javabot.model.EventType;
import javabot.model.OperationEvent;
import javabot.model.criteria.AdminCriteria;
import org.pircbotx.User;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

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

    public Admin getAdminByEmailAddress(final String email) {
        AdminCriteria criteria = new AdminCriteria(ds);
        criteria.emailAddress(email);
        Admin admin = criteria.query().get();
        if(admin == null && count() == 0) {
            admin = new Admin();
            admin.setEmailAddress(email);
            admin.setBotOwner(true);
            save(admin);
        }
        return admin;
    }

    public Admin create(final String ircName, final String userName, final String hostName) {
        final Admin admin = new Admin();
        admin.setIrcName(ircName);
        admin.setEmailAddress(userName);
        admin.setHostName(hostName);
        admin.setUpdated(LocalDateTime.now());
        admin.setBotOwner(findAll().isEmpty());
        save(admin);

        return admin;
    }

    public void enableOperation(String name, Admin admin) {
        OperationEvent event = new OperationEvent();
        event.setOperation(name);
        event.setRequestedOn(LocalDateTime.now());
        event.setType(EventType.ADD);
        event.setRequestedBy(admin.getEmailAddress());
        save(event);
        Config config = configDao.get();
        config.getOperations().add(name);
        configDao.save(config);
    }

    public void disableOperation(String name, Admin admin) {
        OperationEvent event = new OperationEvent();
        event.setOperation(name);
        event.setRequestedOn(LocalDateTime.now());
        event.setType(EventType.DELETE);
        event.setRequestedBy(admin.getEmailAddress());
        save(event);
        Config config = configDao.get();
        config.getOperations().remove(name);
        configDao.save(config);
    }

    public long count() {
        return getQuery().countAll();
    }
}