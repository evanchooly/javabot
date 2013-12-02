package javabot.model.criteria;

import javabot.model.Channel;

public class ChannelCriteria extends com.antwerkz.critter.criteria.BaseCriteria<Channel> {
  private String prefix = "";

  public ChannelCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, Channel.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, java.lang.String> key() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, java.lang.String>(this, query, prefix + "key");
  }

  public org.mongodb.morphia.query.Criteria key(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, java.lang.String>(this, query, prefix + "key").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, java.lang.Boolean> logged() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, java.lang.Boolean>(this, query, prefix + "logged");
  }

  public org.mongodb.morphia.query.Criteria logged(java.lang.Boolean value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, java.lang.Boolean>(this, query, prefix + "logged").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, java.lang.String> name() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, java.lang.String>(this, query, prefix + "name");
  }

  public org.mongodb.morphia.query.Criteria name(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, java.lang.String>(this, query, prefix + "name").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, org.joda.time.DateTime> updated() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, org.joda.time.DateTime>(this, query, prefix + "updated");
  }

  public org.mongodb.morphia.query.Criteria updated(org.joda.time.DateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, org.joda.time.DateTime>(this, query, prefix + "updated").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, java.lang.String> upperName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, java.lang.String>(this, query, prefix + "upperName");
  }

  public org.mongodb.morphia.query.Criteria upperName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelCriteria, Channel, java.lang.String>(this, query, prefix + "upperName").equal(value);
  }


  public ChannelUpdater getUpdater() {
    return new ChannelUpdater();
  }

  public class ChannelUpdater {
    org.mongodb.morphia.query.UpdateOperations<Channel> updateOperations;

    public ChannelUpdater() {
      updateOperations = ds.createUpdateOperations(Channel.class);
    }

    public org.mongodb.morphia.query.UpdateResults<Channel> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<Channel> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<Channel> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<Channel> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public ChannelUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public ChannelUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public ChannelUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public ChannelUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ChannelUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public ChannelUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public ChannelUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public ChannelUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public ChannelUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public ChannelUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public ChannelUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public ChannelUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public ChannelUpdater key(java.lang.String value) {
      updateOperations.set("key", value);
      return this;
    }

    public ChannelUpdater unsetKey(java.lang.String value) {
      updateOperations.unset("key");
      return this;
    }

    public ChannelUpdater addKey(java.lang.String value) {
      updateOperations.add("key", value);
      return this;
    }

    public ChannelUpdater addKey(java.lang.String value, boolean addDups) {
      updateOperations.add("key", value, addDups);
      return this;
    }

    public ChannelUpdater addAllToKey(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("key", values, addDups);
      return this;
    }
  
    public ChannelUpdater removeFirstKey() {
      updateOperations.removeFirst("key");
      return this;
    }
  
    public ChannelUpdater removeLastKey() {
      updateOperations.removeLast("key");
      return this;
    }
  
    public ChannelUpdater removeFromKey(java.lang.String value) {
      updateOperations.removeAll("key", value);
      return this;
    }

    public ChannelUpdater removeAllFromKey(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("key", values);
      return this;
    }
 
    public ChannelUpdater decKey() {
      updateOperations.dec("key");
      return this;
    }

    public ChannelUpdater incKey() {
      updateOperations.inc("key");
      return this;
    }

    public ChannelUpdater incKey(Number value) {
      updateOperations.inc("key", value);
      return this;
    }
    public ChannelUpdater logged(java.lang.Boolean value) {
      updateOperations.set("logged", value);
      return this;
    }

    public ChannelUpdater unsetLogged(java.lang.Boolean value) {
      updateOperations.unset("logged");
      return this;
    }

    public ChannelUpdater addLogged(java.lang.Boolean value) {
      updateOperations.add("logged", value);
      return this;
    }

    public ChannelUpdater addLogged(java.lang.Boolean value, boolean addDups) {
      updateOperations.add("logged", value, addDups);
      return this;
    }

    public ChannelUpdater addAllToLogged(java.util.List<java.lang.Boolean> values, boolean addDups) {
      updateOperations.addAll("logged", values, addDups);
      return this;
    }
  
    public ChannelUpdater removeFirstLogged() {
      updateOperations.removeFirst("logged");
      return this;
    }
  
    public ChannelUpdater removeLastLogged() {
      updateOperations.removeLast("logged");
      return this;
    }
  
    public ChannelUpdater removeFromLogged(java.lang.Boolean value) {
      updateOperations.removeAll("logged", value);
      return this;
    }

    public ChannelUpdater removeAllFromLogged(java.util.List<java.lang.Boolean> values) {
      updateOperations.removeAll("logged", values);
      return this;
    }
 
    public ChannelUpdater decLogged() {
      updateOperations.dec("logged");
      return this;
    }

    public ChannelUpdater incLogged() {
      updateOperations.inc("logged");
      return this;
    }

    public ChannelUpdater incLogged(Number value) {
      updateOperations.inc("logged", value);
      return this;
    }
    public ChannelUpdater name(java.lang.String value) {
      updateOperations.set("name", value);
      return this;
    }

    public ChannelUpdater unsetName(java.lang.String value) {
      updateOperations.unset("name");
      return this;
    }

    public ChannelUpdater addName(java.lang.String value) {
      updateOperations.add("name", value);
      return this;
    }

    public ChannelUpdater addName(java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public ChannelUpdater addAllToName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("name", values, addDups);
      return this;
    }
  
    public ChannelUpdater removeFirstName() {
      updateOperations.removeFirst("name");
      return this;
    }
  
    public ChannelUpdater removeLastName() {
      updateOperations.removeLast("name");
      return this;
    }
  
    public ChannelUpdater removeFromName(java.lang.String value) {
      updateOperations.removeAll("name", value);
      return this;
    }

    public ChannelUpdater removeAllFromName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("name", values);
      return this;
    }
 
    public ChannelUpdater decName() {
      updateOperations.dec("name");
      return this;
    }

    public ChannelUpdater incName() {
      updateOperations.inc("name");
      return this;
    }

    public ChannelUpdater incName(Number value) {
      updateOperations.inc("name", value);
      return this;
    }
    public ChannelUpdater updated(org.joda.time.DateTime value) {
      updateOperations.set("updated", value);
      return this;
    }

    public ChannelUpdater unsetUpdated(org.joda.time.DateTime value) {
      updateOperations.unset("updated");
      return this;
    }

    public ChannelUpdater addUpdated(org.joda.time.DateTime value) {
      updateOperations.add("updated", value);
      return this;
    }

    public ChannelUpdater addUpdated(org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("updated", value, addDups);
      return this;
    }

    public ChannelUpdater addAllToUpdated(java.util.List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("updated", values, addDups);
      return this;
    }
  
    public ChannelUpdater removeFirstUpdated() {
      updateOperations.removeFirst("updated");
      return this;
    }
  
    public ChannelUpdater removeLastUpdated() {
      updateOperations.removeLast("updated");
      return this;
    }
  
    public ChannelUpdater removeFromUpdated(org.joda.time.DateTime value) {
      updateOperations.removeAll("updated", value);
      return this;
    }

    public ChannelUpdater removeAllFromUpdated(java.util.List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("updated", values);
      return this;
    }
 
    public ChannelUpdater decUpdated() {
      updateOperations.dec("updated");
      return this;
    }

    public ChannelUpdater incUpdated() {
      updateOperations.inc("updated");
      return this;
    }

    public ChannelUpdater incUpdated(Number value) {
      updateOperations.inc("updated", value);
      return this;
    }
    public ChannelUpdater upperName(java.lang.String value) {
      updateOperations.set("upperName", value);
      return this;
    }

    public ChannelUpdater unsetUpperName(java.lang.String value) {
      updateOperations.unset("upperName");
      return this;
    }

    public ChannelUpdater addUpperName(java.lang.String value) {
      updateOperations.add("upperName", value);
      return this;
    }

    public ChannelUpdater addUpperName(java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public ChannelUpdater addAllToUpperName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperName", values, addDups);
      return this;
    }
  
    public ChannelUpdater removeFirstUpperName() {
      updateOperations.removeFirst("upperName");
      return this;
    }
  
    public ChannelUpdater removeLastUpperName() {
      updateOperations.removeLast("upperName");
      return this;
    }
  
    public ChannelUpdater removeFromUpperName(java.lang.String value) {
      updateOperations.removeAll("upperName", value);
      return this;
    }

    public ChannelUpdater removeAllFromUpperName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("upperName", values);
      return this;
    }
 
    public ChannelUpdater decUpperName() {
      updateOperations.dec("upperName");
      return this;
    }

    public ChannelUpdater incUpperName() {
      updateOperations.inc("upperName");
      return this;
    }

    public ChannelUpdater incUpperName(Number value) {
      updateOperations.inc("upperName", value);
      return this;
    }
  }
}
