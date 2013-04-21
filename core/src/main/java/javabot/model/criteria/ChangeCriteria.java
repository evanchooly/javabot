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

  public ChangeCriteria distinctChangeDate() {
    ((QueryImpl) query).getCollection().distinct("changeDate");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Change, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ChangeCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Change, java.lang.String> message() {
    return new TypeSafeFieldEnd<>(query, query.criteria("message"));
  }

  public ChangeCriteria distinctMessage() {
    ((QueryImpl) query).getCollection().distinct("message");
    return this;
  }
}
