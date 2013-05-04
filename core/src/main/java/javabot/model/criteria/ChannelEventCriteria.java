package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, java.util.Date> completed() {
    return new TypeSafeFieldEnd<>(query, query.criteria("completed"));
  }

  public ChannelEventCriteria completed(java.util.Date value) {
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, java.util.Date> requestedOn() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedOn"));
  }

  public ChannelEventCriteria requestedOn(java.util.Date value) {
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
}
