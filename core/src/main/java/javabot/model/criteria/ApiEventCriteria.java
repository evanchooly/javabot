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

  public ApiEventCriteria baseUrl(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("baseUrl")).equal(value);
    return this;
  }

  public ApiEventCriteria orderByBaseUrl() {
    return orderByBaseUrl(true);
  }

  public ApiEventCriteria orderByBaseUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "baseUrl");
    return this;
  }

  public ApiEventCriteria distinctBaseUrl() {
    ((QueryImpl) query).getCollection().distinct("baseUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, org.joda.time.DateTime> completed() {
    return new TypeSafeFieldEnd<>(query, query.criteria("completed"));
  }

  public ApiEventCriteria completed(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("completed")).equal(value);
    return this;
  }

  public ApiEventCriteria orderByCompleted() {
    return orderByCompleted(true);
  }

  public ApiEventCriteria orderByCompleted(boolean ascending) {
    query.order((!ascending ? "-" : "") + "completed");
    return this;
  }

  public ApiEventCriteria distinctCompleted() {
    ((QueryImpl) query).getCollection().distinct("completed");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.lang.String> file() {
    return new TypeSafeFieldEnd<>(query, query.criteria("file"));
  }

  public ApiEventCriteria file(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("file")).equal(value);
    return this;
  }

  public ApiEventCriteria orderByFile() {
    return orderByFile(true);
  }

  public ApiEventCriteria orderByFile(boolean ascending) {
    query.order((!ascending ? "-" : "") + "file");
    return this;
  }

  public ApiEventCriteria distinctFile() {
    ((QueryImpl) query).getCollection().distinct("file");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ApiEventCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public ApiEventCriteria orderById() {
    return orderById(true);
  }

  public ApiEventCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public ApiEventCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public ApiEventCriteria name(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("name")).equal(value);
    return this;
  }

  public ApiEventCriteria orderByName() {
    return orderByName(true);
  }

  public ApiEventCriteria orderByName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "name");
    return this;
  }

  public ApiEventCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.lang.Boolean> newApi() {
    return new TypeSafeFieldEnd<>(query, query.criteria("newApi"));
  }

  public ApiEventCriteria newApi(java.lang.Boolean value) {
    new TypeSafeFieldEnd<>(query, query.criteria("newApi")).equal(value);
    return this;
  }

  public ApiEventCriteria orderByNewApi() {
    return orderByNewApi(true);
  }

  public ApiEventCriteria orderByNewApi(boolean ascending) {
    query.order((!ascending ? "-" : "") + "newApi");
    return this;
  }

  public ApiEventCriteria distinctNewApi() {
    ((QueryImpl) query).getCollection().distinct("newApi");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.lang.String> packages() {
    return new TypeSafeFieldEnd<>(query, query.criteria("packages"));
  }

  public ApiEventCriteria packages(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("packages")).equal(value);
    return this;
  }

  public ApiEventCriteria orderByPackages() {
    return orderByPackages(true);
  }

  public ApiEventCriteria orderByPackages(boolean ascending) {
    query.order((!ascending ? "-" : "") + "packages");
    return this;
  }

  public ApiEventCriteria distinctPackages() {
    ((QueryImpl) query).getCollection().distinct("packages");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, java.lang.String> requestedBy() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedBy"));
  }

  public ApiEventCriteria requestedBy(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("requestedBy")).equal(value);
    return this;
  }

  public ApiEventCriteria orderByRequestedBy() {
    return orderByRequestedBy(true);
  }

  public ApiEventCriteria orderByRequestedBy(boolean ascending) {
    query.order((!ascending ? "-" : "") + "requestedBy");
    return this;
  }

  public ApiEventCriteria distinctRequestedBy() {
    ((QueryImpl) query).getCollection().distinct("requestedBy");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, org.joda.time.DateTime> requestedOn() {
    return new TypeSafeFieldEnd<>(query, query.criteria("requestedOn"));
  }

  public ApiEventCriteria requestedOn(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("requestedOn")).equal(value);
    return this;
  }

  public ApiEventCriteria orderByRequestedOn() {
    return orderByRequestedOn(true);
  }

  public ApiEventCriteria orderByRequestedOn(boolean ascending) {
    query.order((!ascending ? "-" : "") + "requestedOn");
    return this;
  }

  public ApiEventCriteria distinctRequestedOn() {
    ((QueryImpl) query).getCollection().distinct("requestedOn");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, javabot.model.AdminEvent.State> state() {
    return new TypeSafeFieldEnd<>(query, query.criteria("state"));
  }

  public ApiEventCriteria state(javabot.model.AdminEvent.State value) {
    new TypeSafeFieldEnd<>(query, query.criteria("state")).equal(value);
    return this;
  }

  public ApiEventCriteria orderByState() {
    return orderByState(true);
  }

  public ApiEventCriteria orderByState(boolean ascending) {
    query.order((!ascending ? "-" : "") + "state");
    return this;
  }

  public ApiEventCriteria distinctState() {
    ((QueryImpl) query).getCollection().distinct("state");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.ApiEvent, javabot.model.EventType> type() {
    return new TypeSafeFieldEnd<>(query, query.criteria("type"));
  }

  public ApiEventCriteria type(javabot.model.EventType value) {
    new TypeSafeFieldEnd<>(query, query.criteria("type")).equal(value);
    return this;
  }

  public ApiEventCriteria orderByType() {
    return orderByType(true);
  }

  public ApiEventCriteria orderByType(boolean ascending) {
    query.order((!ascending ? "-" : "") + "type");
    return this;
  }

  public ApiEventCriteria distinctType() {
    ((QueryImpl) query).getCollection().distinct("type");
    return this;
  }
}
