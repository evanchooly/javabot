package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

public class ChangeCriteria {
  private Query<javabot.model.Change> query;
  private Datastore ds;

  public Query<javabot.model.Change> query() {
    return query;
  }

  public ChangeCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Change.class);
  }

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Change, java.util.Date> changeDate() {
    return new TypeSafeFieldEnd<>(query, query.criteria("changeDate"));
  }

  public ChangeCriteria changeDate(java.util.Date value) {
    new TypeSafeFieldEnd<>(query, query.criteria("changeDate")).equal(value);
    return this;
  }

  public ChangeCriteria orderByChangeDate() {
    return orderByChangeDate(true);
  }

  public ChangeCriteria orderByChangeDate(boolean ascending) {
    query.order((!ascending ? "-" : "") + "changeDate");
    return this;
  }

  public ChangeCriteria distinctChangeDate() {
    ((QueryImpl) query).getCollection().distinct("changeDate");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Change, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ChangeCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public ChangeCriteria orderById() {
    return orderById(true);
  }

  public ChangeCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public ChangeCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Change, java.lang.String> message() {
    return new TypeSafeFieldEnd<>(query, query.criteria("message"));
  }

  public ChangeCriteria message(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("message")).equal(value);
    return this;
  }

  public ChangeCriteria orderByMessage() {
    return orderByMessage(true);
  }

  public ChangeCriteria orderByMessage(boolean ascending) {
    query.order((!ascending ? "-" : "") + "message");
    return this;
  }

  public ChangeCriteria distinctMessage() {
    ((QueryImpl) query).getCollection().distinct("message");
    return this;
  }
}
