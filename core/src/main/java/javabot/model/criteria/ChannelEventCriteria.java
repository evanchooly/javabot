package javabot.model.criteria;

import javabot.model.ChannelEvent;

public class ChannelEventCriteria extends com.antwerkz.critter.criteria.BaseCriteria<ChannelEvent> {
  private String prefix = "";

  public ChannelEventCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, ChannelEvent.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.lang.String> channel() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.lang.String>(this, query, prefix + "channel");
  }

  public org.mongodb.morphia.query.Criteria channel(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.lang.String>(this, query, prefix + "channel").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime> completed() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime>(this, query, prefix + "completed");
  }

  public org.mongodb.morphia.query.Criteria completed(java.time.LocalDateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime>(this, query, prefix + "completed").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.lang.String> key() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.lang.String>(this, query, prefix + "key");
  }

  public org.mongodb.morphia.query.Criteria key(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.lang.String>(this, query, prefix + "key").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.lang.Boolean> logged() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.lang.Boolean>(this, query, prefix + "logged");
  }

  public org.mongodb.morphia.query.Criteria logged(java.lang.Boolean value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.lang.Boolean>(this, query, prefix + "logged").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.lang.String> requestedBy() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.lang.String>(this, query, prefix + "requestedBy");
  }

  public org.mongodb.morphia.query.Criteria requestedBy(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.lang.String>(this, query, prefix + "requestedBy").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime> requestedOn() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime>(this, query, prefix + "requestedOn");
  }

  public org.mongodb.morphia.query.Criteria requestedOn(java.time.LocalDateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime>(this, query, prefix + "requestedOn").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.AdminEvent.State> state() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.AdminEvent.State>(this, query, prefix + "state");
  }

  public org.mongodb.morphia.query.Criteria state(javabot.model.AdminEvent.State value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.AdminEvent.State>(this, query, prefix + "state").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.EventType> type() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.EventType>(this, query, prefix + "type");
  }

  public org.mongodb.morphia.query.Criteria type(javabot.model.EventType value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.EventType>(this, query, prefix + "type").equal(value);
  }


  public ChannelEventUpdater getUpdater() {
    return new ChannelEventUpdater();
  }

  public class ChannelEventUpdater {
    org.mongodb.morphia.query.UpdateOperations<ChannelEvent> updateOperations;

    public ChannelEventUpdater() {
      updateOperations = ds.createUpdateOperations(ChannelEvent.class);
    }

    public org.mongodb.morphia.query.UpdateResults<ChannelEvent> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<ChannelEvent> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<ChannelEvent> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<ChannelEvent> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public ChannelEventUpdater channel(java.lang.String value) {
      updateOperations.set("channel", value);
      return this;
    }

    public ChannelEventUpdater unsetChannel(java.lang.String value) {
      updateOperations.unset("channel");
      return this;
    }

    public ChannelEventUpdater addChannel(java.lang.String value) {
      updateOperations.add("channel", value);
      return this;
    }

    public ChannelEventUpdater addChannel(java.lang.String value, boolean addDups) {
      updateOperations.add("channel", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToChannel(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("channel", values, addDups);
      return this;
    }
  
    public ChannelEventUpdater removeFirstChannel() {
      updateOperations.removeFirst("channel");
      return this;
    }
  
    public ChannelEventUpdater removeLastChannel() {
      updateOperations.removeLast("channel");
      return this;
    }
  
    public ChannelEventUpdater removeFromChannel(java.lang.String value) {
      updateOperations.removeAll("channel", value);
      return this;
    }

    public ChannelEventUpdater removeAllFromChannel(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("channel", values);
      return this;
    }
 
    public ChannelEventUpdater decChannel() {
      updateOperations.dec("channel");
      return this;
    }

    public ChannelEventUpdater incChannel() {
      updateOperations.inc("channel");
      return this;
    }

    public ChannelEventUpdater incChannel(Number value) {
      updateOperations.inc("channel", value);
      return this;
    }
    public ChannelEventUpdater completed(java.time.LocalDateTime value) {
      updateOperations.set("completed", value);
      return this;
    }

    public ChannelEventUpdater unsetCompleted(java.time.LocalDateTime value) {
      updateOperations.unset("completed");
      return this;
    }

    public ChannelEventUpdater addCompleted(java.time.LocalDateTime value) {
      updateOperations.add("completed", value);
      return this;
    }

    public ChannelEventUpdater addCompleted(java.time.LocalDateTime value, boolean addDups) {
      updateOperations.add("completed", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToCompleted(java.util.List<java.time.LocalDateTime> values, boolean addDups) {
      updateOperations.addAll("completed", values, addDups);
      return this;
    }
  
    public ChannelEventUpdater removeFirstCompleted() {
      updateOperations.removeFirst("completed");
      return this;
    }
  
    public ChannelEventUpdater removeLastCompleted() {
      updateOperations.removeLast("completed");
      return this;
    }
  
    public ChannelEventUpdater removeFromCompleted(java.time.LocalDateTime value) {
      updateOperations.removeAll("completed", value);
      return this;
    }

    public ChannelEventUpdater removeAllFromCompleted(java.util.List<java.time.LocalDateTime> values) {
      updateOperations.removeAll("completed", values);
      return this;
    }
 
    public ChannelEventUpdater decCompleted() {
      updateOperations.dec("completed");
      return this;
    }

    public ChannelEventUpdater incCompleted() {
      updateOperations.inc("completed");
      return this;
    }

    public ChannelEventUpdater incCompleted(Number value) {
      updateOperations.inc("completed", value);
      return this;
    }
    public ChannelEventUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public ChannelEventUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public ChannelEventUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public ChannelEventUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public ChannelEventUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public ChannelEventUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public ChannelEventUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public ChannelEventUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public ChannelEventUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public ChannelEventUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public ChannelEventUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public ChannelEventUpdater key(java.lang.String value) {
      updateOperations.set("key", value);
      return this;
    }

    public ChannelEventUpdater unsetKey(java.lang.String value) {
      updateOperations.unset("key");
      return this;
    }

    public ChannelEventUpdater addKey(java.lang.String value) {
      updateOperations.add("key", value);
      return this;
    }

    public ChannelEventUpdater addKey(java.lang.String value, boolean addDups) {
      updateOperations.add("key", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToKey(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("key", values, addDups);
      return this;
    }
  
    public ChannelEventUpdater removeFirstKey() {
      updateOperations.removeFirst("key");
      return this;
    }
  
    public ChannelEventUpdater removeLastKey() {
      updateOperations.removeLast("key");
      return this;
    }
  
    public ChannelEventUpdater removeFromKey(java.lang.String value) {
      updateOperations.removeAll("key", value);
      return this;
    }

    public ChannelEventUpdater removeAllFromKey(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("key", values);
      return this;
    }
 
    public ChannelEventUpdater decKey() {
      updateOperations.dec("key");
      return this;
    }

    public ChannelEventUpdater incKey() {
      updateOperations.inc("key");
      return this;
    }

    public ChannelEventUpdater incKey(Number value) {
      updateOperations.inc("key", value);
      return this;
    }
    public ChannelEventUpdater logged(java.lang.Boolean value) {
      updateOperations.set("logged", value);
      return this;
    }

    public ChannelEventUpdater unsetLogged(java.lang.Boolean value) {
      updateOperations.unset("logged");
      return this;
    }

    public ChannelEventUpdater addLogged(java.lang.Boolean value) {
      updateOperations.add("logged", value);
      return this;
    }

    public ChannelEventUpdater addLogged(java.lang.Boolean value, boolean addDups) {
      updateOperations.add("logged", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToLogged(java.util.List<java.lang.Boolean> values, boolean addDups) {
      updateOperations.addAll("logged", values, addDups);
      return this;
    }
  
    public ChannelEventUpdater removeFirstLogged() {
      updateOperations.removeFirst("logged");
      return this;
    }
  
    public ChannelEventUpdater removeLastLogged() {
      updateOperations.removeLast("logged");
      return this;
    }
  
    public ChannelEventUpdater removeFromLogged(java.lang.Boolean value) {
      updateOperations.removeAll("logged", value);
      return this;
    }

    public ChannelEventUpdater removeAllFromLogged(java.util.List<java.lang.Boolean> values) {
      updateOperations.removeAll("logged", values);
      return this;
    }
 
    public ChannelEventUpdater decLogged() {
      updateOperations.dec("logged");
      return this;
    }

    public ChannelEventUpdater incLogged() {
      updateOperations.inc("logged");
      return this;
    }

    public ChannelEventUpdater incLogged(Number value) {
      updateOperations.inc("logged", value);
      return this;
    }
    public ChannelEventUpdater requestedBy(java.lang.String value) {
      updateOperations.set("requestedBy", value);
      return this;
    }

    public ChannelEventUpdater unsetRequestedBy(java.lang.String value) {
      updateOperations.unset("requestedBy");
      return this;
    }

    public ChannelEventUpdater addRequestedBy(java.lang.String value) {
      updateOperations.add("requestedBy", value);
      return this;
    }

    public ChannelEventUpdater addRequestedBy(java.lang.String value, boolean addDups) {
      updateOperations.add("requestedBy", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToRequestedBy(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("requestedBy", values, addDups);
      return this;
    }
  
    public ChannelEventUpdater removeFirstRequestedBy() {
      updateOperations.removeFirst("requestedBy");
      return this;
    }
  
    public ChannelEventUpdater removeLastRequestedBy() {
      updateOperations.removeLast("requestedBy");
      return this;
    }
  
    public ChannelEventUpdater removeFromRequestedBy(java.lang.String value) {
      updateOperations.removeAll("requestedBy", value);
      return this;
    }

    public ChannelEventUpdater removeAllFromRequestedBy(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("requestedBy", values);
      return this;
    }
 
    public ChannelEventUpdater decRequestedBy() {
      updateOperations.dec("requestedBy");
      return this;
    }

    public ChannelEventUpdater incRequestedBy() {
      updateOperations.inc("requestedBy");
      return this;
    }

    public ChannelEventUpdater incRequestedBy(Number value) {
      updateOperations.inc("requestedBy", value);
      return this;
    }
    public ChannelEventUpdater requestedOn(java.time.LocalDateTime value) {
      updateOperations.set("requestedOn", value);
      return this;
    }

    public ChannelEventUpdater unsetRequestedOn(java.time.LocalDateTime value) {
      updateOperations.unset("requestedOn");
      return this;
    }

    public ChannelEventUpdater addRequestedOn(java.time.LocalDateTime value) {
      updateOperations.add("requestedOn", value);
      return this;
    }

    public ChannelEventUpdater addRequestedOn(java.time.LocalDateTime value, boolean addDups) {
      updateOperations.add("requestedOn", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToRequestedOn(java.util.List<java.time.LocalDateTime> values, boolean addDups) {
      updateOperations.addAll("requestedOn", values, addDups);
      return this;
    }
  
    public ChannelEventUpdater removeFirstRequestedOn() {
      updateOperations.removeFirst("requestedOn");
      return this;
    }
  
    public ChannelEventUpdater removeLastRequestedOn() {
      updateOperations.removeLast("requestedOn");
      return this;
    }
  
    public ChannelEventUpdater removeFromRequestedOn(java.time.LocalDateTime value) {
      updateOperations.removeAll("requestedOn", value);
      return this;
    }

    public ChannelEventUpdater removeAllFromRequestedOn(java.util.List<java.time.LocalDateTime> values) {
      updateOperations.removeAll("requestedOn", values);
      return this;
    }
 
    public ChannelEventUpdater decRequestedOn() {
      updateOperations.dec("requestedOn");
      return this;
    }

    public ChannelEventUpdater incRequestedOn() {
      updateOperations.inc("requestedOn");
      return this;
    }

    public ChannelEventUpdater incRequestedOn(Number value) {
      updateOperations.inc("requestedOn", value);
      return this;
    }
    public ChannelEventUpdater state(javabot.model.AdminEvent.State value) {
      updateOperations.set("state", value);
      return this;
    }

    public ChannelEventUpdater unsetState(javabot.model.AdminEvent.State value) {
      updateOperations.unset("state");
      return this;
    }

    public ChannelEventUpdater addState(javabot.model.AdminEvent.State value) {
      updateOperations.add("state", value);
      return this;
    }

    public ChannelEventUpdater addState(javabot.model.AdminEvent.State value, boolean addDups) {
      updateOperations.add("state", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToState(java.util.List<javabot.model.AdminEvent.State> values, boolean addDups) {
      updateOperations.addAll("state", values, addDups);
      return this;
    }
  
    public ChannelEventUpdater removeFirstState() {
      updateOperations.removeFirst("state");
      return this;
    }
  
    public ChannelEventUpdater removeLastState() {
      updateOperations.removeLast("state");
      return this;
    }
  
    public ChannelEventUpdater removeFromState(javabot.model.AdminEvent.State value) {
      updateOperations.removeAll("state", value);
      return this;
    }

    public ChannelEventUpdater removeAllFromState(java.util.List<javabot.model.AdminEvent.State> values) {
      updateOperations.removeAll("state", values);
      return this;
    }
 
    public ChannelEventUpdater decState() {
      updateOperations.dec("state");
      return this;
    }

    public ChannelEventUpdater incState() {
      updateOperations.inc("state");
      return this;
    }

    public ChannelEventUpdater incState(Number value) {
      updateOperations.inc("state", value);
      return this;
    }
    public ChannelEventUpdater type(javabot.model.EventType value) {
      updateOperations.set("type", value);
      return this;
    }

    public ChannelEventUpdater unsetType(javabot.model.EventType value) {
      updateOperations.unset("type");
      return this;
    }

    public ChannelEventUpdater addType(javabot.model.EventType value) {
      updateOperations.add("type", value);
      return this;
    }

    public ChannelEventUpdater addType(javabot.model.EventType value, boolean addDups) {
      updateOperations.add("type", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToType(java.util.List<javabot.model.EventType> values, boolean addDups) {
      updateOperations.addAll("type", values, addDups);
      return this;
    }
  
    public ChannelEventUpdater removeFirstType() {
      updateOperations.removeFirst("type");
      return this;
    }
  
    public ChannelEventUpdater removeLastType() {
      updateOperations.removeLast("type");
      return this;
    }
  
    public ChannelEventUpdater removeFromType(javabot.model.EventType value) {
      updateOperations.removeAll("type", value);
      return this;
    }

    public ChannelEventUpdater removeAllFromType(java.util.List<javabot.model.EventType> values) {
      updateOperations.removeAll("type", values);
      return this;
    }
 
    public ChannelEventUpdater decType() {
      updateOperations.dec("type");
      return this;
    }

    public ChannelEventUpdater incType() {
      updateOperations.inc("type");
      return this;
    }

    public ChannelEventUpdater incType(Number value) {
      updateOperations.inc("type", value);
      return this;
    }
  }
}
