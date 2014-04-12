package models;

import java.util.ArrayList;
import java.util.List;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

public class Admin extends javabot.model.Admin implements Subject {
  public Admin() {
  }

  public Admin(String ircName, String hostName, String email, String addedBy) {
    setIrcName(ircName);
    setHostName(hostName);
    setAddedBy(addedBy);
  }

  @Override
  public List<? extends Role> getRoles() {
    return new ArrayList<>();
  }

  @Override
  public List<? extends Permission> getPermissions() {
    return new ArrayList<>();
  }

  @Override
  public String getIdentifier() {
    return getIrcName();
  }

}
