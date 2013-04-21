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

  public FactoidCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.util.Date> lastUsed() {
    return new TypeSafeFieldEnd<>(query, query.criteria("lastUsed"));
  }

  public FactoidCriteria distinctLastUsed() {
    ((QueryImpl) query).getCollection().distinct("lastUsed");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.Boolean> locked() {
    return new TypeSafeFieldEnd<>(query, query.criteria("locked"));
  }

  public FactoidCriteria distinctLocked() {
    ((QueryImpl) query).getCollection().distinct("locked");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public FactoidCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.util.Date> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public FactoidCriteria distinctUpdated() {
    ((QueryImpl) query).getCollection().distinct("updated");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public FactoidCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> upperUserName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperUserName"));
  }

  public FactoidCriteria distinctUpperUserName() {
    ((QueryImpl) query).getCollection().distinct("upperUserName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> userName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("userName"));
  }

  public FactoidCriteria distinctUserName() {
    ((QueryImpl) query).getCollection().distinct("userName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> value() {
    return new TypeSafeFieldEnd<>(query, query.criteria("value"));
  }

  public FactoidCriteria distinctValue() {
    ((QueryImpl) query).getCollection().distinct("value");
    return this;
  }
}
