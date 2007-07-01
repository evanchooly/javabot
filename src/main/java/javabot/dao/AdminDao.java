package javabot.dao;

import java.util.List;

import javabot.model.Admin;

public interface AdminDao {
    String AUTHENTICATE = "Admin.authenticate";
    String ALL = "Admin.all";

    boolean isAdmin(String key);

    Admin getAdmin(String username);

    List<Admin> findAll();

}