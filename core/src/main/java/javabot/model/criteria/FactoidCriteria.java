package javabot.model.criteria;

import javabot.model.Factoid;

public class FactoidCriteria extends com.antwerkz.critter.criteria.BaseCriteria<Factoid> {
  private String prefix = "";

  public FactoidCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, Factoid.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime> lastUsed() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime>(this, query, prefix + "lastUsed");
  }

  public org.mongodb.morphia.query.Criteria lastUsed(java.time.LocalDateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime>(this, query, prefix + "lastUsed").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.Boolean> locked() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.Boolean>(this, query, prefix + "locked");
  }

  public org.mongodb.morphia.query.Criteria locked(java.lang.Boolean value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.Boolean>(this, query, prefix + "locked").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String> name() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String>(this, query, prefix + "name");
  }

  public org.mongodb.morphia.query.Criteria name(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String>(this, query, prefix + "name").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime> updated() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime>(this, query, prefix + "updated");
  }

  public org.mongodb.morphia.query.Criteria updated(java.time.LocalDateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime>(this, query, prefix + "updated").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String> upperName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String>(this, query, prefix + "upperName");
  }

  public org.mongodb.morphia.query.Criteria upperName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String>(this, query, prefix + "upperName").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String> upperUserName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String>(this, query, prefix + "upperUserName");
  }

  public org.mongodb.morphia.query.Criteria upperUserName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String>(this, query, prefix + "upperUserName").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String> upperValue() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String>(this, query, prefix + "upperValue");
  }

  public org.mongodb.morphia.query.Criteria upperValue(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String>(this, query, prefix + "upperValue").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String> userName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String>(this, query, prefix + "userName");
  }

  public org.mongodb.morphia.query.Criteria userName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String>(this, query, prefix + "userName").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String> value() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String>(this, query, prefix + "value");
  }

  public org.mongodb.morphia.query.Criteria value(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<FactoidCriteria, Factoid, java.lang.String>(this, query, prefix + "value").equal(value);
  }


  public FactoidUpdater getUpdater() {
    return new FactoidUpdater();
  }

  public class FactoidUpdater {
    org.mongodb.morphia.query.UpdateOperations<Factoid> updateOperations;

    public FactoidUpdater() {
      updateOperations = ds.createUpdateOperations(Factoid.class);
    }

    public org.mongodb.morphia.query.UpdateResults<Factoid> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<Factoid> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<Factoid> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<Factoid> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public FactoidUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public FactoidUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public FactoidUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public FactoidUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public FactoidUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public FactoidUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public FactoidUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public FactoidUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public FactoidUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public FactoidUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public FactoidUpdater lastUsed(java.time.LocalDateTime value) {
      updateOperations.set("lastUsed", value);
      return this;
    }

    public FactoidUpdater unsetLastUsed(java.time.LocalDateTime value) {
      updateOperations.unset("lastUsed");
      return this;
    }

    public FactoidUpdater addLastUsed(java.time.LocalDateTime value) {
      updateOperations.add("lastUsed", value);
      return this;
    }

    public FactoidUpdater addLastUsed(java.time.LocalDateTime value, boolean addDups) {
      updateOperations.add("lastUsed", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToLastUsed(java.util.List<java.time.LocalDateTime> values, boolean addDups) {
      updateOperations.addAll("lastUsed", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstLastUsed() {
      updateOperations.removeFirst("lastUsed");
      return this;
    }
  
    public FactoidUpdater removeLastLastUsed() {
      updateOperations.removeLast("lastUsed");
      return this;
    }
  
    public FactoidUpdater removeFromLastUsed(java.time.LocalDateTime value) {
      updateOperations.removeAll("lastUsed", value);
      return this;
    }

    public FactoidUpdater removeAllFromLastUsed(java.util.List<java.time.LocalDateTime> values) {
      updateOperations.removeAll("lastUsed", values);
      return this;
    }
 
    public FactoidUpdater decLastUsed() {
      updateOperations.dec("lastUsed");
      return this;
    }

    public FactoidUpdater incLastUsed() {
      updateOperations.inc("lastUsed");
      return this;
    }

    public FactoidUpdater incLastUsed(Number value) {
      updateOperations.inc("lastUsed", value);
      return this;
    }
    public FactoidUpdater locked(java.lang.Boolean value) {
      updateOperations.set("locked", value);
      return this;
    }

    public FactoidUpdater unsetLocked(java.lang.Boolean value) {
      updateOperations.unset("locked");
      return this;
    }

    public FactoidUpdater addLocked(java.lang.Boolean value) {
      updateOperations.add("locked", value);
      return this;
    }

    public FactoidUpdater addLocked(java.lang.Boolean value, boolean addDups) {
      updateOperations.add("locked", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToLocked(java.util.List<java.lang.Boolean> values, boolean addDups) {
      updateOperations.addAll("locked", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstLocked() {
      updateOperations.removeFirst("locked");
      return this;
    }
  
    public FactoidUpdater removeLastLocked() {
      updateOperations.removeLast("locked");
      return this;
    }
  
    public FactoidUpdater removeFromLocked(java.lang.Boolean value) {
      updateOperations.removeAll("locked", value);
      return this;
    }

    public FactoidUpdater removeAllFromLocked(java.util.List<java.lang.Boolean> values) {
      updateOperations.removeAll("locked", values);
      return this;
    }
 
    public FactoidUpdater decLocked() {
      updateOperations.dec("locked");
      return this;
    }

    public FactoidUpdater incLocked() {
      updateOperations.inc("locked");
      return this;
    }

    public FactoidUpdater incLocked(Number value) {
      updateOperations.inc("locked", value);
      return this;
    }
    public FactoidUpdater name(java.lang.String value) {
      updateOperations.set("name", value);
      return this;
    }

    public FactoidUpdater unsetName(java.lang.String value) {
      updateOperations.unset("name");
      return this;
    }

    public FactoidUpdater addName(java.lang.String value) {
      updateOperations.add("name", value);
      return this;
    }

    public FactoidUpdater addName(java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("name", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstName() {
      updateOperations.removeFirst("name");
      return this;
    }
  
    public FactoidUpdater removeLastName() {
      updateOperations.removeLast("name");
      return this;
    }
  
    public FactoidUpdater removeFromName(java.lang.String value) {
      updateOperations.removeAll("name", value);
      return this;
    }

    public FactoidUpdater removeAllFromName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("name", values);
      return this;
    }
 
    public FactoidUpdater decName() {
      updateOperations.dec("name");
      return this;
    }

    public FactoidUpdater incName() {
      updateOperations.inc("name");
      return this;
    }

    public FactoidUpdater incName(Number value) {
      updateOperations.inc("name", value);
      return this;
    }
    public FactoidUpdater updated(java.time.LocalDateTime value) {
      updateOperations.set("updated", value);
      return this;
    }

    public FactoidUpdater unsetUpdated(java.time.LocalDateTime value) {
      updateOperations.unset("updated");
      return this;
    }

    public FactoidUpdater addUpdated(java.time.LocalDateTime value) {
      updateOperations.add("updated", value);
      return this;
    }

    public FactoidUpdater addUpdated(java.time.LocalDateTime value, boolean addDups) {
      updateOperations.add("updated", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToUpdated(java.util.List<java.time.LocalDateTime> values, boolean addDups) {
      updateOperations.addAll("updated", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstUpdated() {
      updateOperations.removeFirst("updated");
      return this;
    }
  
    public FactoidUpdater removeLastUpdated() {
      updateOperations.removeLast("updated");
      return this;
    }
  
    public FactoidUpdater removeFromUpdated(java.time.LocalDateTime value) {
      updateOperations.removeAll("updated", value);
      return this;
    }

    public FactoidUpdater removeAllFromUpdated(java.util.List<java.time.LocalDateTime> values) {
      updateOperations.removeAll("updated", values);
      return this;
    }
 
    public FactoidUpdater decUpdated() {
      updateOperations.dec("updated");
      return this;
    }

    public FactoidUpdater incUpdated() {
      updateOperations.inc("updated");
      return this;
    }

    public FactoidUpdater incUpdated(Number value) {
      updateOperations.inc("updated", value);
      return this;
    }
    public FactoidUpdater upperName(java.lang.String value) {
      updateOperations.set("upperName", value);
      return this;
    }

    public FactoidUpdater unsetUpperName(java.lang.String value) {
      updateOperations.unset("upperName");
      return this;
    }

    public FactoidUpdater addUpperName(java.lang.String value) {
      updateOperations.add("upperName", value);
      return this;
    }

    public FactoidUpdater addUpperName(java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToUpperName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperName", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstUpperName() {
      updateOperations.removeFirst("upperName");
      return this;
    }
  
    public FactoidUpdater removeLastUpperName() {
      updateOperations.removeLast("upperName");
      return this;
    }
  
    public FactoidUpdater removeFromUpperName(java.lang.String value) {
      updateOperations.removeAll("upperName", value);
      return this;
    }

    public FactoidUpdater removeAllFromUpperName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("upperName", values);
      return this;
    }
 
    public FactoidUpdater decUpperName() {
      updateOperations.dec("upperName");
      return this;
    }

    public FactoidUpdater incUpperName() {
      updateOperations.inc("upperName");
      return this;
    }

    public FactoidUpdater incUpperName(Number value) {
      updateOperations.inc("upperName", value);
      return this;
    }
    public FactoidUpdater upperUserName(java.lang.String value) {
      updateOperations.set("upperUserName", value);
      return this;
    }

    public FactoidUpdater unsetUpperUserName(java.lang.String value) {
      updateOperations.unset("upperUserName");
      return this;
    }

    public FactoidUpdater addUpperUserName(java.lang.String value) {
      updateOperations.add("upperUserName", value);
      return this;
    }

    public FactoidUpdater addUpperUserName(java.lang.String value, boolean addDups) {
      updateOperations.add("upperUserName", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToUpperUserName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperUserName", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstUpperUserName() {
      updateOperations.removeFirst("upperUserName");
      return this;
    }
  
    public FactoidUpdater removeLastUpperUserName() {
      updateOperations.removeLast("upperUserName");
      return this;
    }
  
    public FactoidUpdater removeFromUpperUserName(java.lang.String value) {
      updateOperations.removeAll("upperUserName", value);
      return this;
    }

    public FactoidUpdater removeAllFromUpperUserName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("upperUserName", values);
      return this;
    }
 
    public FactoidUpdater decUpperUserName() {
      updateOperations.dec("upperUserName");
      return this;
    }

    public FactoidUpdater incUpperUserName() {
      updateOperations.inc("upperUserName");
      return this;
    }

    public FactoidUpdater incUpperUserName(Number value) {
      updateOperations.inc("upperUserName", value);
      return this;
    }
    public FactoidUpdater upperValue(java.lang.String value) {
      updateOperations.set("upperValue", value);
      return this;
    }

    public FactoidUpdater unsetUpperValue(java.lang.String value) {
      updateOperations.unset("upperValue");
      return this;
    }

    public FactoidUpdater addUpperValue(java.lang.String value) {
      updateOperations.add("upperValue", value);
      return this;
    }

    public FactoidUpdater addUpperValue(java.lang.String value, boolean addDups) {
      updateOperations.add("upperValue", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToUpperValue(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperValue", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstUpperValue() {
      updateOperations.removeFirst("upperValue");
      return this;
    }
  
    public FactoidUpdater removeLastUpperValue() {
      updateOperations.removeLast("upperValue");
      return this;
    }
  
    public FactoidUpdater removeFromUpperValue(java.lang.String value) {
      updateOperations.removeAll("upperValue", value);
      return this;
    }

    public FactoidUpdater removeAllFromUpperValue(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("upperValue", values);
      return this;
    }
 
    public FactoidUpdater decUpperValue() {
      updateOperations.dec("upperValue");
      return this;
    }

    public FactoidUpdater incUpperValue() {
      updateOperations.inc("upperValue");
      return this;
    }

    public FactoidUpdater incUpperValue(Number value) {
      updateOperations.inc("upperValue", value);
      return this;
    }
    public FactoidUpdater userName(java.lang.String value) {
      updateOperations.set("userName", value);
      return this;
    }

    public FactoidUpdater unsetUserName(java.lang.String value) {
      updateOperations.unset("userName");
      return this;
    }

    public FactoidUpdater addUserName(java.lang.String value) {
      updateOperations.add("userName", value);
      return this;
    }

    public FactoidUpdater addUserName(java.lang.String value, boolean addDups) {
      updateOperations.add("userName", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToUserName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("userName", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstUserName() {
      updateOperations.removeFirst("userName");
      return this;
    }
  
    public FactoidUpdater removeLastUserName() {
      updateOperations.removeLast("userName");
      return this;
    }
  
    public FactoidUpdater removeFromUserName(java.lang.String value) {
      updateOperations.removeAll("userName", value);
      return this;
    }

    public FactoidUpdater removeAllFromUserName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("userName", values);
      return this;
    }
 
    public FactoidUpdater decUserName() {
      updateOperations.dec("userName");
      return this;
    }

    public FactoidUpdater incUserName() {
      updateOperations.inc("userName");
      return this;
    }

    public FactoidUpdater incUserName(Number value) {
      updateOperations.inc("userName", value);
      return this;
    }
    public FactoidUpdater value(java.lang.String value) {
      updateOperations.set("value", value);
      return this;
    }

    public FactoidUpdater unsetValue(java.lang.String value) {
      updateOperations.unset("value");
      return this;
    }

    public FactoidUpdater addValue(java.lang.String value) {
      updateOperations.add("value", value);
      return this;
    }

    public FactoidUpdater addValue(java.lang.String value, boolean addDups) {
      updateOperations.add("value", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToValue(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("value", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstValue() {
      updateOperations.removeFirst("value");
      return this;
    }
  
    public FactoidUpdater removeLastValue() {
      updateOperations.removeLast("value");
      return this;
    }
  
    public FactoidUpdater removeFromValue(java.lang.String value) {
      updateOperations.removeAll("value", value);
      return this;
    }

    public FactoidUpdater removeAllFromValue(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("value", values);
      return this;
    }
 
    public FactoidUpdater decValue() {
      updateOperations.dec("value");
      return this;
    }

    public FactoidUpdater incValue() {
      updateOperations.inc("value");
      return this;
    }

    public FactoidUpdater incValue(Number value) {
      updateOperations.inc("value", value);
      return this;
    }
  }
}
