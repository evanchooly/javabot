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

  public AdminCriteria distinctIrcName() {
    ((QueryImpl) query).getCollection().distinct("ircName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, java.util.Date> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public AdminCriteria updated(java.util.Date value) {
    new TypeSafeFieldEnd<>(query, query.criteria("updated")).equal(value);
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

  public AdminCriteria distinctUserName() {
    ((QueryImpl) query).getCollection().distinct("userName");
    return this;
  }
}
