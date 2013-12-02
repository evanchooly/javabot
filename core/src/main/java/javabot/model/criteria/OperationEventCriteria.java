package javabot.model.criteria;

import javabot.model.OperationEvent;

public class OperationEventCriteria extends com.antwerkz.critter.criteria.BaseCriteria<OperationEvent> {
  private String prefix = "";

  public OperationEventCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, OperationEvent.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.joda.time.DateTime> completed() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.joda.time.DateTime>(this, query, prefix + "completed");
  }

  public org.mongodb.morphia.query.Criteria completed(org.joda.time.DateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.joda.time.DateTime>(this, query, prefix + "completed").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.lang.String> operation() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.lang.String>(this, query, prefix + "operation");
  }

  public org.mongodb.morphia.query.Criteria operation(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.lang.String>(this, query, prefix + "operation").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.lang.String> requestedBy() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.lang.String>(this, query, prefix + "requestedBy");
  }

  public org.mongodb.morphia.query.Criteria requestedBy(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.lang.String>(this, query, prefix + "requestedBy").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.joda.time.DateTime> requestedOn() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.joda.time.DateTime>(this, query, prefix + "requestedOn");
  }

  public org.mongodb.morphia.query.Criteria requestedOn(org.joda.time.DateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.joda.time.DateTime>(this, query, prefix + "requestedOn").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.AdminEvent.State> state() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.AdminEvent.State>(this, query, prefix + "state");
  }

  public org.mongodb.morphia.query.Criteria state(javabot.model.AdminEvent.State value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.AdminEvent.State>(this, query, prefix + "state").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.EventType> type() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.EventType>(this, query, prefix + "type");
  }

  public org.mongodb.morphia.query.Criteria type(javabot.model.EventType value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.EventType>(this, query, prefix + "type").equal(value);
  }


  public OperationEventUpdater getUpdater() {
    return new OperationEventUpdater();
  }

  public class OperationEventUpdater {
    org.mongodb.morphia.query.UpdateOperations<OperationEvent> updateOperations;

    public OperationEventUpdater() {
      updateOperations = ds.createUpdateOperations(OperationEvent.class);
    }

    public org.mongodb.morphia.query.UpdateResults<OperationEvent> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<OperationEvent> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<OperationEvent> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<OperationEvent> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public OperationEventUpdater completed(org.joda.time.DateTime value) {
      updateOperations.set("completed", value);
      return this;
    }

    public OperationEventUpdater unsetCompleted(org.joda.time.DateTime value) {
      updateOperations.unset("completed");
      return this;
    }

    public OperationEventUpdater addCompleted(org.joda.time.DateTime value) {
      updateOperations.add("completed", value);
      return this;
    }

    public OperationEventUpdater addCompleted(org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("completed", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToCompleted(java.util.List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("completed", values, addDups);
      return this;
    }
  
    public OperationEventUpdater removeFirstCompleted() {
      updateOperations.removeFirst("completed");
      return this;
    }
  
    public OperationEventUpdater removeLastCompleted() {
      updateOperations.removeLast("completed");
      return this;
    }
  
    public OperationEventUpdater removeFromCompleted(org.joda.time.DateTime value) {
      updateOperations.removeAll("completed", value);
      return this;
    }

    public OperationEventUpdater removeAllFromCompleted(java.util.List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("completed", values);
      return this;
    }
 
    public OperationEventUpdater decCompleted() {
      updateOperations.dec("completed");
      return this;
    }

    public OperationEventUpdater incCompleted() {
      updateOperations.inc("completed");
      return this;
    }

    public OperationEventUpdater incCompleted(Number value) {
      updateOperations.inc("completed", value);
      return this;
    }
    public OperationEventUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public OperationEventUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public OperationEventUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public OperationEventUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public OperationEventUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public OperationEventUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public OperationEventUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public OperationEventUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public OperationEventUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public OperationEventUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public OperationEventUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public OperationEventUpdater operation(java.lang.String value) {
      updateOperations.set("operation", value);
      return this;
    }

    public OperationEventUpdater unsetOperation(java.lang.String value) {
      updateOperations.unset("operation");
      return this;
    }

    public OperationEventUpdater addOperation(java.lang.String value) {
      updateOperations.add("operation", value);
      return this;
    }

    public OperationEventUpdater addOperation(java.lang.String value, boolean addDups) {
      updateOperations.add("operation", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToOperation(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("operation", values, addDups);
      return this;
    }
  
    public OperationEventUpdater removeFirstOperation() {
      updateOperations.removeFirst("operation");
      return this;
    }
  
    public OperationEventUpdater removeLastOperation() {
      updateOperations.removeLast("operation");
      return this;
    }
  
    public OperationEventUpdater removeFromOperation(java.lang.String value) {
      updateOperations.removeAll("operation", value);
      return this;
    }

    public OperationEventUpdater removeAllFromOperation(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("operation", values);
      return this;
    }
 
    public OperationEventUpdater decOperation() {
      updateOperations.dec("operation");
      return this;
    }

    public OperationEventUpdater incOperation() {
      updateOperations.inc("operation");
      return this;
    }

    public OperationEventUpdater incOperation(Number value) {
      updateOperations.inc("operation", value);
      return this;
    }
    public OperationEventUpdater requestedBy(java.lang.String value) {
      updateOperations.set("requestedBy", value);
      return this;
    }

    public OperationEventUpdater unsetRequestedBy(java.lang.String value) {
      updateOperations.unset("requestedBy");
      return this;
    }

    public OperationEventUpdater addRequestedBy(java.lang.String value) {
      updateOperations.add("requestedBy", value);
      return this;
    }

    public OperationEventUpdater addRequestedBy(java.lang.String value, boolean addDups) {
      updateOperations.add("requestedBy", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToRequestedBy(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("requestedBy", values, addDups);
      return this;
    }
  
    public OperationEventUpdater removeFirstRequestedBy() {
      updateOperations.removeFirst("requestedBy");
      return this;
    }
  
    public OperationEventUpdater removeLastRequestedBy() {
      updateOperations.removeLast("requestedBy");
      return this;
    }
  
    public OperationEventUpdater removeFromRequestedBy(java.lang.String value) {
      updateOperations.removeAll("requestedBy", value);
      return this;
    }

    public OperationEventUpdater removeAllFromRequestedBy(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("requestedBy", values);
      return this;
    }
 
    public OperationEventUpdater decRequestedBy() {
      updateOperations.dec("requestedBy");
      return this;
    }

    public OperationEventUpdater incRequestedBy() {
      updateOperations.inc("requestedBy");
      return this;
    }

    public OperationEventUpdater incRequestedBy(Number value) {
      updateOperations.inc("requestedBy", value);
      return this;
    }
    public OperationEventUpdater requestedOn(org.joda.time.DateTime value) {
      updateOperations.set("requestedOn", value);
      return this;
    }

    public OperationEventUpdater unsetRequestedOn(org.joda.time.DateTime value) {
      updateOperations.unset("requestedOn");
      return this;
    }

    public OperationEventUpdater addRequestedOn(org.joda.time.DateTime value) {
      updateOperations.add("requestedOn", value);
      return this;
    }

    public OperationEventUpdater addRequestedOn(org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("requestedOn", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToRequestedOn(java.util.List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("requestedOn", values, addDups);
      return this;
    }
  
    public OperationEventUpdater removeFirstRequestedOn() {
      updateOperations.removeFirst("requestedOn");
      return this;
    }
  
    public OperationEventUpdater removeLastRequestedOn() {
      updateOperations.removeLast("requestedOn");
      return this;
    }
  
    public OperationEventUpdater removeFromRequestedOn(org.joda.time.DateTime value) {
      updateOperations.removeAll("requestedOn", value);
      return this;
    }

    public OperationEventUpdater removeAllFromRequestedOn(java.util.List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("requestedOn", values);
      return this;
    }
 
    public OperationEventUpdater decRequestedOn() {
      updateOperations.dec("requestedOn");
      return this;
    }

    public OperationEventUpdater incRequestedOn() {
      updateOperations.inc("requestedOn");
      return this;
    }

    public OperationEventUpdater incRequestedOn(Number value) {
      updateOperations.inc("requestedOn", value);
      return this;
    }
    public OperationEventUpdater state(javabot.model.AdminEvent.State value) {
      updateOperations.set("state", value);
      return this;
    }

    public OperationEventUpdater unsetState(javabot.model.AdminEvent.State value) {
      updateOperations.unset("state");
      return this;
    }

    public OperationEventUpdater addState(javabot.model.AdminEvent.State value) {
      updateOperations.add("state", value);
      return this;
    }

    public OperationEventUpdater addState(javabot.model.AdminEvent.State value, boolean addDups) {
      updateOperations.add("state", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToState(java.util.List<javabot.model.AdminEvent.State> values, boolean addDups) {
      updateOperations.addAll("state", values, addDups);
      return this;
    }
  
    public OperationEventUpdater removeFirstState() {
      updateOperations.removeFirst("state");
      return this;
    }
  
    public OperationEventUpdater removeLastState() {
      updateOperations.removeLast("state");
      return this;
    }
  
    public OperationEventUpdater removeFromState(javabot.model.AdminEvent.State value) {
      updateOperations.removeAll("state", value);
      return this;
    }

    public OperationEventUpdater removeAllFromState(java.util.List<javabot.model.AdminEvent.State> values) {
      updateOperations.removeAll("state", values);
      return this;
    }
 
    public OperationEventUpdater decState() {
      updateOperations.dec("state");
      return this;
    }

    public OperationEventUpdater incState() {
      updateOperations.inc("state");
      return this;
    }

    public OperationEventUpdater incState(Number value) {
      updateOperations.inc("state", value);
      return this;
    }
    public OperationEventUpdater type(javabot.model.EventType value) {
      updateOperations.set("type", value);
      return this;
    }

    public OperationEventUpdater unsetType(javabot.model.EventType value) {
      updateOperations.unset("type");
      return this;
    }

    public OperationEventUpdater addType(javabot.model.EventType value) {
      updateOperations.add("type", value);
      return this;
    }

    public OperationEventUpdater addType(javabot.model.EventType value, boolean addDups) {
      updateOperations.add("type", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToType(java.util.List<javabot.model.EventType> values, boolean addDups) {
      updateOperations.addAll("type", values, addDups);
      return this;
    }
  
    public OperationEventUpdater removeFirstType() {
      updateOperations.removeFirst("type");
      return this;
    }
  
    public OperationEventUpdater removeLastType() {
      updateOperations.removeLast("type");
      return this;
    }
  
    public OperationEventUpdater removeFromType(javabot.model.EventType value) {
      updateOperations.removeAll("type", value);
      return this;
    }

    public OperationEventUpdater removeAllFromType(java.util.List<javabot.model.EventType> values) {
      updateOperations.removeAll("type", values);
      return this;
    }
 
    public OperationEventUpdater decType() {
      updateOperations.dec("type");
      return this;
    }

    public OperationEventUpdater incType() {
      updateOperations.inc("type");
      return this;
    }

    public OperationEventUpdater incType(Number value) {
      updateOperations.inc("type", value);
      return this;
    }
  }
}
