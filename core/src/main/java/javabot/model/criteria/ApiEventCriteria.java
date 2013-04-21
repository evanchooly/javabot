package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

public class ApiEventCriteria {
  private Query<javabot.model.ApiEvent> query;
  private Datastore ds;

  public Query<javabot.model.ApiEvent> query() {
    return query;
  }

  public ApiEventCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.ApiEvent.class);
  }

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.lang.String> baseUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("baseUrl"));
  }

  public ApiEventCriteria distinctBaseUrl() {
    ((QueryImpl) query).getCollection().distinct("baseUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.util.Date> completed() {
    return new TypeSafeFieldEnd<>(query, query.criteria("completed"));
  }

  public ApiEventCriteria distinctCompleted() {
    ((QueryImpl) query).getCollection().distinct("completed");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.lang.String> file() {
    return new TypeSafeFieldEnd<>(query, query.criteria("file"));
  }

  public ApiEventCriteria distinctFile() {
    ((QueryImpl) query).getCollection().distinct("file");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ApiEventCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public ApiEventCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.lang.Boolean> newApi() {
    return new TypeSafeFieldEnd<>(query, query.criteria("newApi"));
  }

  public ApiEventCriteria distinctNewApi() {
    ((QueryImpl) query).getCollection().distinct("newApi");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.lang.String> packages() {
    return new TypeSafeFieldEnd<>(query, query.criteria("packages"));
  }

  public ApiEventCriteria distinctPackages() {
    ((QueryImpl) query).getCollection().distinct("packages");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.lang.String> requestedBy() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedBy"));
  }

  public ApiEventCriteria distinctRequestedBy() {
    ((QueryImpl) query).getCollection().distinct("requestedBy");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.util.Date> requestedOn() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedOn"));
  }

  public ApiEventCriteria distinctRequestedOn() {
    ((QueryImpl) query).getCollection().distinct("requestedOn");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, javabot.model.AdminEvent.State> state() {
    return new TypeSafeFieldEnd<>(query, query.criteria("state"));
  }

  public ApiEventCriteria distinctState() {
    ((QueryImpl) query).getCollection().distinct("state");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, javabot.model.EventType> type() {
    return new TypeSafeFieldEnd<>(query, query.criteria("type"));
  }

  public ApiEventCriteria distinctType() {
    ((QueryImpl) query).getCollection().distinct("type");
    return this;
  }
}
