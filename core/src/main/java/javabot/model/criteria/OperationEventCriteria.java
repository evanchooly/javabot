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


public class OperationEventCriteria {
  private Query<javabot.model.OperationEvent> query;
  private Datastore ds;

  public Query<javabot.model.OperationEvent> query() {
    return query;
  }

  public OperationEventCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.OperationEvent.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.OperationEvent, org.joda.time.DateTime> completed() {
    return new TypeSafeFieldEnd<>(query, query.criteria("completed"));
  }

  public OperationEventCriteria completed(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("completed")).equal(value);
    return this;
  }

  public OperationEventCriteria orderByCompleted() {
    return orderByCompleted(true);
  }

  public OperationEventCriteria orderByCompleted(boolean ascending) {
    query.order((!ascending ? "-" : "") + "completed");
    return this;
  }

  public OperationEventCriteria distinctCompleted() {
    ((QueryImpl) query).getCollection().distinct("completed");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.OperationEvent, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public OperationEventCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public OperationEventCriteria orderById() {
    return orderById(true);
  }

  public OperationEventCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public OperationEventCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.OperationEvent, java.lang.String> operation() {
    return new TypeSafeFieldEnd<>(query, query.criteria("operation"));
  }

  public OperationEventCriteria operation(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("operation")).equal(value);
    return this;
  }

  public OperationEventCriteria orderByOperation() {
    return orderByOperation(true);
  }

  public OperationEventCriteria orderByOperation(boolean ascending) {
    query.order((!ascending ? "-" : "") + "operation");
    return this;
  }

  public OperationEventCriteria distinctOperation() {
    ((QueryImpl) query).getCollection().distinct("operation");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.OperationEvent, java.lang.String> requestedBy() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedBy"));
  }

  public OperationEventCriteria requestedBy(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("requestedBy")).equal(value);
    return this;
  }

  public OperationEventCriteria orderByRequestedBy() {
    return orderByRequestedBy(true);
  }

  public OperationEventCriteria orderByRequestedBy(boolean ascending) {
    query.order((!ascending ? "-" : "") + "requestedBy");
    return this;
  }

  public OperationEventCriteria distinctRequestedBy() {
    ((QueryImpl) query).getCollection().distinct("requestedBy");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.OperationEvent, org.joda.time.DateTime> requestedOn() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedOn"));
  }

  public OperationEventCriteria requestedOn(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("requestedOn")).equal(value);
    return this;
  }

  public OperationEventCriteria orderByRequestedOn() {
    return orderByRequestedOn(true);
  }

  public OperationEventCriteria orderByRequestedOn(boolean ascending) {
    query.order((!ascending ? "-" : "") + "requestedOn");
    return this;
  }

  public OperationEventCriteria distinctRequestedOn() {
    ((QueryImpl) query).getCollection().distinct("requestedOn");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.OperationEvent, javabot.model.AdminEvent.State> state() {
    return new TypeSafeFieldEnd<>(query, query.criteria("state"));
  }

  public OperationEventCriteria state(javabot.model.AdminEvent.State value) {
    new TypeSafeFieldEnd<>(query, query.criteria("state")).equal(value);
    return this;
  }

  public OperationEventCriteria orderByState() {
    return orderByState(true);
  }

  public OperationEventCriteria orderByState(boolean ascending) {
    query.order((!ascending ? "-" : "") + "state");
    return this;
  }

  public OperationEventCriteria distinctState() {
    ((QueryImpl) query).getCollection().distinct("state");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.OperationEvent, javabot.model.EventType> type() {
    return new TypeSafeFieldEnd<>(query, query.criteria("type"));
  }

  public OperationEventCriteria type(javabot.model.EventType value) {
    new TypeSafeFieldEnd<>(query, query.criteria("type")).equal(value);
    return this;
  }

  public OperationEventCriteria orderByType() {
    return orderByType(true);
  }

  public OperationEventCriteria orderByType(boolean ascending) {
    query.order((!ascending ? "-" : "") + "type");
    return this;
  }

  public OperationEventCriteria distinctType() {
    ((QueryImpl) query).getCollection().distinct("type");
    return this;
  }

  public OperationEventUpdater getUpdater() {
    return new OperationEventUpdater();
  }

  public class OperationEventUpdater {
    UpdateOperations<javabot.model.OperationEvent> updateOperations;

    public OperationEventUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.OperationEvent.class);
    }

    public UpdateResults<javabot.model.OperationEvent> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.OperationEvent> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.OperationEvent> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.OperationEvent> upsert(WriteConcern wc) {
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

    public OperationEventUpdater addCompleted(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("completed", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToCompleted(List<org.joda.time.DateTime> values, boolean addDups) {
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

    public OperationEventUpdater removeAllFromCompleted(List<org.joda.time.DateTime> values) {
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

    public OperationEventUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public OperationEventUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
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

    public OperationEventUpdater addOperation(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("operation", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToOperation(List<java.lang.String> values, boolean addDups) {
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

    public OperationEventUpdater removeAllFromOperation(List<java.lang.String> values) {
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

    public OperationEventUpdater addRequestedBy(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("requestedBy", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToRequestedBy(List<java.lang.String> values, boolean addDups) {
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

    public OperationEventUpdater removeAllFromRequestedBy(List<java.lang.String> values) {
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

    public OperationEventUpdater addRequestedOn(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("requestedOn", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToRequestedOn(List<org.joda.time.DateTime> values, boolean addDups) {
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

    public OperationEventUpdater removeAllFromRequestedOn(List<org.joda.time.DateTime> values) {
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

    public OperationEventUpdater addState(String fieldExpr, javabot.model.AdminEvent.State value, boolean addDups) {
      updateOperations.add("state", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToState(List<javabot.model.AdminEvent.State> values, boolean addDups) {
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

    public OperationEventUpdater removeAllFromState(List<javabot.model.AdminEvent.State> values) {
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

    public OperationEventUpdater addType(String fieldExpr, javabot.model.EventType value, boolean addDups) {
      updateOperations.add("type", value, addDups);
      return this;
    }

    public OperationEventUpdater addAllToType(List<javabot.model.EventType> values, boolean addDups) {
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

    public OperationEventUpdater removeAllFromType(List<javabot.model.EventType> values) {
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
