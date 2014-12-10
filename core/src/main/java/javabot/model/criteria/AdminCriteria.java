package javabot.model.criteria;

import javabot.model.Admin;

public class AdminCriteria extends com.antwerkz.critter.criteria.BaseCriteria<Admin> {
  private String prefix = "";

  public AdminCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, Admin.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.String> addedBy() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.String>(this, query, prefix + "addedBy");
  }

  public org.mongodb.morphia.query.Criteria addedBy(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.String>(this, query, prefix + "addedBy").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.Boolean> botOwner() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.Boolean>(this, query, prefix + "botOwner");
  }

  public org.mongodb.morphia.query.Criteria botOwner(java.lang.Boolean value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.Boolean>(this, query, prefix + "botOwner").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.String> emailAddress() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.String>(this, query, prefix + "emailAddress");
  }

  public org.mongodb.morphia.query.Criteria emailAddress(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.String>(this, query, prefix + "emailAddress").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.String> hostName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.String>(this, query, prefix + "hostName");
  }

  public org.mongodb.morphia.query.Criteria hostName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.String>(this, query, prefix + "hostName").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.String> ircName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.String>(this, query, prefix + "ircName");
  }

  public org.mongodb.morphia.query.Criteria ircName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.lang.String>(this, query, prefix + "ircName").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.time.LocalDateTime> updated() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.time.LocalDateTime>(this, query, prefix + "updated");
  }

  public org.mongodb.morphia.query.Criteria updated(java.time.LocalDateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminCriteria, Admin, java.time.LocalDateTime>(this, query, prefix + "updated").equal(value);
  }


  public AdminUpdater getUpdater() {
    return new AdminUpdater();
  }

  public class AdminUpdater {
    org.mongodb.morphia.query.UpdateOperations<Admin> updateOperations;

    public AdminUpdater() {
      updateOperations = ds.createUpdateOperations(Admin.class);
    }

    public org.mongodb.morphia.query.UpdateResults<Admin> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<Admin> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<Admin> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<Admin> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public AdminUpdater addedBy(java.lang.String value) {
      updateOperations.set("addedBy", value);
      return this;
    }

    public AdminUpdater unsetAddedBy(java.lang.String value) {
      updateOperations.unset("addedBy");
      return this;
    }

    public AdminUpdater addAddedBy(java.lang.String value) {
      updateOperations.add("addedBy", value);
      return this;
    }

    public AdminUpdater addAddedBy(java.lang.String value, boolean addDups) {
      updateOperations.add("addedBy", value, addDups);
      return this;
    }

    public AdminUpdater addAllToAddedBy(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("addedBy", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstAddedBy() {
      updateOperations.removeFirst("addedBy");
      return this;
    }
  
    public AdminUpdater removeLastAddedBy() {
      updateOperations.removeLast("addedBy");
      return this;
    }
  
    public AdminUpdater removeFromAddedBy(java.lang.String value) {
      updateOperations.removeAll("addedBy", value);
      return this;
    }

    public AdminUpdater removeAllFromAddedBy(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("addedBy", values);
      return this;
    }
 
    public AdminUpdater decAddedBy() {
      updateOperations.dec("addedBy");
      return this;
    }

    public AdminUpdater incAddedBy() {
      updateOperations.inc("addedBy");
      return this;
    }

    public AdminUpdater incAddedBy(Number value) {
      updateOperations.inc("addedBy", value);
      return this;
    }
    public AdminUpdater botOwner(java.lang.Boolean value) {
      updateOperations.set("botOwner", value);
      return this;
    }

    public AdminUpdater unsetBotOwner(java.lang.Boolean value) {
      updateOperations.unset("botOwner");
      return this;
    }

    public AdminUpdater addBotOwner(java.lang.Boolean value) {
      updateOperations.add("botOwner", value);
      return this;
    }

    public AdminUpdater addBotOwner(java.lang.Boolean value, boolean addDups) {
      updateOperations.add("botOwner", value, addDups);
      return this;
    }

    public AdminUpdater addAllToBotOwner(java.util.List<java.lang.Boolean> values, boolean addDups) {
      updateOperations.addAll("botOwner", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstBotOwner() {
      updateOperations.removeFirst("botOwner");
      return this;
    }
  
    public AdminUpdater removeLastBotOwner() {
      updateOperations.removeLast("botOwner");
      return this;
    }
  
    public AdminUpdater removeFromBotOwner(java.lang.Boolean value) {
      updateOperations.removeAll("botOwner", value);
      return this;
    }

    public AdminUpdater removeAllFromBotOwner(java.util.List<java.lang.Boolean> values) {
      updateOperations.removeAll("botOwner", values);
      return this;
    }
 
    public AdminUpdater decBotOwner() {
      updateOperations.dec("botOwner");
      return this;
    }

    public AdminUpdater incBotOwner() {
      updateOperations.inc("botOwner");
      return this;
    }

    public AdminUpdater incBotOwner(Number value) {
      updateOperations.inc("botOwner", value);
      return this;
    }
    public AdminUpdater emailAddress(java.lang.String value) {
      updateOperations.set("emailAddress", value);
      return this;
    }

    public AdminUpdater unsetEmailAddress(java.lang.String value) {
      updateOperations.unset("emailAddress");
      return this;
    }

    public AdminUpdater addEmailAddress(java.lang.String value) {
      updateOperations.add("emailAddress", value);
      return this;
    }

    public AdminUpdater addEmailAddress(java.lang.String value, boolean addDups) {
      updateOperations.add("emailAddress", value, addDups);
      return this;
    }

    public AdminUpdater addAllToEmailAddress(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("emailAddress", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstEmailAddress() {
      updateOperations.removeFirst("emailAddress");
      return this;
    }
  
    public AdminUpdater removeLastEmailAddress() {
      updateOperations.removeLast("emailAddress");
      return this;
    }
  
    public AdminUpdater removeFromEmailAddress(java.lang.String value) {
      updateOperations.removeAll("emailAddress", value);
      return this;
    }

    public AdminUpdater removeAllFromEmailAddress(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("emailAddress", values);
      return this;
    }
 
    public AdminUpdater decEmailAddress() {
      updateOperations.dec("emailAddress");
      return this;
    }

    public AdminUpdater incEmailAddress() {
      updateOperations.inc("emailAddress");
      return this;
    }

    public AdminUpdater incEmailAddress(Number value) {
      updateOperations.inc("emailAddress", value);
      return this;
    }
    public AdminUpdater hostName(java.lang.String value) {
      updateOperations.set("hostName", value);
      return this;
    }

    public AdminUpdater unsetHostName(java.lang.String value) {
      updateOperations.unset("hostName");
      return this;
    }

    public AdminUpdater addHostName(java.lang.String value) {
      updateOperations.add("hostName", value);
      return this;
    }

    public AdminUpdater addHostName(java.lang.String value, boolean addDups) {
      updateOperations.add("hostName", value, addDups);
      return this;
    }

    public AdminUpdater addAllToHostName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("hostName", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstHostName() {
      updateOperations.removeFirst("hostName");
      return this;
    }
  
    public AdminUpdater removeLastHostName() {
      updateOperations.removeLast("hostName");
      return this;
    }
  
    public AdminUpdater removeFromHostName(java.lang.String value) {
      updateOperations.removeAll("hostName", value);
      return this;
    }

    public AdminUpdater removeAllFromHostName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("hostName", values);
      return this;
    }
 
    public AdminUpdater decHostName() {
      updateOperations.dec("hostName");
      return this;
    }

    public AdminUpdater incHostName() {
      updateOperations.inc("hostName");
      return this;
    }

    public AdminUpdater incHostName(Number value) {
      updateOperations.inc("hostName", value);
      return this;
    }
    public AdminUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public AdminUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public AdminUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public AdminUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public AdminUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public AdminUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public AdminUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public AdminUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public AdminUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public AdminUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public AdminUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public AdminUpdater ircName(java.lang.String value) {
      updateOperations.set("ircName", value);
      return this;
    }

    public AdminUpdater unsetIrcName(java.lang.String value) {
      updateOperations.unset("ircName");
      return this;
    }

    public AdminUpdater addIrcName(java.lang.String value) {
      updateOperations.add("ircName", value);
      return this;
    }

    public AdminUpdater addIrcName(java.lang.String value, boolean addDups) {
      updateOperations.add("ircName", value, addDups);
      return this;
    }

    public AdminUpdater addAllToIrcName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("ircName", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstIrcName() {
      updateOperations.removeFirst("ircName");
      return this;
    }
  
    public AdminUpdater removeLastIrcName() {
      updateOperations.removeLast("ircName");
      return this;
    }
  
    public AdminUpdater removeFromIrcName(java.lang.String value) {
      updateOperations.removeAll("ircName", value);
      return this;
    }

    public AdminUpdater removeAllFromIrcName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("ircName", values);
      return this;
    }
 
    public AdminUpdater decIrcName() {
      updateOperations.dec("ircName");
      return this;
    }

    public AdminUpdater incIrcName() {
      updateOperations.inc("ircName");
      return this;
    }

    public AdminUpdater incIrcName(Number value) {
      updateOperations.inc("ircName", value);
      return this;
    }
    public AdminUpdater updated(java.time.LocalDateTime value) {
      updateOperations.set("updated", value);
      return this;
    }

    public AdminUpdater unsetUpdated(java.time.LocalDateTime value) {
      updateOperations.unset("updated");
      return this;
    }

    public AdminUpdater addUpdated(java.time.LocalDateTime value) {
      updateOperations.add("updated", value);
      return this;
    }

    public AdminUpdater addUpdated(java.time.LocalDateTime value, boolean addDups) {
      updateOperations.add("updated", value, addDups);
      return this;
    }

    public AdminUpdater addAllToUpdated(java.util.List<java.time.LocalDateTime> values, boolean addDups) {
      updateOperations.addAll("updated", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstUpdated() {
      updateOperations.removeFirst("updated");
      return this;
    }
  
    public AdminUpdater removeLastUpdated() {
      updateOperations.removeLast("updated");
      return this;
    }
  
    public AdminUpdater removeFromUpdated(java.time.LocalDateTime value) {
      updateOperations.removeAll("updated", value);
      return this;
    }

    public AdminUpdater removeAllFromUpdated(java.util.List<java.time.LocalDateTime> values) {
      updateOperations.removeAll("updated", values);
      return this;
    }
 
    public AdminUpdater decUpdated() {
      updateOperations.dec("updated");
      return this;
    }

    public AdminUpdater incUpdated() {
      updateOperations.inc("updated");
      return this;
    }

    public AdminUpdater incUpdated(Number value) {
      updateOperations.inc("updated", value);
      return this;
    }
  }
}
