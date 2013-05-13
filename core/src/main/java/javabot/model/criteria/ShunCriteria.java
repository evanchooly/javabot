package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

public class ShunCriteria {
  private Query<javabot.model.Shun> query;
  private Datastore ds;

  public Query<javabot.model.Shun> query() {
    return query;
  }

  public ShunCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Shun.class);
  }

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Shun, org.joda.time.DateTime> expiry() {
    return new TypeSafeFieldEnd<>(query, query.criteria("expiry"));
  }

  public ShunCriteria expiry(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("expiry")).equal(value);
    return this;
  }

  public ShunCriteria orderByExpiry() {
    return orderByExpiry(true);
  }

  public ShunCriteria orderByExpiry(boolean ascending) {
    query.order((!ascending ? "-" : "") + "expiry");
    return this;
  }

  public ShunCriteria distinctExpiry() {
    ((QueryImpl) query).getCollection().distinct("expiry");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Shun, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ShunCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public ShunCriteria orderById() {
    return orderById(true);
  }

  public ShunCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public ShunCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Shun, java.lang.String> nick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("nick"));
  }

  public ShunCriteria nick(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("nick")).equal(value);
    return this;
  }

  public ShunCriteria orderByNick() {
    return orderByNick(true);
  }

  public ShunCriteria orderByNick(boolean ascending) {
    query.order((!ascending ? "-" : "") + "nick");
    return this;
  }

  public ShunCriteria distinctNick() {
    ((QueryImpl) query).getCollection().distinct("nick");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Shun, java.lang.String> upperNick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperNick"));
  }

  public ShunCriteria upperNick(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperNick")).equal(value);
    return this;
  }

  public ShunCriteria orderByUpperNick() {
    return orderByUpperNick(true);
  }

  public ShunCriteria orderByUpperNick(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperNick");
    return this;
  }

  public ShunCriteria distinctUpperNick() {
    ((QueryImpl) query).getCollection().distinct("upperNick");
    return this;
  }
}
