package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;
import com.google.code.morphia.query.UpdateOperations;
import com.google.code.morphia.query.UpdateResults;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import java.util.List;


public class ChannelEventCriteria {
  private Query<javabot.model.ChannelEvent> query;
  private Datastore ds;

  public Query<javabot.model.ChannelEvent> query() {
    return query;
  }

  public ChannelEventCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.ChannelEvent.class);
  }

  public WriteResult delete() {
     return ds.delete(query());
  }

  public WriteResult delete(WriteConcern wc) {
     return ds.delete(query(), wc);
  }

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, java.lang.String> channel() {
    return new TypeSafeFieldEnd<>(query, query.criteria("channel"));
  }

  public ChannelEventCriteria channel(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("channel")).equal(value);
    return this;
  }

  public ChannelEventCriteria orderByChannel() {
    return orderByChannel(true);
  }

  public ChannelEventCriteria orderByChannel(boolean ascending) {
    query.order((!ascending ? "-" : "") + "channel");
    return this;
  }

  public ChannelEventCriteria distinctChannel() {
    ((QueryImpl) query).getCollection().distinct("channel");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, org.joda.time.DateTime> completed() {
    return new TypeSafeFieldEnd<>(query, query.criteria("completed"));
  }

  public ChannelEventCriteria completed(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("completed")).equal(value);
    return this;
  }

  public ChannelEventCriteria orderByCompleted() {
    return orderByCompleted(true);
  }

  public ChannelEventCriteria orderByCompleted(boolean ascending) {
    query.order((!ascending ? "-" : "") + "completed");
    return this;
  }

  public ChannelEventCriteria distinctCompleted() {
    ((QueryImpl) query).getCollection().distinct("completed");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ChannelEventCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public ChannelEventCriteria orderById() {
    return orderById(true);
  }

  public ChannelEventCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public ChannelEventCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, java.lang.String> key() {
    return new TypeSafeFieldEnd<>(query, query.criteria("key"));
  }

  public ChannelEventCriteria key(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("key")).equal(value);
    return this;
  }

  public ChannelEventCriteria orderByKey() {
    return orderByKey(true);
  }

  public ChannelEventCriteria orderByKey(boolean ascending) {
    query.order((!ascending ? "-" : "") + "key");
    return this;
  }

  public ChannelEventCriteria distinctKey() {
    ((QueryImpl) query).getCollection().distinct("key");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, java.lang.Boolean> logged() {
    return new TypeSafeFieldEnd<>(query, query.criteria("logged"));
  }

  public ChannelEventCriteria logged(java.lang.Boolean value) {
    new TypeSafeFieldEnd<>(query, query.criteria("logged")).equal(value);
    return this;
  }

  public ChannelEventCriteria orderByLogged() {
    return orderByLogged(true);
  }

  public ChannelEventCriteria orderByLogged(boolean ascending) {
    query.order((!ascending ? "-" : "") + "logged");
    return this;
  }

  public ChannelEventCriteria distinctLogged() {
    ((QueryImpl) query).getCollection().distinct("logged");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, java.lang.String> requestedBy() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedBy"));
  }

  public ChannelEventCriteria requestedBy(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("requestedBy")).equal(value);
    return this;
  }

  public ChannelEventCriteria orderByRequestedBy() {
    return orderByRequestedBy(true);
  }

  public ChannelEventCriteria orderByRequestedBy(boolean ascending) {
    query.order((!ascending ? "-" : "") + "requestedBy");
    return this;
  }

  public ChannelEventCriteria distinctRequestedBy() {
    ((QueryImpl) query).getCollection().distinct("requestedBy");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, org.joda.time.DateTime> requestedOn() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedOn"));
  }

  public ChannelEventCriteria requestedOn(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("requestedOn")).equal(value);
    return this;
  }

  public ChannelEventCriteria orderByRequestedOn() {
    return orderByRequestedOn(true);
  }

  public ChannelEventCriteria orderByRequestedOn(boolean ascending) {
    query.order((!ascending ? "-" : "") + "requestedOn");
    return this;
  }

  public ChannelEventCriteria distinctRequestedOn() {
    ((QueryImpl) query).getCollection().distinct("requestedOn");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, javabot.model.AdminEvent.State> state() {
    return new TypeSafeFieldEnd<>(query, query.criteria("state"));
  }

  public ChannelEventCriteria state(javabot.model.AdminEvent.State value) {
    new TypeSafeFieldEnd<>(query, query.criteria("state")).equal(value);
    return this;
  }

  public ChannelEventCriteria orderByState() {
    return orderByState(true);
  }

  public ChannelEventCriteria orderByState(boolean ascending) {
    query.order((!ascending ? "-" : "") + "state");
    return this;
  }

  public ChannelEventCriteria distinctState() {
    ((QueryImpl) query).getCollection().distinct("state");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, javabot.model.EventType> type() {
    return new TypeSafeFieldEnd<>(query, query.criteria("type"));
  }

  public ChannelEventCriteria type(javabot.model.EventType value) {
    new TypeSafeFieldEnd<>(query, query.criteria("type")).equal(value);
    return this;
  }

  public ChannelEventCriteria orderByType() {
    return orderByType(true);
  }

  public ChannelEventCriteria orderByType(boolean ascending) {
    query.order((!ascending ? "-" : "") + "type");
    return this;
  }

  public ChannelEventCriteria distinctType() {
    ((QueryImpl) query).getCollection().distinct("type");
    return this;
  }

  public ChannelEventUpdater getUpdater() {
    return new ChannelEventUpdater();
  }

  public class ChannelEventUpdater {
    UpdateOperations<javabot.model.ChannelEvent> updateOperations;

    public ChannelEventUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.ChannelEvent.class);
    }

    public UpdateResults<javabot.model.ChannelEvent> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.ChannelEvent> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.ChannelEvent> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.ChannelEvent> upsert(WriteConcern wc) {
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

    public ChannelEventUpdater addChannel(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("channel", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToChannel(List<java.lang.String> values, boolean addDups) {
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

    public ChannelEventUpdater removeAllFromChannel(List<java.lang.String> values) {
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
    public ChannelEventUpdater completed(org.joda.time.DateTime value) {
      updateOperations.set("completed", value);
      return this;
    }

    public ChannelEventUpdater unsetCompleted(org.joda.time.DateTime value) {
      updateOperations.unset("completed");
      return this;
    }

    public ChannelEventUpdater addCompleted(org.joda.time.DateTime value) {
      updateOperations.add("completed", value);
      return this;
    }

    public ChannelEventUpdater addCompleted(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("completed", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToCompleted(List<org.joda.time.DateTime> values, boolean addDups) {
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
  
    public ChannelEventUpdater removeFromCompleted(org.joda.time.DateTime value) {
      updateOperations.removeAll("completed", value);
      return this;
    }

    public ChannelEventUpdater removeAllFromCompleted(List<org.joda.time.DateTime> values) {
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

    public ChannelEventUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public ChannelEventUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
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

    public ChannelEventUpdater addKey(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("key", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToKey(List<java.lang.String> values, boolean addDups) {
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

    public ChannelEventUpdater removeAllFromKey(List<java.lang.String> values) {
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

    public ChannelEventUpdater addLogged(String fieldExpr, java.lang.Boolean value, boolean addDups) {
      updateOperations.add("logged", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToLogged(List<java.lang.Boolean> values, boolean addDups) {
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

    public ChannelEventUpdater removeAllFromLogged(List<java.lang.Boolean> values) {
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

    public ChannelEventUpdater addRequestedBy(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("requestedBy", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToRequestedBy(List<java.lang.String> values, boolean addDups) {
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

    public ChannelEventUpdater removeAllFromRequestedBy(List<java.lang.String> values) {
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
    public ChannelEventUpdater requestedOn(org.joda.time.DateTime value) {
      updateOperations.set("requestedOn", value);
      return this;
    }

    public ChannelEventUpdater unsetRequestedOn(org.joda.time.DateTime value) {
      updateOperations.unset("requestedOn");
      return this;
    }

    public ChannelEventUpdater addRequestedOn(org.joda.time.DateTime value) {
      updateOperations.add("requestedOn", value);
      return this;
    }

    public ChannelEventUpdater addRequestedOn(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("requestedOn", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToRequestedOn(List<org.joda.time.DateTime> values, boolean addDups) {
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
  
    public ChannelEventUpdater removeFromRequestedOn(org.joda.time.DateTime value) {
      updateOperations.removeAll("requestedOn", value);
      return this;
    }

    public ChannelEventUpdater removeAllFromRequestedOn(List<org.joda.time.DateTime> values) {
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

    public ChannelEventUpdater addState(String fieldExpr, javabot.model.AdminEvent.State value, boolean addDups) {
      updateOperations.add("state", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToState(List<javabot.model.AdminEvent.State> values, boolean addDups) {
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

    public ChannelEventUpdater removeAllFromState(List<javabot.model.AdminEvent.State> values) {
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

    public ChannelEventUpdater addType(String fieldExpr, javabot.model.EventType value, boolean addDups) {
      updateOperations.add("type", value, addDups);
      return this;
    }

    public ChannelEventUpdater addAllToType(List<javabot.model.EventType> values, boolean addDups) {
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

    public ChannelEventUpdater removeAllFromType(List<javabot.model.EventType> values) {
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
