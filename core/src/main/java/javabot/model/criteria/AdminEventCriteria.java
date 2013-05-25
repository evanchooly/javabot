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


public class AdminEventCriteria {
  private Query<javabot.model.AdminEvent> query;
  private Datastore ds;

  public Query<javabot.model.AdminEvent> query() {
    return query;
  }

  public AdminEventCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.AdminEvent.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.AdminEvent, org.joda.time.DateTime> completed() {
    return new TypeSafeFieldEnd<>(query, query.criteria("completed"));
  }

  public AdminEventCriteria completed(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("completed")).equal(value);
    return this;
  }

  public AdminEventCriteria orderByCompleted() {
    return orderByCompleted(true);
  }

  public AdminEventCriteria orderByCompleted(boolean ascending) {
    query.order((!ascending ? "-" : "") + "completed");
    return this;
  }

  public AdminEventCriteria distinctCompleted() {
    ((QueryImpl) query).getCollection().distinct("completed");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.AdminEvent, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public AdminEventCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public AdminEventCriteria orderById() {
    return orderById(true);
  }

  public AdminEventCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public AdminEventCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.AdminEvent, java.lang.String> requestedBy() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedBy"));
  }

  public AdminEventCriteria requestedBy(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("requestedBy")).equal(value);
    return this;
  }

  public AdminEventCriteria orderByRequestedBy() {
    return orderByRequestedBy(true);
  }

  public AdminEventCriteria orderByRequestedBy(boolean ascending) {
    query.order((!ascending ? "-" : "") + "requestedBy");
    return this;
  }

  public AdminEventCriteria distinctRequestedBy() {
    ((QueryImpl) query).getCollection().distinct("requestedBy");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.AdminEvent, org.joda.time.DateTime> requestedOn() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedOn"));
  }

  public AdminEventCriteria requestedOn(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("requestedOn")).equal(value);
    return this;
  }

  public AdminEventCriteria orderByRequestedOn() {
    return orderByRequestedOn(true);
  }

  public AdminEventCriteria orderByRequestedOn(boolean ascending) {
    query.order((!ascending ? "-" : "") + "requestedOn");
    return this;
  }

  public AdminEventCriteria distinctRequestedOn() {
    ((QueryImpl) query).getCollection().distinct("requestedOn");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.AdminEvent, javabot.model.AdminEvent.State> state() {
    return new TypeSafeFieldEnd<>(query, query.criteria("state"));
  }

  public AdminEventCriteria state(javabot.model.AdminEvent.State value) {
    new TypeSafeFieldEnd<>(query, query.criteria("state")).equal(value);
    return this;
  }

  public AdminEventCriteria orderByState() {
    return orderByState(true);
  }

  public AdminEventCriteria orderByState(boolean ascending) {
    query.order((!ascending ? "-" : "") + "state");
    return this;
  }

  public AdminEventCriteria distinctState() {
    ((QueryImpl) query).getCollection().distinct("state");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.AdminEvent, javabot.model.EventType> type() {
    return new TypeSafeFieldEnd<>(query, query.criteria("type"));
  }

  public AdminEventCriteria type(javabot.model.EventType value) {
    new TypeSafeFieldEnd<>(query, query.criteria("type")).equal(value);
    return this;
  }

  public AdminEventCriteria orderByType() {
    return orderByType(true);
  }

  public AdminEventCriteria orderByType(boolean ascending) {
    query.order((!ascending ? "-" : "") + "type");
    return this;
  }

  public AdminEventCriteria distinctType() {
    ((QueryImpl) query).getCollection().distinct("type");
    return this;
  }

  public AdminEventUpdater getUpdater() {
    return new AdminEventUpdater();
  }

  public class AdminEventUpdater {
    UpdateOperations<javabot.model.AdminEvent> updateOperations;

    public AdminEventUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.AdminEvent.class);
    }

    public UpdateResults<javabot.model.AdminEvent> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.AdminEvent> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.AdminEvent> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.AdminEvent> upsert(WriteConcern wc) {
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

    public AdminEventUpdater addCompleted(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("completed", value, addDups);
      return this;
    }

    public AdminEventUpdater addAllToCompleted(List<org.joda.time.DateTime> values, boolean addDups) {
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

    public AdminEventUpdater removeAllFromCompleted(List<org.joda.time.DateTime> values) {
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

    public AdminEventUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public AdminEventUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public AdminEventUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
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

    public AdminEventUpdater addRequestedBy(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("requestedBy", value, addDups);
      return this;
    }

    public AdminEventUpdater addAllToRequestedBy(List<java.lang.String> values, boolean addDups) {
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

    public AdminEventUpdater removeAllFromRequestedBy(List<java.lang.String> values) {
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

    public AdminEventUpdater addRequestedOn(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("requestedOn", value, addDups);
      return this;
    }

    public AdminEventUpdater addAllToRequestedOn(List<org.joda.time.DateTime> values, boolean addDups) {
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

    public AdminEventUpdater removeAllFromRequestedOn(List<org.joda.time.DateTime> values) {
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

    public AdminEventUpdater addState(String fieldExpr, javabot.model.AdminEvent.State value, boolean addDups) {
      updateOperations.add("state", value, addDups);
      return this;
    }

    public AdminEventUpdater addAllToState(List<javabot.model.AdminEvent.State> values, boolean addDups) {
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

    public AdminEventUpdater removeAllFromState(List<javabot.model.AdminEvent.State> values) {
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

    public AdminEventUpdater addType(String fieldExpr, javabot.model.EventType value, boolean addDups) {
      updateOperations.add("type", value, addDups);
      return this;
    }

    public AdminEventUpdater addAllToType(List<javabot.model.EventType> values, boolean addDups) {
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

    public AdminEventUpdater removeAllFromType(List<javabot.model.EventType> values) {
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
