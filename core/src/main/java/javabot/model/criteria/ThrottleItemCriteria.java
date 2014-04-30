package javabot.model.criteria;

import javabot.model.ThrottleItem;

public class ThrottleItemCriteria extends com.antwerkz.critter.criteria.BaseCriteria<ThrottleItem> {
  private String prefix = "";

  public ThrottleItemCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, ThrottleItem.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, java.lang.String> user() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, java.lang.String>(this, query, prefix + "user");
  }

  public org.mongodb.morphia.query.Criteria user(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, java.lang.String>(this, query, prefix + "user").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, java.util.Date> when() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, java.util.Date>(this, query, prefix + "when");
  }

  public org.mongodb.morphia.query.Criteria when(java.util.Date value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, java.util.Date>(this, query, prefix + "when").equal(value);
  }


  public ThrottleItemUpdater getUpdater() {
    return new ThrottleItemUpdater();
  }

  public class ThrottleItemUpdater {
    org.mongodb.morphia.query.UpdateOperations<ThrottleItem> updateOperations;

    public ThrottleItemUpdater() {
      updateOperations = ds.createUpdateOperations(ThrottleItem.class);
    }

    public org.mongodb.morphia.query.UpdateResults<ThrottleItem> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<ThrottleItem> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<ThrottleItem> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<ThrottleItem> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public ThrottleItemUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public ThrottleItemUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public ThrottleItemUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public ThrottleItemUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ThrottleItemUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public ThrottleItemUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public ThrottleItemUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public ThrottleItemUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public ThrottleItemUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public ThrottleItemUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public ThrottleItemUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public ThrottleItemUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public ThrottleItemUpdater user(java.lang.String value) {
      updateOperations.set("user", value);
      return this;
    }

    public ThrottleItemUpdater unsetUser(java.lang.String value) {
      updateOperations.unset("user");
      return this;
    }

    public ThrottleItemUpdater addUser(java.lang.String value) {
      updateOperations.add("user", value);
      return this;
    }

    public ThrottleItemUpdater addUser(java.lang.String value, boolean addDups) {
      updateOperations.add("user", value, addDups);
      return this;
    }

    public ThrottleItemUpdater addAllToUser(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("user", values, addDups);
      return this;
    }
  
    public ThrottleItemUpdater removeFirstUser() {
      updateOperations.removeFirst("user");
      return this;
    }
  
    public ThrottleItemUpdater removeLastUser() {
      updateOperations.removeLast("user");
      return this;
    }
  
    public ThrottleItemUpdater removeFromUser(java.lang.String value) {
      updateOperations.removeAll("user", value);
      return this;
    }

    public ThrottleItemUpdater removeAllFromUser(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("user", values);
      return this;
    }
 
    public ThrottleItemUpdater decUser() {
      updateOperations.dec("user");
      return this;
    }

    public ThrottleItemUpdater incUser() {
      updateOperations.inc("user");
      return this;
    }

    public ThrottleItemUpdater incUser(Number value) {
      updateOperations.inc("user", value);
      return this;
    }
    public ThrottleItemUpdater when(java.util.Date value) {
      updateOperations.set("when", value);
      return this;
    }

    public ThrottleItemUpdater unsetWhen(java.util.Date value) {
      updateOperations.unset("when");
      return this;
    }

    public ThrottleItemUpdater addWhen(java.util.Date value) {
      updateOperations.add("when", value);
      return this;
    }

    public ThrottleItemUpdater addWhen(java.util.Date value, boolean addDups) {
      updateOperations.add("when", value, addDups);
      return this;
    }

    public ThrottleItemUpdater addAllToWhen(java.util.List<java.util.Date> values, boolean addDups) {
      updateOperations.addAll("when", values, addDups);
      return this;
    }
  
    public ThrottleItemUpdater removeFirstWhen() {
      updateOperations.removeFirst("when");
      return this;
    }
  
    public ThrottleItemUpdater removeLastWhen() {
      updateOperations.removeLast("when");
      return this;
    }
  
    public ThrottleItemUpdater removeFromWhen(java.util.Date value) {
      updateOperations.removeAll("when", value);
      return this;
    }

    public ThrottleItemUpdater removeAllFromWhen(java.util.List<java.util.Date> values) {
      updateOperations.removeAll("when", values);
      return this;
    }
 
    public ThrottleItemUpdater decWhen() {
      updateOperations.dec("when");
      return this;
    }

    public ThrottleItemUpdater incWhen() {
      updateOperations.inc("when");
      return this;
    }

    public ThrottleItemUpdater incWhen(Number value) {
      updateOperations.inc("when", value);
      return this;
    }
  }
}
