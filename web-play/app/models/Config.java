package models;

import play.db.jpa.Model;

import java.util.Set;
import java.util.TreeSet;

public class Config extends Model {
    String server = "irc.freenode.org";
    Integer port = 6667;
    Integer historyLength = 6;
    String trigger = "~";
    String nick;
    String password;
    Integer schemaVersion;
    Set<String> operations = new TreeSet<String>();
}
