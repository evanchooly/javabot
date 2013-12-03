package javabot.model.criteria;

import javabot.model.AdminEvent;

public class AdminEventCriteria extends com.antwerkz.critter.criteria.BaseCriteria<AdminEvent> {
  private String prefix = "";

  public AdminEventCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, AdminEvent.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.joda.time.DateTime> completed() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.joda.time.DateTime>(this, query, prefix + "completed");
  }

  public org.mongodb.morphia.query.Criteria completed(org.joda.time.DateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.joda.time.DateTime>(this, query, prefix + "completed").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, java.lang.String> requestedBy() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, java.lang.String>(this, query, prefix + "requestedBy");
  }

  public org.mongodb.morphia.query.Criteria requestedBy(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, java.lang.String>(this, query, prefix + "requestedBy").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.joda.time.DateTime> requestedOn() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.joda.time.DateTime>(this, query, prefix + "requestedOn");
  }

  public org.mongodb.morphia.query.Criteria requestedOn(org.joda.time.DateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.joda.time.DateTime>(this, query, prefix + "requestedOn").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.AdminEvent.State> state() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.AdminEvent.State>(this, query, prefix + "state");
  }

  public org.mongodb.morphia.query.Criteria state(javabot.model.AdminEvent.State value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.AdminEvent.State>(this, query, prefix + "state").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.EventType> type() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.EventType>(this, query, prefix + "type");
  }

  public org.mongodb.morphia.query.Criteria type(javabot.model.EventType value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.EventType>(this, query, prefix + "type").equal(value);
  }


  public AdminEventUpdater getUpdater() {
    return new AdminEventUpdater();
  }

  public class AdminEventUpdater {
    org.mongodb.morphia.query.UpdateOperations<AdminEvent> updateOperations;

    public AdminEventUpdater() {
      updateOperations = ds.createUpdateOperations(AdminEvent.class);
    }

    public org.mongodb.morphia.query.UpdateResults<AdminEvent> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<AdminEvent> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<AdminEvent> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<AdminEvent> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public AdminEventUpdater completed(org.joda.time.DateTime value) {
      updateOperations.set("completed", value);
      return this;
    }

    public AdminEventUpdater unsetCompleted(org.joda.time.DateTime value) {
      updateOperations.unset("completed");
      return this;
    }

    public AdminEventUpdater addCompleted(org.joda.time.DateTime value) {
      updateOperations.add("completed", value);
      return this;
    }

    public AdminEventUpdater addCompleted(org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("completed", value, addDups);
      return this;
    }

    public AdminEventUpdater addAllToCompleted(java.util.List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("completed", values, addDups);
      return this;
    }
  
    public AdminEventUpdater removeFirstCompleted() {
      updateOperations.removeFirst("completed");
      return this;
    }
  
    public AdminEventUpdater removeLastCompleted() {
      updateOperations.removeLast("completed");
      return this;
    }
  
    public AdminEventUpdater removeFromCompleted(org.joda.time.DateTime value) {
      updateOperations.removeAll("completed", value);
      return this;
    }

    public AdminEventUpdater removeAllFromCompleted(java.util.List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("completed", values);
      return this;
    }
 
    public AdminEventUpdater decCompleted() {
      updateOperations.dec("completed");
      return this;
    }

    public AdminEventUpdater incCompleted() {
      updateOperations.inc("completed");
      return this;
    }

    public AdminEventUpdater incCompleted(Number value) {
      updateOperations.inc("completed", value);
      return this;
    }
    public AdminEventUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public AdminEventUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public AdminEventUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public AdminEventUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public AdminEventUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public AdminEventUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public AdminEventUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public AdminEventUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public AdminEventUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public AdminEventUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public AdminEventUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public AdminEventUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public AdminEventUpdater requestedBy(java.lang.String value) {
      updateOperations.set("requestedBy", value);
      return this;
    }

    public AdminEventUpdater unsetRequestedBy(java.lang.String value) {
      updateOperations.unset("requestedBy");
      return this;
    }

    public AdminEventUpdater addRequestedBy(java.lang.String value) {
      updateOperations.add("requestedBy", value);
      return this;
    }

    public AdminEventUpdater addRequestedBy(java.lang.String value, boolean addDups) {
      updateOperations.add("requestedBy", value, addDups);
      return this;
    }

    public AdminEventUpdater addAllToRequestedBy(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("requestedBy", values, addDups);
      return this;
    }
  
    public AdminEventUpdater removeFirstRequestedBy() {
      updateOperations.removeFirst("requestedBy");
      return this;
    }
  
    public AdminEventUpdater removeLastRequestedBy() {
      updateOperations.removeLast("requestedBy");
      return this;
    }
  
    public AdminEventUpdater removeFromRequestedBy(java.lang.String value) {
      updateOperations.removeAll("requestedBy", value);
      return this;
    }

    public AdminEventUpdater removeAllFromRequestedBy(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("requestedBy", values);
      return this;
    }
 
    public AdminEventUpdater decRequestedBy() {
      updateOperations.dec("requestedBy");
      return this;
    }

    public AdminEventUpdater incRequestedBy() {
      updateOperations.inc("requestedBy");
      return this;
    }

    public AdminEventUpdater incRequestedBy(Number value) {
      updateOperations.inc("requestedBy", value);
      return this;
    }
    public AdminEventUpdater requestedOn(org.joda.time.DateTime value) {
      updateOperations.set("requestedOn", value);
      return this;
    }

    public AdminEventUpdater unsetRequestedOn(org.joda.time.DateTime value) {
      updateOperations.unset("requestedOn");
      return this;
    }

    public AdminEventUpdater addRequestedOn(org.joda.time.DateTime value) {
      updateOperations.add("requestedOn", value);
      return this;
    }

    public AdminEventUpdater addRequestedOn(org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("requestedOn", value, addDups);
      return this;
    }

    public AdminEventUpdater addAllToRequestedOn(java.util.List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("requestedOn", values, addDups);
      return this;
    }
  
    public AdminEventUpdater removeFirstRequestedOn() {
      updateOperations.removeFirst("requestedOn");
      return this;
    }
  
    public AdminEventUpdater removeLastRequestedOn() {
      updateOperations.removeLast("requestedOn");
      return this;
    }
  
    public AdminEventUpdater removeFromRequestedOn(org.joda.time.DateTime value) {
      updateOperations.removeAll("requestedOn", value);
      return this;
    }

    public AdminEventUpdater removeAllFromRequestedOn(java.util.List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("requestedOn", values);
      return this;
    }
 
    public AdminEventUpdater decRequestedOn() {
      updateOperations.dec("requestedOn");
      return this;
    }

    public AdminEventUpdater incRequestedOn() {
      updateOperations.inc("requestedOn");
      return this;
    }

    public AdminEventUpdater incRequestedOn(Number value) {
      updateOperations.inc("requestedOn", value);
      return this;
    }
    public AdminEventUpdater state(javabot.model.AdminEvent.State value) {
      updateOperations.set("state", value);
      return this;
    }

    public AdminEventUpdater unsetState(javabot.model.AdminEvent.State value) {
      updateOperations.unset("state");
      return this;
    }

    public AdminEventUpdater addState(javabot.model.AdminEvent.State value) {
      updateOperations.add("state", value);
      return this;
    }

    public AdminEventUpdater addState(javabot.model.AdminEvent.State value, boolean addDups) {
      updateOperations.add("state", value, addDups);
      return this;
    }

    public AdminEventUpdater addAllToState(java.util.List<javabot.model.AdminEvent.State> values, boolean addDups) {
      updateOperations.addAll("state", values, addDups);
      return this;
    }
  
    public AdminEventUpdater removeFirstState() {
      updateOperations.removeFirst("state");
      return this;
    }
  
    public AdminEventUpdater removeLastState() {
      updateOperations.removeLast("state");
      return this;
    }
  
    public AdminEventUpdater removeFromState(javabot.model.AdminEvent.State value) {
      updateOperations.removeAll("state", value);
      return this;
    }

    public AdminEventUpdater removeAllFromState(java.util.List<javabot.model.AdminEvent.State> values) {
      updateOperations.removeAll("state", values);
      return this;
    }
 
    public AdminEventUpdater decState() {
      updateOperations.dec("state");
      return this;
    }

    public AdminEventUpdater incState() {
      updateOperations.inc("state");
      return this;
    }

    public AdminEventUpdater incState(Number value) {
      updateOperations.inc("state", value);
      return this;
    }
    public AdminEventUpdater type(javabot.model.EventType value) {
      updateOperations.set("type", value);
      return this;
    }

    public AdminEventUpdater unsetType(javabot.model.EventType value) {
      updateOperations.unset("type");
      return this;
    }

    public AdminEventUpdater addType(javabot.model.EventType value) {
      updateOperations.add("type", value);
      return this;
    }

    public AdminEventUpdater addType(javabot.model.EventType value, boolean addDups) {
      updateOperations.add("type", value, addDups);
      return this;
    }

    public AdminEventUpdater addAllToType(java.util.List<javabot.model.EventType> values, boolean addDups) {
      updateOperations.addAll("type", values, addDups);
      return this;
    }
  
    public AdminEventUpdater removeFirstType() {
      updateOperations.removeFirst("type");
      return this;
    }
  
    public AdminEventUpdater removeLastType() {
      updateOperations.removeLast("type");
      return this;
    }
  
    public AdminEventUpdater removeFromType(javabot.model.EventType value) {
      updateOperations.removeAll("type", value);
      return this;
    }

    public AdminEventUpdater removeAllFromType(java.util.List<javabot.model.EventType> values) {
      updateOperations.removeAll("type", values);
      return this;
    }
 
    public AdminEventUpdater decType() {
      updateOperations.dec("type");
      return this;
    }

    public AdminEventUpdater incType() {
      updateOperations.inc("type");
      return this;
    }

    public AdminEventUpdater incType(Number value) {
      updateOperations.inc("type", value);
      return this;
    }
  }
}
