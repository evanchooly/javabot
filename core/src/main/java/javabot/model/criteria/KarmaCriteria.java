package javabot.model.criteria;

import javabot.model.Karma;

public class KarmaCriteria extends com.antwerkz.critter.criteria.BaseCriteria<Karma> {
  private String prefix = "";

  public KarmaCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, Karma.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, java.lang.String> name() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, java.lang.String>(this, query, prefix + "name");
  }

  public org.mongodb.morphia.query.Criteria name(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, java.lang.String>(this, query, prefix + "name").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, org.joda.time.DateTime> updated() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, org.joda.time.DateTime>(this, query, prefix + "updated");
  }

  public org.mongodb.morphia.query.Criteria updated(org.joda.time.DateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, org.joda.time.DateTime>(this, query, prefix + "updated").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, java.lang.String> upperName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, java.lang.String>(this, query, prefix + "upperName");
  }

  public org.mongodb.morphia.query.Criteria upperName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, java.lang.String>(this, query, prefix + "upperName").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, java.lang.String> userName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, java.lang.String>(this, query, prefix + "userName");
  }

  public org.mongodb.morphia.query.Criteria userName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, java.lang.String>(this, query, prefix + "userName").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, java.lang.Integer> value() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, java.lang.Integer>(this, query, prefix + "value");
  }

  public org.mongodb.morphia.query.Criteria value(java.lang.Integer value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<KarmaCriteria, Karma, java.lang.Integer>(this, query, prefix + "value").equal(value);
  }


  public KarmaUpdater getUpdater() {
    return new KarmaUpdater();
  }

  public class KarmaUpdater {
    org.mongodb.morphia.query.UpdateOperations<Karma> updateOperations;

    public KarmaUpdater() {
      updateOperations = ds.createUpdateOperations(Karma.class);
    }

    public org.mongodb.morphia.query.UpdateResults<Karma> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<Karma> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<Karma> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<Karma> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public KarmaUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public KarmaUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public KarmaUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public KarmaUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public KarmaUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public KarmaUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public KarmaUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public KarmaUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public KarmaUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public KarmaUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public KarmaUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public KarmaUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public KarmaUpdater name(java.lang.String value) {
      updateOperations.set("name", value);
      return this;
    }

    public KarmaUpdater unsetName(java.lang.String value) {
      updateOperations.unset("name");
      return this;
    }

    public KarmaUpdater addName(java.lang.String value) {
      updateOperations.add("name", value);
      return this;
    }

    public KarmaUpdater addName(java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public KarmaUpdater addAllToName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("name", values, addDups);
      return this;
    }
  
    public KarmaUpdater removeFirstName() {
      updateOperations.removeFirst("name");
      return this;
    }
  
    public KarmaUpdater removeLastName() {
      updateOperations.removeLast("name");
      return this;
    }
  
    public KarmaUpdater removeFromName(java.lang.String value) {
      updateOperations.removeAll("name", value);
      return this;
    }

    public KarmaUpdater removeAllFromName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("name", values);
      return this;
    }
 
    public KarmaUpdater decName() {
      updateOperations.dec("name");
      return this;
    }

    public KarmaUpdater incName() {
      updateOperations.inc("name");
      return this;
    }

    public KarmaUpdater incName(Number value) {
      updateOperations.inc("name", value);
      return this;
    }
    public KarmaUpdater updated(org.joda.time.DateTime value) {
      updateOperations.set("updated", value);
      return this;
    }

    public KarmaUpdater unsetUpdated(org.joda.time.DateTime value) {
      updateOperations.unset("updated");
      return this;
    }

    public KarmaUpdater addUpdated(org.joda.time.DateTime value) {
      updateOperations.add("updated", value);
      return this;
    }

    public KarmaUpdater addUpdated(org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("updated", value, addDups);
      return this;
    }

    public KarmaUpdater addAllToUpdated(java.util.List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("updated", values, addDups);
      return this;
    }
  
    public KarmaUpdater removeFirstUpdated() {
      updateOperations.removeFirst("updated");
      return this;
    }
  
    public KarmaUpdater removeLastUpdated() {
      updateOperations.removeLast("updated");
      return this;
    }
  
    public KarmaUpdater removeFromUpdated(org.joda.time.DateTime value) {
      updateOperations.removeAll("updated", value);
      return this;
    }

    public KarmaUpdater removeAllFromUpdated(java.util.List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("updated", values);
      return this;
    }
 
    public KarmaUpdater decUpdated() {
      updateOperations.dec("updated");
      return this;
    }

    public KarmaUpdater incUpdated() {
      updateOperations.inc("updated");
      return this;
    }

    public KarmaUpdater incUpdated(Number value) {
      updateOperations.inc("updated", value);
      return this;
    }
    public KarmaUpdater upperName(java.lang.String value) {
      updateOperations.set("upperName", value);
      return this;
    }

    public KarmaUpdater unsetUpperName(java.lang.String value) {
      updateOperations.unset("upperName");
      return this;
    }

    public KarmaUpdater addUpperName(java.lang.String value) {
      updateOperations.add("upperName", value);
      return this;
    }

    public KarmaUpdater addUpperName(java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public KarmaUpdater addAllToUpperName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperName", values, addDups);
      return this;
    }
  
    public KarmaUpdater removeFirstUpperName() {
      updateOperations.removeFirst("upperName");
      return this;
    }
  
    public KarmaUpdater removeLastUpperName() {
      updateOperations.removeLast("upperName");
      return this;
    }
  
    public KarmaUpdater removeFromUpperName(java.lang.String value) {
      updateOperations.removeAll("upperName", value);
      return this;
    }

    public KarmaUpdater removeAllFromUpperName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("upperName", values);
      return this;
    }
 
    public KarmaUpdater decUpperName() {
      updateOperations.dec("upperName");
      return this;
    }

    public KarmaUpdater incUpperName() {
      updateOperations.inc("upperName");
      return this;
    }

    public KarmaUpdater incUpperName(Number value) {
      updateOperations.inc("upperName", value);
      return this;
    }
    public KarmaUpdater userName(java.lang.String value) {
      updateOperations.set("userName", value);
      return this;
    }

    public KarmaUpdater unsetUserName(java.lang.String value) {
      updateOperations.unset("userName");
      return this;
    }

    public KarmaUpdater addUserName(java.lang.String value) {
      updateOperations.add("userName", value);
      return this;
    }

    public KarmaUpdater addUserName(java.lang.String value, boolean addDups) {
      updateOperations.add("userName", value, addDups);
      return this;
    }

    public KarmaUpdater addAllToUserName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("userName", values, addDups);
      return this;
    }
  
    public KarmaUpdater removeFirstUserName() {
      updateOperations.removeFirst("userName");
      return this;
    }
  
    public KarmaUpdater removeLastUserName() {
      updateOperations.removeLast("userName");
      return this;
    }
  
    public KarmaUpdater removeFromUserName(java.lang.String value) {
      updateOperations.removeAll("userName", value);
      return this;
    }

    public KarmaUpdater removeAllFromUserName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("userName", values);
      return this;
    }
 
    public KarmaUpdater decUserName() {
      updateOperations.dec("userName");
      return this;
    }

    public KarmaUpdater incUserName() {
      updateOperations.inc("userName");
      return this;
    }

    public KarmaUpdater incUserName(Number value) {
      updateOperations.inc("userName", value);
      return this;
    }
    public KarmaUpdater value(java.lang.Integer value) {
      updateOperations.set("value", value);
      return this;
    }

    public KarmaUpdater unsetValue(java.lang.Integer value) {
      updateOperations.unset("value");
      return this;
    }

    public KarmaUpdater addValue(java.lang.Integer value) {
      updateOperations.add("value", value);
      return this;
    }

    public KarmaUpdater addValue(java.lang.Integer value, boolean addDups) {
      updateOperations.add("value", value, addDups);
      return this;
    }

    public KarmaUpdater addAllToValue(java.util.List<java.lang.Integer> values, boolean addDups) {
      updateOperations.addAll("value", values, addDups);
      return this;
    }
  
    public KarmaUpdater removeFirstValue() {
      updateOperations.removeFirst("value");
      return this;
    }
  
    public KarmaUpdater removeLastValue() {
      updateOperations.removeLast("value");
      return this;
    }
  
    public KarmaUpdater removeFromValue(java.lang.Integer value) {
      updateOperations.removeAll("value", value);
      return this;
    }

    public KarmaUpdater removeAllFromValue(java.util.List<java.lang.Integer> values) {
      updateOperations.removeAll("value", values);
      return this;
    }
 
    public KarmaUpdater decValue() {
      updateOperations.dec("value");
      return this;
    }

    public KarmaUpdater incValue() {
      updateOperations.inc("value");
      return this;
    }

    public KarmaUpdater incValue(Number value) {
      updateOperations.inc("value", value);
      return this;
    }
  }
}
