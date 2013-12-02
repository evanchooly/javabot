package javabot.model.criteria;

import javabot.model.Change;

public class ChangeCriteria extends com.antwerkz.critter.criteria.BaseCriteria<Change> {
  private String prefix = "";

  public ChangeCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, Change.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<ChangeCriteria, Change, org.joda.time.DateTime> changeDate() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChangeCriteria, Change, org.joda.time.DateTime>(this, query, prefix + "changeDate");
  }

  public org.mongodb.morphia.query.Criteria changeDate(org.joda.time.DateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChangeCriteria, Change, org.joda.time.DateTime>(this, query, prefix + "changeDate").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChangeCriteria, Change, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChangeCriteria, Change, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChangeCriteria, Change, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChangeCriteria, Change, java.lang.String> message() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChangeCriteria, Change, java.lang.String>(this, query, prefix + "message");
  }

  public org.mongodb.morphia.query.Criteria message(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChangeCriteria, Change, java.lang.String>(this, query, prefix + "message").equal(value);
  }


  public ChangeUpdater getUpdater() {
    return new ChangeUpdater();
  }

  public class ChangeUpdater {
    org.mongodb.morphia.query.UpdateOperations<Change> updateOperations;

    public ChangeUpdater() {
      updateOperations = ds.createUpdateOperations(Change.class);
    }

    public org.mongodb.morphia.query.UpdateResults<Change> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<Change> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<Change> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<Change> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public ChangeUpdater changeDate(org.joda.time.DateTime value) {
      updateOperations.set("changeDate", value);
      return this;
    }

    public ChangeUpdater unsetChangeDate(org.joda.time.DateTime value) {
      updateOperations.unset("changeDate");
      return this;
    }

    public ChangeUpdater addChangeDate(org.joda.time.DateTime value) {
      updateOperations.add("changeDate", value);
      return this;
    }

    public ChangeUpdater addChangeDate(org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("changeDate", value, addDups);
      return this;
    }

    public ChangeUpdater addAllToChangeDate(java.util.List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("changeDate", values, addDups);
      return this;
    }
  
    public ChangeUpdater removeFirstChangeDate() {
      updateOperations.removeFirst("changeDate");
      return this;
    }
  
    public ChangeUpdater removeLastChangeDate() {
      updateOperations.removeLast("changeDate");
      return this;
    }
  
    public ChangeUpdater removeFromChangeDate(org.joda.time.DateTime value) {
      updateOperations.removeAll("changeDate", value);
      return this;
    }

    public ChangeUpdater removeAllFromChangeDate(java.util.List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("changeDate", values);
      return this;
    }
 
    public ChangeUpdater decChangeDate() {
      updateOperations.dec("changeDate");
      return this;
    }

    public ChangeUpdater incChangeDate() {
      updateOperations.inc("changeDate");
      return this;
    }

    public ChangeUpdater incChangeDate(Number value) {
      updateOperations.inc("changeDate", value);
      return this;
    }
    public ChangeUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public ChangeUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public ChangeUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public ChangeUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ChangeUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public ChangeUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public ChangeUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public ChangeUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public ChangeUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public ChangeUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public ChangeUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public ChangeUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public ChangeUpdater message(java.lang.String value) {
      updateOperations.set("message", value);
      return this;
    }

    public ChangeUpdater unsetMessage(java.lang.String value) {
      updateOperations.unset("message");
      return this;
    }

    public ChangeUpdater addMessage(java.lang.String value) {
      updateOperations.add("message", value);
      return this;
    }

    public ChangeUpdater addMessage(java.lang.String value, boolean addDups) {
      updateOperations.add("message", value, addDups);
      return this;
    }

    public ChangeUpdater addAllToMessage(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("message", values, addDups);
      return this;
    }
  
    public ChangeUpdater removeFirstMessage() {
      updateOperations.removeFirst("message");
      return this;
    }
  
    public ChangeUpdater removeLastMessage() {
      updateOperations.removeLast("message");
      return this;
    }
  
    public ChangeUpdater removeFromMessage(java.lang.String value) {
      updateOperations.removeAll("message", value);
      return this;
    }

    public ChangeUpdater removeAllFromMessage(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("message", values);
      return this;
    }
 
    public ChangeUpdater decMessage() {
      updateOperations.dec("message");
      return this;
    }

    public ChangeUpdater incMessage() {
      updateOperations.inc("message");
      return this;
    }

    public ChangeUpdater incMessage(Number value) {
      updateOperations.inc("message", value);
      return this;
    }
  }
}
