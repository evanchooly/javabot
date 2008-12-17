package javabot.dao;

import java.util.List;

import javabot.model.Admin;

public interface AdminDao extends BaseDao<Admin> {
    String AUTHENTICATE = "Admin.authenticate";
    String FIND_WITH_HOST = "Admin.findWithHost";
    String FIND_ALL = "Admin.findAll";

    boolean isAdmin(String user, String hostname);

    Admin getAdmin(String userName);

    Admin getAdmin(String userName, String hostName);

    List<Admin> findAll();

    void create(String newAdmin, String newPassword, String newHostName);
}