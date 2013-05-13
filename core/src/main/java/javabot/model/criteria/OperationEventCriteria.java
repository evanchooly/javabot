package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

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
}
