package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

public class LogsCriteria {
  private Query<javabot.model.Logs> query;
  private Datastore ds;

  public Query<javabot.model.Logs> query() {
    return query;
  }

  public LogsCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Logs.class);
  }

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, java.lang.String> channel() {
    return new TypeSafeFieldEnd<>(query, query.criteria("channel"));
  }

  public LogsCriteria channel(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("channel")).equal(value);
    return this;
  }

  public LogsCriteria distinctChannel() {
    ((QueryImpl) query).getCollection().distinct("channel");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public LogsCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public LogsCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, java.lang.String> message() {
    return new TypeSafeFieldEnd<>(query, query.criteria("message"));
  }

  public LogsCriteria message(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("message")).equal(value);
    return this;
  }

  public LogsCriteria distinctMessage() {
    ((QueryImpl) query).getCollection().distinct("message");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, java.lang.String> nick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("nick"));
  }

  public LogsCriteria nick(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("nick")).equal(value);
    return this;
  }

  public LogsCriteria distinctNick() {
    ((QueryImpl) query).getCollection().distinct("nick");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, javabot.model.Logs.Type> type() {
    return new TypeSafeFieldEnd<>(query, query.criteria("type"));
  }

  public LogsCriteria type(javabot.model.Logs.Type value) {
    new TypeSafeFieldEnd<>(query, query.criteria("type")).equal(value);
    return this;
  }

  public LogsCriteria distinctType() {
    ((QueryImpl) query).getCollection().distinct("type");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, java.util.Date> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public LogsCriteria updated(java.util.Date value) {
    new TypeSafeFieldEnd<>(query, query.criteria("updated")).equal(value);
    return this;
  }

  public LogsCriteria distinctUpdated() {
    ((QueryImpl) query).getCollection().distinct("updated");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, java.lang.String> upperNick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperNick"));
  }

  public LogsCriteria upperNick(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperNick")).equal(value);
    return this;
  }

  public LogsCriteria distinctUpperNick() {
    ((QueryImpl) query).getCollection().distinct("upperNick");
    return this;
  }
}
