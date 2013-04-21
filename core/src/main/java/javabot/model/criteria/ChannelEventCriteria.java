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

  public ChannelEventCriteria distinctChannel() {
    ((QueryImpl) query).getCollection().distinct("channel");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, java.util.Date> completed() {
    return new TypeSafeFieldEnd<>(query, query.criteria("completed"));
  }

  public ChannelEventCriteria distinctCompleted() {
    ((QueryImpl) query).getCollection().distinct("completed");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ChannelEventCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, java.lang.String> key() {
    return new TypeSafeFieldEnd<>(query, query.criteria("key"));
  }

  public ChannelEventCriteria distinctKey() {
    ((QueryImpl) query).getCollection().distinct("key");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, java.lang.Boolean> logged() {
    return new TypeSafeFieldEnd<>(query, query.criteria("logged"));
  }

  public ChannelEventCriteria distinctLogged() {
    ((QueryImpl) query).getCollection().distinct("logged");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, java.lang.String> requestedBy() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedBy"));
  }

  public ChannelEventCriteria distinctRequestedBy() {
    ((QueryImpl) query).getCollection().distinct("requestedBy");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, java.util.Date> requestedOn() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedOn"));
  }

  public ChannelEventCriteria distinctRequestedOn() {
    ((QueryImpl) query).getCollection().distinct("requestedOn");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, javabot.model.AdminEvent.State> state() {
    return new TypeSafeFieldEnd<>(query, query.criteria("state"));
  }

  public ChannelEventCriteria distinctState() {
    ((QueryImpl) query).getCollection().distinct("state");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ChannelEvent, javabot.model.EventType> type() {
    return new TypeSafeFieldEnd<>(query, query.criteria("type"));
  }

  public ChannelEventCriteria distinctType() {
    ((QueryImpl) query).getCollection().distinct("type");
    return this;
  }
}
