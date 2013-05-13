package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

public class FactoidCriteria {
  private Query<javabot.model.Factoid> query;
  private Datastore ds;

  public Query<javabot.model.Factoid> query() {
    return query;
  }

  public FactoidCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Factoid.class);
  }

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public FactoidCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public FactoidCriteria orderById() {
    return orderById(true);
  }

  public FactoidCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public FactoidCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, org.joda.time.DateTime> lastUsed() {
    return new TypeSafeFieldEnd<>(query, query.criteria("lastUsed"));
  }

  public FactoidCriteria lastUsed(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("lastUsed")).equal(value);
    return this;
  }

  public FactoidCriteria orderByLastUsed() {
    return orderByLastUsed(true);
  }

  public FactoidCriteria orderByLastUsed(boolean ascending) {
    query.order((!ascending ? "-" : "") + "lastUsed");
    return this;
  }

  public FactoidCriteria distinctLastUsed() {
    ((QueryImpl) query).getCollection().distinct("lastUsed");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.Boolean> locked() {
    return new TypeSafeFieldEnd<>(query, query.criteria("locked"));
  }

  public FactoidCriteria locked(java.lang.Boolean value) {
    new TypeSafeFieldEnd<>(query, query.criteria("locked")).equal(value);
    return this;
  }

  public FactoidCriteria orderByLocked() {
    return orderByLocked(true);
  }

  public FactoidCriteria orderByLocked(boolean ascending) {
    query.order((!ascending ? "-" : "") + "locked");
    return this;
  }

  public FactoidCriteria distinctLocked() {
    ((QueryImpl) query).getCollection().distinct("locked");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public FactoidCriteria name(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("name")).equal(value);
    return this;
  }

  public FactoidCriteria orderByName() {
    return orderByName(true);
  }

  public FactoidCriteria orderByName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "name");
    return this;
  }

  public FactoidCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, org.joda.time.DateTime> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public FactoidCriteria updated(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("updated")).equal(value);
    return this;
  }

  public FactoidCriteria orderByUpdated() {
    return orderByUpdated(true);
  }

  public FactoidCriteria orderByUpdated(boolean ascending) {
    query.order((!ascending ? "-" : "") + "updated");
    return this;
  }

  public FactoidCriteria distinctUpdated() {
    ((QueryImpl) query).getCollection().distinct("updated");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public FactoidCriteria upperName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperName")).equal(value);
    return this;
  }

  public FactoidCriteria orderByUpperName() {
    return orderByUpperName(true);
  }

  public FactoidCriteria orderByUpperName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperName");
    return this;
  }

  public FactoidCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> upperUserName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperUserName"));
  }

  public FactoidCriteria upperUserName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperUserName")).equal(value);
    return this;
  }

  public FactoidCriteria orderByUpperUserName() {
    return orderByUpperUserName(true);
  }

  public FactoidCriteria orderByUpperUserName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperUserName");
    return this;
  }

  public FactoidCriteria distinctUpperUserName() {
    ((QueryImpl) query).getCollection().distinct("upperUserName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> userName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("userName"));
  }

  public FactoidCriteria userName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("userName")).equal(value);
    return this;
  }

  public FactoidCriteria orderByUserName() {
    return orderByUserName(true);
  }

  public FactoidCriteria orderByUserName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "userName");
    return this;
  }

  public FactoidCriteria distinctUserName() {
    ((QueryImpl) query).getCollection().distinct("userName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> value() {
    return new TypeSafeFieldEnd<>(query, query.criteria("value"));
  }

  public FactoidCriteria value(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("value")).equal(value);
    return this;
  }

  public FactoidCriteria orderByValue() {
    return orderByValue(true);
  }

  public FactoidCriteria orderByValue(boolean ascending) {
    query.order((!ascending ? "-" : "") + "value");
    return this;
  }

  public FactoidCriteria distinctValue() {
    ((QueryImpl) query).getCollection().distinct("value");
    return this;
  }
}
