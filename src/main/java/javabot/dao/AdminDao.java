package javabot.dao;

import java.util.Iterator;

import javabot.dao.model.Admin;
import javabot.dao.util.QueryParam;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:02 PM

//
public interface AdminDao {
    String AUTHENTICATE = "Admin.authenticate";

    boolean isAdmin(String key);

    Admin getAdmin(String username);

    Admin get(Long id);

    Iterator<Admin> getIterator();

    Iterator<Admin> getAdmins(QueryParam qp);

    void updateAdmin(Admin admin, ChangesDao c_dao);

}