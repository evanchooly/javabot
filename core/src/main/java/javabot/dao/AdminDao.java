package javabot.dao;

import javabot.model.Admin;

import java.util.List;

public interface AdminDao extends BaseDao<Admin> {
    String FIND = "Admin.find";
    String FIND_WITH_HOST = "Admin.findWithHost";
    String FIND_ALL = "Admin.findAll";

    boolean isAdmin(String user, String hostname);

    Admin getAdmin(String userName, String hostName);

    Admin getAdmin(String userName);

    List<Admin> findAll();

    void create(String newAdmin, String newHostName);
}