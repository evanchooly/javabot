package javabot.dao;

import java.util.List;

import javabot.model.Admin;

public interface AdminDao extends BaseDao<Admin> {
    String AUTHENTICATE = "Admin.authenticate";
    String FIND_ALL = "Admin.findAll";

    boolean isAdmin(String key);

    Admin getAdmin(String username);

    List<Admin> findAll();

    void create(String newAdmin, String newPassword);
}