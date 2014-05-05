package javabot.model.criteria;

import javabot.model.NickServInfo;

public class NickServInfoCriteria extends com.antwerkz.critter.criteria.BaseCriteria<NickServInfo> {
  private String prefix = "";

  public NickServInfoCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, NickServInfo.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.lang.String> account() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.lang.String>(this, query, prefix + "account");
  }

  public org.mongodb.morphia.query.Criteria account(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.lang.String>(this, query, prefix + "account").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.util.Map<java.lang.String,java.lang.String>> extraneous() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.util.Map<java.lang.String,java.lang.String>>(this, query, prefix + "extraneous");
  }

  public org.mongodb.morphia.query.Criteria extraneous(java.util.Map<java.lang.String,java.lang.String> value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.util.Map<java.lang.String,java.lang.String>>(this, query, prefix + "extraneous").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.lang.String> lastAddress() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.lang.String>(this, query, prefix + "lastAddress");
  }

  public org.mongodb.morphia.query.Criteria lastAddress(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.lang.String>(this, query, prefix + "lastAddress").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime> lastSeen() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(this, query, prefix + "lastSeen");
  }

  public org.mongodb.morphia.query.Criteria lastSeen(java.time.LocalDateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(this, query, prefix + "lastSeen").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.lang.String> nick() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.lang.String>(this, query, prefix + "nick");
  }

  public org.mongodb.morphia.query.Criteria nick(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.lang.String>(this, query, prefix + "nick").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime> registered() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(this, query, prefix + "registered");
  }

  public org.mongodb.morphia.query.Criteria registered(java.time.LocalDateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(this, query, prefix + "registered").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime> userRegistered() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(this, query, prefix + "userRegistered");
  }

  public org.mongodb.morphia.query.Criteria userRegistered(java.time.LocalDateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(this, query, prefix + "userRegistered").equal(value);
  }


  public NickServInfoUpdater getUpdater() {
    return new NickServInfoUpdater();
  }

  public class NickServInfoUpdater {
    org.mongodb.morphia.query.UpdateOperations<NickServInfo> updateOperations;

    public NickServInfoUpdater() {
      updateOperations = ds.createUpdateOperations(NickServInfo.class);
    }

    public org.mongodb.morphia.query.UpdateResults<NickServInfo> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<NickServInfo> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<NickServInfo> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<NickServInfo> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public NickServInfoUpdater account(java.lang.String value) {
      updateOperations.set("account", value);
      return this;
    }

    public NickServInfoUpdater unsetAccount(java.lang.String value) {
      updateOperations.unset("account");
      return this;
    }

    public NickServInfoUpdater addAccount(java.lang.String value) {
      updateOperations.add("account", value);
      return this;
    }

    public NickServInfoUpdater addAccount(java.lang.String value, boolean addDups) {
      updateOperations.add("account", value, addDups);
      return this;
    }

    public NickServInfoUpdater addAllToAccount(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("account", values, addDups);
      return this;
    }
  
    public NickServInfoUpdater removeFirstAccount() {
      updateOperations.removeFirst("account");
      return this;
    }
  
    public NickServInfoUpdater removeLastAccount() {
      updateOperations.removeLast("account");
      return this;
    }
  
    public NickServInfoUpdater removeFromAccount(java.lang.String value) {
      updateOperations.removeAll("account", value);
      return this;
    }

    public NickServInfoUpdater removeAllFromAccount(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("account", values);
      return this;
    }
 
    public NickServInfoUpdater decAccount() {
      updateOperations.dec("account");
      return this;
    }

    public NickServInfoUpdater incAccount() {
      updateOperations.inc("account");
      return this;
    }

    public NickServInfoUpdater incAccount(Number value) {
      updateOperations.inc("account", value);
      return this;
    }
    public NickServInfoUpdater extraneous(java.util.Map<java.lang.String,java.lang.String> value) {
      updateOperations.set("extraneous", value);
      return this;
    }

    public NickServInfoUpdater unsetExtraneous(java.util.Map<java.lang.String,java.lang.String> value) {
      updateOperations.unset("extraneous");
      return this;
    }

    public NickServInfoUpdater addExtraneous(java.util.Map<java.lang.String,java.lang.String> value) {
      updateOperations.add("extraneous", value);
      return this;
    }

    public NickServInfoUpdater addExtraneous(java.util.Map<java.lang.String,java.lang.String> value, boolean addDups) {
      updateOperations.add("extraneous", value, addDups);
      return this;
    }

    public NickServInfoUpdater addAllToExtraneous(java.util.List<java.util.Map<java.lang.String,java.lang.String>> values, boolean addDups) {
      updateOperations.addAll("extraneous", values, addDups);
      return this;
    }
  
    public NickServInfoUpdater removeFirstExtraneous() {
      updateOperations.removeFirst("extraneous");
      return this;
    }
  
    public NickServInfoUpdater removeLastExtraneous() {
      updateOperations.removeLast("extraneous");
      return this;
    }
  
    public NickServInfoUpdater removeFromExtraneous(java.util.Map<java.lang.String,java.lang.String> value) {
      updateOperations.removeAll("extraneous", value);
      return this;
    }

    public NickServInfoUpdater removeAllFromExtraneous(java.util.List<java.util.Map<java.lang.String,java.lang.String>> values) {
      updateOperations.removeAll("extraneous", values);
      return this;
    }
 
    public NickServInfoUpdater decExtraneous() {
      updateOperations.dec("extraneous");
      return this;
    }

    public NickServInfoUpdater incExtraneous() {
      updateOperations.inc("extraneous");
      return this;
    }

    public NickServInfoUpdater incExtraneous(Number value) {
      updateOperations.inc("extraneous", value);
      return this;
    }
    public NickServInfoUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public NickServInfoUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public NickServInfoUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public NickServInfoUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public NickServInfoUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public NickServInfoUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public NickServInfoUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public NickServInfoUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public NickServInfoUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public NickServInfoUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public NickServInfoUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public NickServInfoUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public NickServInfoUpdater lastAddress(java.lang.String value) {
      updateOperations.set("lastAddress", value);
      return this;
    }

    public NickServInfoUpdater unsetLastAddress(java.lang.String value) {
      updateOperations.unset("lastAddress");
      return this;
    }

    public NickServInfoUpdater addLastAddress(java.lang.String value) {
      updateOperations.add("lastAddress", value);
      return this;
    }

    public NickServInfoUpdater addLastAddress(java.lang.String value, boolean addDups) {
      updateOperations.add("lastAddress", value, addDups);
      return this;
    }

    public NickServInfoUpdater addAllToLastAddress(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("lastAddress", values, addDups);
      return this;
    }
  
    public NickServInfoUpdater removeFirstLastAddress() {
      updateOperations.removeFirst("lastAddress");
      return this;
    }
  
    public NickServInfoUpdater removeLastLastAddress() {
      updateOperations.removeLast("lastAddress");
      return this;
    }
  
    public NickServInfoUpdater removeFromLastAddress(java.lang.String value) {
      updateOperations.removeAll("lastAddress", value);
      return this;
    }

    public NickServInfoUpdater removeAllFromLastAddress(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("lastAddress", values);
      return this;
    }
 
    public NickServInfoUpdater decLastAddress() {
      updateOperations.dec("lastAddress");
      return this;
    }

    public NickServInfoUpdater incLastAddress() {
      updateOperations.inc("lastAddress");
      return this;
    }

    public NickServInfoUpdater incLastAddress(Number value) {
      updateOperations.inc("lastAddress", value);
      return this;
    }
    public NickServInfoUpdater lastSeen(java.time.LocalDateTime value) {
      updateOperations.set("lastSeen", value);
      return this;
    }

    public NickServInfoUpdater unsetLastSeen(java.time.LocalDateTime value) {
      updateOperations.unset("lastSeen");
      return this;
    }

    public NickServInfoUpdater addLastSeen(java.time.LocalDateTime value) {
      updateOperations.add("lastSeen", value);
      return this;
    }

    public NickServInfoUpdater addLastSeen(java.time.LocalDateTime value, boolean addDups) {
      updateOperations.add("lastSeen", value, addDups);
      return this;
    }

    public NickServInfoUpdater addAllToLastSeen(java.util.List<java.time.LocalDateTime> values, boolean addDups) {
      updateOperations.addAll("lastSeen", values, addDups);
      return this;
    }
  
    public NickServInfoUpdater removeFirstLastSeen() {
      updateOperations.removeFirst("lastSeen");
      return this;
    }
  
    public NickServInfoUpdater removeLastLastSeen() {
      updateOperations.removeLast("lastSeen");
      return this;
    }
  
    public NickServInfoUpdater removeFromLastSeen(java.time.LocalDateTime value) {
      updateOperations.removeAll("lastSeen", value);
      return this;
    }

    public NickServInfoUpdater removeAllFromLastSeen(java.util.List<java.time.LocalDateTime> values) {
      updateOperations.removeAll("lastSeen", values);
      return this;
    }
 
    public NickServInfoUpdater decLastSeen() {
      updateOperations.dec("lastSeen");
      return this;
    }

    public NickServInfoUpdater incLastSeen() {
      updateOperations.inc("lastSeen");
      return this;
    }

    public NickServInfoUpdater incLastSeen(Number value) {
      updateOperations.inc("lastSeen", value);
      return this;
    }
    public NickServInfoUpdater nick(java.lang.String value) {
      updateOperations.set("nick", value);
      return this;
    }

    public NickServInfoUpdater unsetNick(java.lang.String value) {
      updateOperations.unset("nick");
      return this;
    }

    public NickServInfoUpdater addNick(java.lang.String value) {
      updateOperations.add("nick", value);
      return this;
    }

    public NickServInfoUpdater addNick(java.lang.String value, boolean addDups) {
      updateOperations.add("nick", value, addDups);
      return this;
    }

    public NickServInfoUpdater addAllToNick(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("nick", values, addDups);
      return this;
    }
  
    public NickServInfoUpdater removeFirstNick() {
      updateOperations.removeFirst("nick");
      return this;
    }
  
    public NickServInfoUpdater removeLastNick() {
      updateOperations.removeLast("nick");
      return this;
    }
  
    public NickServInfoUpdater removeFromNick(java.lang.String value) {
      updateOperations.removeAll("nick", value);
      return this;
    }

    public NickServInfoUpdater removeAllFromNick(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("nick", values);
      return this;
    }
 
    public NickServInfoUpdater decNick() {
      updateOperations.dec("nick");
      return this;
    }

    public NickServInfoUpdater incNick() {
      updateOperations.inc("nick");
      return this;
    }

    public NickServInfoUpdater incNick(Number value) {
      updateOperations.inc("nick", value);
      return this;
    }
    public NickServInfoUpdater registered(java.time.LocalDateTime value) {
      updateOperations.set("registered", value);
      return this;
    }

    public NickServInfoUpdater unsetRegistered(java.time.LocalDateTime value) {
      updateOperations.unset("registered");
      return this;
    }

    public NickServInfoUpdater addRegistered(java.time.LocalDateTime value) {
      updateOperations.add("registered", value);
      return this;
    }

    public NickServInfoUpdater addRegistered(java.time.LocalDateTime value, boolean addDups) {
      updateOperations.add("registered", value, addDups);
      return this;
    }

    public NickServInfoUpdater addAllToRegistered(java.util.List<java.time.LocalDateTime> values, boolean addDups) {
      updateOperations.addAll("registered", values, addDups);
      return this;
    }
  
    public NickServInfoUpdater removeFirstRegistered() {
      updateOperations.removeFirst("registered");
      return this;
    }
  
    public NickServInfoUpdater removeLastRegistered() {
      updateOperations.removeLast("registered");
      return this;
    }
  
    public NickServInfoUpdater removeFromRegistered(java.time.LocalDateTime value) {
      updateOperations.removeAll("registered", value);
      return this;
    }

    public NickServInfoUpdater removeAllFromRegistered(java.util.List<java.time.LocalDateTime> values) {
      updateOperations.removeAll("registered", values);
      return this;
    }
 
    public NickServInfoUpdater decRegistered() {
      updateOperations.dec("registered");
      return this;
    }

    public NickServInfoUpdater incRegistered() {
      updateOperations.inc("registered");
      return this;
    }

    public NickServInfoUpdater incRegistered(Number value) {
      updateOperations.inc("registered", value);
      return this;
    }
    public NickServInfoUpdater userRegistered(java.time.LocalDateTime value) {
      updateOperations.set("userRegistered", value);
      return this;
    }

    public NickServInfoUpdater unsetUserRegistered(java.time.LocalDateTime value) {
      updateOperations.unset("userRegistered");
      return this;
    }

    public NickServInfoUpdater addUserRegistered(java.time.LocalDateTime value) {
      updateOperations.add("userRegistered", value);
      return this;
    }

    public NickServInfoUpdater addUserRegistered(java.time.LocalDateTime value, boolean addDups) {
      updateOperations.add("userRegistered", value, addDups);
      return this;
    }

    public NickServInfoUpdater addAllToUserRegistered(java.util.List<java.time.LocalDateTime> values, boolean addDups) {
      updateOperations.addAll("userRegistered", values, addDups);
      return this;
    }
  
    public NickServInfoUpdater removeFirstUserRegistered() {
      updateOperations.removeFirst("userRegistered");
      return this;
    }
  
    public NickServInfoUpdater removeLastUserRegistered() {
      updateOperations.removeLast("userRegistered");
      return this;
    }
  
    public NickServInfoUpdater removeFromUserRegistered(java.time.LocalDateTime value) {
      updateOperations.removeAll("userRegistered", value);
      return this;
    }

    public NickServInfoUpdater removeAllFromUserRegistered(java.util.List<java.time.LocalDateTime> values) {
      updateOperations.removeAll("userRegistered", values);
      return this;
    }
 
    public NickServInfoUpdater decUserRegistered() {
      updateOperations.dec("userRegistered");
      return this;
    }

    public NickServInfoUpdater incUserRegistered() {
      updateOperations.inc("userRegistered");
      return this;
    }

    public NickServInfoUpdater incUserRegistered(Number value) {
      updateOperations.inc("userRegistered", value);
      return this;
    }
  }
}
