package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

public class AdminCriteria {
  private Query<javabot.model.Admin> query;
  private Datastore ds;

  public Query<javabot.model.Admin> query() {
    return query;
  }

  public AdminCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Admin.class);
  }

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, java.lang.String> addedBy() {
    return new TypeSafeFieldEnd<>(query, query.criteria("addedBy"));
  }

  public AdminCriteria addedBy(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("addedBy")).equal(value);
    return this;
  }

  public AdminCriteria orderByAddedBy() {
    return orderByAddedBy(true);
  }

  public AdminCriteria orderByAddedBy(boolean ascending) {
    query.order((!ascending ? "-" : "") + "addedBy");
    return this;
  }

  public AdminCriteria distinctAddedBy() {
    ((QueryImpl) query).getCollection().distinct("addedBy");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, java.lang.Boolean> botOwner() {
    return new TypeSafeFieldEnd<>(query, query.criteria("botOwner"));
  }

  public AdminCriteria botOwner(java.lang.Boolean value) {
    new TypeSafeFieldEnd<>(query, query.criteria("botOwner")).equal(value);
    return this;
  }

  public AdminCriteria orderByBotOwner() {
    return orderByBotOwner(true);
  }

  public AdminCriteria orderByBotOwner(boolean ascending) {
    query.order((!ascending ? "-" : "") + "botOwner");
    return this;
  }

  public AdminCriteria distinctBotOwner() {
    ((QueryImpl) query).getCollection().distinct("botOwner");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, java.lang.String> hostName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("hostName"));
  }

  public AdminCriteria hostName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("hostName")).equal(value);
    return this;
  }

  public AdminCriteria orderByHostName() {
    return orderByHostName(true);
  }

  public AdminCriteria orderByHostName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "hostName");
    return this;
  }

  public AdminCriteria distinctHostName() {
    ((QueryImpl) query).getCollection().distinct("hostName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public AdminCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public AdminCriteria orderById() {
    return orderById(true);
  }

  public AdminCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public AdminCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, java.lang.String> ircName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("ircName"));
  }

  public AdminCriteria ircName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("ircName")).equal(value);
    return this;
  }

  public AdminCriteria orderByIrcName() {
    return orderByIrcName(true);
  }

  public AdminCriteria orderByIrcName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "ircName");
    return this;
  }

  public AdminCriteria distinctIrcName() {
    ((QueryImpl) query).getCollection().distinct("ircName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, org.joda.time.DateTime> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public AdminCriteria updated(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("updated")).equal(value);
    return this;
  }

  public AdminCriteria orderByUpdated() {
    return orderByUpdated(true);
  }

  public AdminCriteria orderByUpdated(boolean ascending) {
    query.order((!ascending ? "-" : "") + "updated");
    return this;
  }

  public AdminCriteria distinctUpdated() {
    ((QueryImpl) query).getCollection().distinct("updated");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, java.lang.String> userName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("userName"));
  }

  public AdminCriteria userName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("userName")).equal(value);
    return this;
  }

  public AdminCriteria orderByUserName() {
    return orderByUserName(true);
  }

  public AdminCriteria orderByUserName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "userName");
    return this;
  }

  public AdminCriteria distinctUserName() {
    ((QueryImpl) query).getCollection().distinct("userName");
    return this;
  }
}
