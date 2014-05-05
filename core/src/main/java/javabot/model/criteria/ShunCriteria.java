package javabot.model.criteria;

import javabot.model.Shun;

public class ShunCriteria extends com.antwerkz.critter.criteria.BaseCriteria<Shun> {
  private String prefix = "";

  public ShunCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, Shun.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<ShunCriteria, Shun, java.time.LocalDateTime> expiry() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ShunCriteria, Shun, java.time.LocalDateTime>(this, query, prefix + "expiry");
  }

  public org.mongodb.morphia.query.Criteria expiry(java.time.LocalDateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ShunCriteria, Shun, java.time.LocalDateTime>(this, query, prefix + "expiry").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ShunCriteria, Shun, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ShunCriteria, Shun, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ShunCriteria, Shun, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ShunCriteria, Shun, java.lang.String> nick() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ShunCriteria, Shun, java.lang.String>(this, query, prefix + "nick");
  }

  public org.mongodb.morphia.query.Criteria nick(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ShunCriteria, Shun, java.lang.String>(this, query, prefix + "nick").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ShunCriteria, Shun, java.lang.String> upperNick() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ShunCriteria, Shun, java.lang.String>(this, query, prefix + "upperNick");
  }

  public org.mongodb.morphia.query.Criteria upperNick(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ShunCriteria, Shun, java.lang.String>(this, query, prefix + "upperNick").equal(value);
  }


  public ShunUpdater getUpdater() {
    return new ShunUpdater();
  }

  public class ShunUpdater {
    org.mongodb.morphia.query.UpdateOperations<Shun> updateOperations;

    public ShunUpdater() {
      updateOperations = ds.createUpdateOperations(Shun.class);
    }

    public org.mongodb.morphia.query.UpdateResults<Shun> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<Shun> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<Shun> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<Shun> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public ShunUpdater expiry(java.time.LocalDateTime value) {
      updateOperations.set("expiry", value);
      return this;
    }

    public ShunUpdater unsetExpiry(java.time.LocalDateTime value) {
      updateOperations.unset("expiry");
      return this;
    }

    public ShunUpdater addExpiry(java.time.LocalDateTime value) {
      updateOperations.add("expiry", value);
      return this;
    }

    public ShunUpdater addExpiry(java.time.LocalDateTime value, boolean addDups) {
      updateOperations.add("expiry", value, addDups);
      return this;
    }

    public ShunUpdater addAllToExpiry(java.util.List<java.time.LocalDateTime> values, boolean addDups) {
      updateOperations.addAll("expiry", values, addDups);
      return this;
    }
  
    public ShunUpdater removeFirstExpiry() {
      updateOperations.removeFirst("expiry");
      return this;
    }
  
    public ShunUpdater removeLastExpiry() {
      updateOperations.removeLast("expiry");
      return this;
    }
  
    public ShunUpdater removeFromExpiry(java.time.LocalDateTime value) {
      updateOperations.removeAll("expiry", value);
      return this;
    }

    public ShunUpdater removeAllFromExpiry(java.util.List<java.time.LocalDateTime> values) {
      updateOperations.removeAll("expiry", values);
      return this;
    }
 
    public ShunUpdater decExpiry() {
      updateOperations.dec("expiry");
      return this;
    }

    public ShunUpdater incExpiry() {
      updateOperations.inc("expiry");
      return this;
    }

    public ShunUpdater incExpiry(Number value) {
      updateOperations.inc("expiry", value);
      return this;
    }
    public ShunUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public ShunUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public ShunUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public ShunUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ShunUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public ShunUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public ShunUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public ShunUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public ShunUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public ShunUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public ShunUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public ShunUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public ShunUpdater nick(java.lang.String value) {
      updateOperations.set("nick", value);
      return this;
    }

    public ShunUpdater unsetNick(java.lang.String value) {
      updateOperations.unset("nick");
      return this;
    }

    public ShunUpdater addNick(java.lang.String value) {
      updateOperations.add("nick", value);
      return this;
    }

    public ShunUpdater addNick(java.lang.String value, boolean addDups) {
      updateOperations.add("nick", value, addDups);
      return this;
    }

    public ShunUpdater addAllToNick(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("nick", values, addDups);
      return this;
    }
  
    public ShunUpdater removeFirstNick() {
      updateOperations.removeFirst("nick");
      return this;
    }
  
    public ShunUpdater removeLastNick() {
      updateOperations.removeLast("nick");
      return this;
    }
  
    public ShunUpdater removeFromNick(java.lang.String value) {
      updateOperations.removeAll("nick", value);
      return this;
    }

    public ShunUpdater removeAllFromNick(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("nick", values);
      return this;
    }
 
    public ShunUpdater decNick() {
      updateOperations.dec("nick");
      return this;
    }

    public ShunUpdater incNick() {
      updateOperations.inc("nick");
      return this;
    }

    public ShunUpdater incNick(Number value) {
      updateOperations.inc("nick", value);
      return this;
    }
    public ShunUpdater upperNick(java.lang.String value) {
      updateOperations.set("upperNick", value);
      return this;
    }

    public ShunUpdater unsetUpperNick(java.lang.String value) {
      updateOperations.unset("upperNick");
      return this;
    }

    public ShunUpdater addUpperNick(java.lang.String value) {
      updateOperations.add("upperNick", value);
      return this;
    }

    public ShunUpdater addUpperNick(java.lang.String value, boolean addDups) {
      updateOperations.add("upperNick", value, addDups);
      return this;
    }

    public ShunUpdater addAllToUpperNick(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperNick", values, addDups);
      return this;
    }
  
    public ShunUpdater removeFirstUpperNick() {
      updateOperations.removeFirst("upperNick");
      return this;
    }
  
    public ShunUpdater removeLastUpperNick() {
      updateOperations.removeLast("upperNick");
      return this;
    }
  
    public ShunUpdater removeFromUpperNick(java.lang.String value) {
      updateOperations.removeAll("upperNick", value);
      return this;
    }

    public ShunUpdater removeAllFromUpperNick(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("upperNick", values);
      return this;
    }
 
    public ShunUpdater decUpperNick() {
      updateOperations.dec("upperNick");
      return this;
    }

    public ShunUpdater incUpperNick() {
      updateOperations.inc("upperNick");
      return this;
    }

    public ShunUpdater incUpperNick(Number value) {
      updateOperations.inc("upperNick", value);
      return this;
    }
  }
}
