package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

public class KarmaCriteria {
  private Query<javabot.model.Karma> query;
  private Datastore ds;

  public Query<javabot.model.Karma> query() {
    return query;
  }

  public KarmaCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Karma.class);
  }

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Karma, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public KarmaCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public KarmaCriteria orderById() {
    return orderById(true);
  }

  public KarmaCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public KarmaCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Karma, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public KarmaCriteria name(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("name")).equal(value);
    return this;
  }

  public KarmaCriteria orderByName() {
    return orderByName(true);
  }

  public KarmaCriteria orderByName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "name");
    return this;
  }

  public KarmaCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Karma, org.joda.time.DateTime> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public KarmaCriteria updated(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("updated")).equal(value);
    return this;
  }

  public KarmaCriteria orderByUpdated() {
    return orderByUpdated(true);
  }

  public KarmaCriteria orderByUpdated(boolean ascending) {
    query.order((!ascending ? "-" : "") + "updated");
    return this;
  }

  public KarmaCriteria distinctUpdated() {
    ((QueryImpl) query).getCollection().distinct("updated");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Karma, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public KarmaCriteria upperName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperName")).equal(value);
    return this;
  }

  public KarmaCriteria orderByUpperName() {
    return orderByUpperName(true);
  }

  public KarmaCriteria orderByUpperName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperName");
    return this;
  }

  public KarmaCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Karma, java.lang.String> userName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("userName"));
  }

  public KarmaCriteria userName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("userName")).equal(value);
    return this;
  }

  public KarmaCriteria orderByUserName() {
    return orderByUserName(true);
  }

  public KarmaCriteria orderByUserName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "userName");
    return this;
  }

  public KarmaCriteria distinctUserName() {
    ((QueryImpl) query).getCollection().distinct("userName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Karma, java.lang.Integer> value() {
    return new TypeSafeFieldEnd<>(query, query.criteria("value"));
  }

  public KarmaCriteria value(java.lang.Integer value) {
    new TypeSafeFieldEnd<>(query, query.criteria("value")).equal(value);
    return this;
  }

  public KarmaCriteria orderByValue() {
    return orderByValue(true);
  }

  public KarmaCriteria orderByValue(boolean ascending) {
    query.order((!ascending ? "-" : "") + "value");
    return this;
  }

  public KarmaCriteria distinctValue() {
    ((QueryImpl) query).getCollection().distinct("value");
    return this;
  }
}
