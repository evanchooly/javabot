package javabot.dao;

import javabot.dao.model.Admin;
import javabot.dao.util.QueryParam;

import java.util.Iterator;

// User: joed
// Date: Apr 11, 2007
// Time: 2:41:02 PM

//
public interface AdminDao {

    boolean isAdmin(String key);

    Admin getAdmin(String username);

    Admin get(Long id);

    Iterator<Admin> getIterator();

    Iterator<Admin> getAdmins(QueryParam qp);

    void updateAdmin(Admin admin, ChangesDao c_dao);

}