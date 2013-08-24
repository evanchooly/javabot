package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;
import com.google.code.morphia.query.UpdateOperations;
import com.google.code.morphia.query.UpdateResults;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import java.util.List;


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

  public WriteResult delete() {
     return ds.delete(query());
  }

  public WriteResult delete(WriteConcern wc) {
     return ds.delete(query(), wc);
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

  public ApiEventUpdater getUpdater() {
    return new ApiEventUpdater();
  }

  public class ApiEventUpdater {
    UpdateOperations<javabot.model.ApiEvent> updateOperations;

    public ApiEventUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.ApiEvent.class);
    }

    public UpdateResults<javabot.model.ApiEvent> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.ApiEvent> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.ApiEvent> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.ApiEvent> upsert(WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public ApiEventUpdater baseUrl(java.lang.String value) {
      updateOperations.set("baseUrl", value);
      return this;
    }

    public ApiEventUpdater unsetBaseUrl(java.lang.String value) {
      updateOperations.unset("baseUrl");
      return this;
    }

    public ApiEventUpdater addBaseUrl(java.lang.String value) {
      updateOperations.add("baseUrl", value);
      return this;
    }

    public ApiEventUpdater addBaseUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("baseUrl", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToBaseUrl(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("baseUrl", values, addDups);
      return this;
    }
  
    public ApiEventUpdater removeFirstBaseUrl() {
      updateOperations.removeFirst("baseUrl");
      return this;
    }
  
    public ApiEventUpdater removeLastBaseUrl() {
      updateOperations.removeLast("baseUrl");
      return this;
    }
  
    public ApiEventUpdater removeFromBaseUrl(java.lang.String value) {
      updateOperations.removeAll("baseUrl", value);
      return this;
    }

    public ApiEventUpdater removeAllFromBaseUrl(List<java.lang.String> values) {
      updateOperations.removeAll("baseUrl", values);
      return this;
    }
 
    public ApiEventUpdater decBaseUrl() {
      updateOperations.dec("baseUrl");
      return this;
    }

    public ApiEventUpdater incBaseUrl() {
      updateOperations.inc("baseUrl");
      return this;
    }

    public ApiEventUpdater incBaseUrl(Number value) {
      updateOperations.inc("baseUrl", value);
      return this;
    }
    public ApiEventUpdater completed(org.joda.time.DateTime value) {
      updateOperations.set("completed", value);
      return this;
    }

    public ApiEventUpdater unsetCompleted(org.joda.time.DateTime value) {
      updateOperations.unset("completed");
      return this;
    }

    public ApiEventUpdater addCompleted(org.joda.time.DateTime value) {
      updateOperations.add("completed", value);
      return this;
    }

    public ApiEventUpdater addCompleted(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("completed", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToCompleted(List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("completed", values, addDups);
      return this;
    }
  
    public ApiEventUpdater removeFirstCompleted() {
      updateOperations.removeFirst("completed");
      return this;
    }
  
    public ApiEventUpdater removeLastCompleted() {
      updateOperations.removeLast("completed");
      return this;
    }
  
    public ApiEventUpdater removeFromCompleted(org.joda.time.DateTime value) {
      updateOperations.removeAll("completed", value);
      return this;
    }

    public ApiEventUpdater removeAllFromCompleted(List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("completed", values);
      return this;
    }
 
    public ApiEventUpdater decCompleted() {
      updateOperations.dec("completed");
      return this;
    }

    public ApiEventUpdater incCompleted() {
      updateOperations.inc("completed");
      return this;
    }

    public ApiEventUpdater incCompleted(Number value) {
      updateOperations.inc("completed", value);
      return this;
    }
    public ApiEventUpdater file(java.lang.String value) {
      updateOperations.set("file", value);
      return this;
    }

    public ApiEventUpdater unsetFile(java.lang.String value) {
      updateOperations.unset("file");
      return this;
    }

    public ApiEventUpdater addFile(java.lang.String value) {
      updateOperations.add("file", value);
      return this;
    }

    public ApiEventUpdater addFile(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("file", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToFile(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("file", values, addDups);
      return this;
    }
  
    public ApiEventUpdater removeFirstFile() {
      updateOperations.removeFirst("file");
      return this;
    }
  
    public ApiEventUpdater removeLastFile() {
      updateOperations.removeLast("file");
      return this;
    }
  
    public ApiEventUpdater removeFromFile(java.lang.String value) {
      updateOperations.removeAll("file", value);
      return this;
    }

    public ApiEventUpdater removeAllFromFile(List<java.lang.String> values) {
      updateOperations.removeAll("file", values);
      return this;
    }
 
    public ApiEventUpdater decFile() {
      updateOperations.dec("file");
      return this;
    }

    public ApiEventUpdater incFile() {
      updateOperations.inc("file");
      return this;
    }

    public ApiEventUpdater incFile(Number value) {
      updateOperations.inc("file", value);
      return this;
    }
    public ApiEventUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public ApiEventUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public ApiEventUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public ApiEventUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public ApiEventUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public ApiEventUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public ApiEventUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public ApiEventUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public ApiEventUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public ApiEventUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public ApiEventUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public ApiEventUpdater name(java.lang.String value) {
      updateOperations.set("name", value);
      return this;
    }

    public ApiEventUpdater unsetName(java.lang.String value) {
      updateOperations.unset("name");
      return this;
    }

    public ApiEventUpdater addName(java.lang.String value) {
      updateOperations.add("name", value);
      return this;
    }

    public ApiEventUpdater addName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("name", values, addDups);
      return this;
    }
  
    public ApiEventUpdater removeFirstName() {
      updateOperations.removeFirst("name");
      return this;
    }
  
    public ApiEventUpdater removeLastName() {
      updateOperations.removeLast("name");
      return this;
    }
  
    public ApiEventUpdater removeFromName(java.lang.String value) {
      updateOperations.removeAll("name", value);
      return this;
    }

    public ApiEventUpdater removeAllFromName(List<java.lang.String> values) {
      updateOperations.removeAll("name", values);
      return this;
    }
 
    public ApiEventUpdater decName() {
      updateOperations.dec("name");
      return this;
    }

    public ApiEventUpdater incName() {
      updateOperations.inc("name");
      return this;
    }

    public ApiEventUpdater incName(Number value) {
      updateOperations.inc("name", value);
      return this;
    }
    public ApiEventUpdater newApi(java.lang.Boolean value) {
      updateOperations.set("newApi", value);
      return this;
    }

    public ApiEventUpdater unsetNewApi(java.lang.Boolean value) {
      updateOperations.unset("newApi");
      return this;
    }

    public ApiEventUpdater addNewApi(java.lang.Boolean value) {
      updateOperations.add("newApi", value);
      return this;
    }

    public ApiEventUpdater addNewApi(String fieldExpr, java.lang.Boolean value, boolean addDups) {
      updateOperations.add("newApi", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToNewApi(List<java.lang.Boolean> values, boolean addDups) {
      updateOperations.addAll("newApi", values, addDups);
      return this;
    }
  
    public ApiEventUpdater removeFirstNewApi() {
      updateOperations.removeFirst("newApi");
      return this;
    }
  
    public ApiEventUpdater removeLastNewApi() {
      updateOperations.removeLast("newApi");
      return this;
    }
  
    public ApiEventUpdater removeFromNewApi(java.lang.Boolean value) {
      updateOperations.removeAll("newApi", value);
      return this;
    }

    public ApiEventUpdater removeAllFromNewApi(List<java.lang.Boolean> values) {
      updateOperations.removeAll("newApi", values);
      return this;
    }
 
    public ApiEventUpdater decNewApi() {
      updateOperations.dec("newApi");
      return this;
    }

    public ApiEventUpdater incNewApi() {
      updateOperations.inc("newApi");
      return this;
    }

    public ApiEventUpdater incNewApi(Number value) {
      updateOperations.inc("newApi", value);
      return this;
    }
    public ApiEventUpdater requestedBy(java.lang.String value) {
      updateOperations.set("requestedBy", value);
      return this;
    }

    public ApiEventUpdater unsetRequestedBy(java.lang.String value) {
      updateOperations.unset("requestedBy");
      return this;
    }

    public ApiEventUpdater addRequestedBy(java.lang.String value) {
      updateOperations.add("requestedBy", value);
      return this;
    }

    public ApiEventUpdater addRequestedBy(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("requestedBy", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToRequestedBy(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("requestedBy", values, addDups);
      return this;
    }
  
    public ApiEventUpdater removeFirstRequestedBy() {
      updateOperations.removeFirst("requestedBy");
      return this;
    }
  
    public ApiEventUpdater removeLastRequestedBy() {
      updateOperations.removeLast("requestedBy");
      return this;
    }
  
    public ApiEventUpdater removeFromRequestedBy(java.lang.String value) {
      updateOperations.removeAll("requestedBy", value);
      return this;
    }

    public ApiEventUpdater removeAllFromRequestedBy(List<java.lang.String> values) {
      updateOperations.removeAll("requestedBy", values);
      return this;
    }
 
    public ApiEventUpdater decRequestedBy() {
      updateOperations.dec("requestedBy");
      return this;
    }

    public ApiEventUpdater incRequestedBy() {
      updateOperations.inc("requestedBy");
      return this;
    }

    public ApiEventUpdater incRequestedBy(Number value) {
      updateOperations.inc("requestedBy", value);
      return this;
    }
    public ApiEventUpdater requestedOn(org.joda.time.DateTime value) {
      updateOperations.set("requestedOn", value);
      return this;
    }

    public ApiEventUpdater unsetRequestedOn(org.joda.time.DateTime value) {
      updateOperations.unset("requestedOn");
      return this;
    }

    public ApiEventUpdater addRequestedOn(org.joda.time.DateTime value) {
      updateOperations.add("requestedOn", value);
      return this;
    }

    public ApiEventUpdater addRequestedOn(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("requestedOn", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToRequestedOn(List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("requestedOn", values, addDups);
      return this;
    }
  
    public ApiEventUpdater removeFirstRequestedOn() {
      updateOperations.removeFirst("requestedOn");
      return this;
    }
  
    public ApiEventUpdater removeLastRequestedOn() {
      updateOperations.removeLast("requestedOn");
      return this;
    }
  
    public ApiEventUpdater removeFromRequestedOn(org.joda.time.DateTime value) {
      updateOperations.removeAll("requestedOn", value);
      return this;
    }

    public ApiEventUpdater removeAllFromRequestedOn(List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("requestedOn", values);
      return this;
    }
 
    public ApiEventUpdater decRequestedOn() {
      updateOperations.dec("requestedOn");
      return this;
    }

    public ApiEventUpdater incRequestedOn() {
      updateOperations.inc("requestedOn");
      return this;
    }

    public ApiEventUpdater incRequestedOn(Number value) {
      updateOperations.inc("requestedOn", value);
      return this;
    }
    public ApiEventUpdater state(javabot.model.AdminEvent.State value) {
      updateOperations.set("state", value);
      return this;
    }

    public ApiEventUpdater unsetState(javabot.model.AdminEvent.State value) {
      updateOperations.unset("state");
      return this;
    }

    public ApiEventUpdater addState(javabot.model.AdminEvent.State value) {
      updateOperations.add("state", value);
      return this;
    }

    public ApiEventUpdater addState(String fieldExpr, javabot.model.AdminEvent.State value, boolean addDups) {
      updateOperations.add("state", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToState(List<javabot.model.AdminEvent.State> values, boolean addDups) {
      updateOperations.addAll("state", values, addDups);
      return this;
    }
  
    public ApiEventUpdater removeFirstState() {
      updateOperations.removeFirst("state");
      return this;
    }
  
    public ApiEventUpdater removeLastState() {
      updateOperations.removeLast("state");
      return this;
    }
  
    public ApiEventUpdater removeFromState(javabot.model.AdminEvent.State value) {
      updateOperations.removeAll("state", value);
      return this;
    }

    public ApiEventUpdater removeAllFromState(List<javabot.model.AdminEvent.State> values) {
      updateOperations.removeAll("state", values);
      return this;
    }
 
    public ApiEventUpdater decState() {
      updateOperations.dec("state");
      return this;
    }

    public ApiEventUpdater incState() {
      updateOperations.inc("state");
      return this;
    }

    public ApiEventUpdater incState(Number value) {
      updateOperations.inc("state", value);
      return this;
    }
    public ApiEventUpdater type(javabot.model.EventType value) {
      updateOperations.set("type", value);
      return this;
    }

    public ApiEventUpdater unsetType(javabot.model.EventType value) {
      updateOperations.unset("type");
      return this;
    }

    public ApiEventUpdater addType(javabot.model.EventType value) {
      updateOperations.add("type", value);
      return this;
    }

    public ApiEventUpdater addType(String fieldExpr, javabot.model.EventType value, boolean addDups) {
      updateOperations.add("type", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToType(List<javabot.model.EventType> values, boolean addDups) {
      updateOperations.addAll("type", values, addDups);
      return this;
    }
  
    public ApiEventUpdater removeFirstType() {
      updateOperations.removeFirst("type");
      return this;
    }
  
    public ApiEventUpdater removeLastType() {
      updateOperations.removeLast("type");
      return this;
    }
  
    public ApiEventUpdater removeFromType(javabot.model.EventType value) {
      updateOperations.removeAll("type", value);
      return this;
    }

    public ApiEventUpdater removeAllFromType(List<javabot.model.EventType> values) {
      updateOperations.removeAll("type", values);
      return this;
    }
 
    public ApiEventUpdater decType() {
      updateOperations.dec("type");
      return this;
    }

    public ApiEventUpdater incType() {
      updateOperations.inc("type");
      return this;
    }

    public ApiEventUpdater incType(Number value) {
      updateOperations.inc("type", value);
      return this;
    }
  }
}
