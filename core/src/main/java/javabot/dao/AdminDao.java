package javabot.dao;

import java.util.List;

import javabot.model.Admin;

public interface AdminDao extends BaseDao {
    String FIND = "Admin.find";
    String FIND_WITH_HOST = "Admin.findWithHost";
    String FIND_ALL = "Admin.findAll";

    boolean isAdmin(String user, String hostname);

    Admin getAdmin(String userName, String hostName);

    Admin getAdmin(String userName);

    List<Admin> findAll();

    void create(String newAdmin, String newHostName);
}