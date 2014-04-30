package javabot.model.criteria;

import javabot.model.IrcUser;

public class IrcUserCriteria {
  private final org.mongodb.morphia.query.Query query;
  private final String prefix;

  public IrcUserCriteria(org.mongodb.morphia.query.Query query, String prefix) {
    this.query = query;
    this.prefix = prefix + ".";
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<IrcUserCriteria, IrcUser, java.lang.String> host() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<IrcUserCriteria, IrcUser, java.lang.String>(this, query, prefix + "host");
  }

  public org.mongodb.morphia.query.Criteria host(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<IrcUserCriteria, IrcUser, java.lang.String>(this, query, prefix + "host").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<IrcUserCriteria, IrcUser, java.lang.String> nick() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<IrcUserCriteria, IrcUser, java.lang.String>(this, query, prefix + "nick");
  }

  public org.mongodb.morphia.query.Criteria nick(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<IrcUserCriteria, IrcUser, java.lang.String>(this, query, prefix + "nick").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<IrcUserCriteria, IrcUser, java.lang.String> userName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<IrcUserCriteria, IrcUser, java.lang.String>(this, query, prefix + "userName");
  }

  public org.mongodb.morphia.query.Criteria userName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<IrcUserCriteria, IrcUser, java.lang.String>(this, query, prefix + "userName").equal(value);
  }
}
