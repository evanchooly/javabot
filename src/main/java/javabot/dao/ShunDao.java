package javabot.dao;

import java.util.Date;

import javabot.model.Shun;

public interface ShunDao {
  void addShun (String nick, Date until);

  Shun getShun (String nick);

  boolean isShunned (String nick);
  
  String CLEANUP = "Shun.cleanup";
  String BY_NAME = "Shun.byName";
}
