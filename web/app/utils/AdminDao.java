package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;
import javabot.model.Admin;

public class AdminDao extends javabot.dao.AdminDao {
  public Subject getSubject(String userName) {
    Admin admin = getAdmin(userName);
    System.out.println("admin = " + admin);
    return admin != null ? new JavabotSubject(admin) : null;
  }

  private class JavabotSubject implements Subject {

    private Admin admin;

    private JavabotSubject(final Admin admin) {
      this.admin = admin;
    }

    @Override
    public List<? extends Role> getRoles() {
      return Arrays.asList(new Role() {
        public String getName() {
          return "botAdmin";
        }
      });
    }

    @Override
    public List<? extends Permission> getPermissions() {
      return new ArrayList<>();
    }

    @Override
    public String getIdentifier() {
      return admin.getUserName();
    }
  }
}
