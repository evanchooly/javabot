package javabot.model.criteria;

import javabot.model.ApiEvent;

public class ApiEventCriteria extends com.antwerkz.critter.criteria.BaseCriteria<ApiEvent> {
  private String prefix = "";

  public ApiEventCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, ApiEvent.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId> apiId() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId>(this, query, prefix + "apiId");
  }

  public org.mongodb.morphia.query.Criteria apiId(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId>(this, query, prefix + "apiId").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.lang.String> baseUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.lang.String>(this, query, prefix + "baseUrl");
  }

  public org.mongodb.morphia.query.Criteria baseUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.lang.String>(this, query, prefix + "baseUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.joda.time.DateTime> completed() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.joda.time.DateTime>(this, query, prefix + "completed");
  }

  public org.mongodb.morphia.query.Criteria completed(org.joda.time.DateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.joda.time.DateTime>(this, query, prefix + "completed").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.lang.String> downloadUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.lang.String>(this, query, prefix + "downloadUrl");
  }

  public org.mongodb.morphia.query.Criteria downloadUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.lang.String>(this, query, prefix + "downloadUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.lang.String> name() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.lang.String>(this, query, prefix + "name");
  }

  public org.mongodb.morphia.query.Criteria name(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.lang.String>(this, query, prefix + "name").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.lang.String> requestedBy() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.lang.String>(this, query, prefix + "requestedBy");
  }

  public org.mongodb.morphia.query.Criteria requestedBy(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.lang.String>(this, query, prefix + "requestedBy").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.joda.time.DateTime> requestedOn() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.joda.time.DateTime>(this, query, prefix + "requestedOn");
  }

  public org.mongodb.morphia.query.Criteria requestedOn(org.joda.time.DateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.joda.time.DateTime>(this, query, prefix + "requestedOn").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.AdminEvent.State> state() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.AdminEvent.State>(this, query, prefix + "state");
  }

  public org.mongodb.morphia.query.Criteria state(javabot.model.AdminEvent.State value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.AdminEvent.State>(this, query, prefix + "state").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.EventType> type() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.EventType>(this, query, prefix + "type");
  }

  public org.mongodb.morphia.query.Criteria type(javabot.model.EventType value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.EventType>(this, query, prefix + "type").equal(value);
  }


  public ApiEventUpdater getUpdater() {
    return new ApiEventUpdater();
  }

  public class ApiEventUpdater {
    org.mongodb.morphia.query.UpdateOperations<ApiEvent> updateOperations;

    public ApiEventUpdater() {
      updateOperations = ds.createUpdateOperations(ApiEvent.class);
    }

    public org.mongodb.morphia.query.UpdateResults<ApiEvent> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<ApiEvent> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<ApiEvent> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<ApiEvent> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public ApiEventUpdater apiId(org.bson.types.ObjectId value) {
      updateOperations.set("apiId", value);
      return this;
    }

    public ApiEventUpdater unsetApiId(org.bson.types.ObjectId value) {
      updateOperations.unset("apiId");
      return this;
    }

    public ApiEventUpdater addApiId(org.bson.types.ObjectId value) {
      updateOperations.add("apiId", value);
      return this;
    }

    public ApiEventUpdater addApiId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("apiId", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToApiId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("apiId", values, addDups);
      return this;
    }
  
    public ApiEventUpdater removeFirstApiId() {
      updateOperations.removeFirst("apiId");
      return this;
    }
  
    public ApiEventUpdater removeLastApiId() {
      updateOperations.removeLast("apiId");
      return this;
    }
  
    public ApiEventUpdater removeFromApiId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("apiId", value);
      return this;
    }

    public ApiEventUpdater removeAllFromApiId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("apiId", values);
      return this;
    }
 
    public ApiEventUpdater decApiId() {
      updateOperations.dec("apiId");
      return this;
    }

    public ApiEventUpdater incApiId() {
      updateOperations.inc("apiId");
      return this;
    }

    public ApiEventUpdater incApiId(Number value) {
      updateOperations.inc("apiId", value);
      return this;
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

    public ApiEventUpdater addBaseUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("baseUrl", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToBaseUrl(java.util.List<java.lang.String> values, boolean addDups) {
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

    public ApiEventUpdater removeAllFromBaseUrl(java.util.List<java.lang.String> values) {
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

    public ApiEventUpdater addCompleted(org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("completed", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToCompleted(java.util.List<org.joda.time.DateTime> values, boolean addDups) {
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

    public ApiEventUpdater removeAllFromCompleted(java.util.List<org.joda.time.DateTime> values) {
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
    public ApiEventUpdater downloadUrl(java.lang.String value) {
      updateOperations.set("downloadUrl", value);
      return this;
    }

    public ApiEventUpdater unsetDownloadUrl(java.lang.String value) {
      updateOperations.unset("downloadUrl");
      return this;
    }

    public ApiEventUpdater addDownloadUrl(java.lang.String value) {
      updateOperations.add("downloadUrl", value);
      return this;
    }

    public ApiEventUpdater addDownloadUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("downloadUrl", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToDownloadUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("downloadUrl", values, addDups);
      return this;
    }
  
    public ApiEventUpdater removeFirstDownloadUrl() {
      updateOperations.removeFirst("downloadUrl");
      return this;
    }
  
    public ApiEventUpdater removeLastDownloadUrl() {
      updateOperations.removeLast("downloadUrl");
      return this;
    }
  
    public ApiEventUpdater removeFromDownloadUrl(java.lang.String value) {
      updateOperations.removeAll("downloadUrl", value);
      return this;
    }

    public ApiEventUpdater removeAllFromDownloadUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("downloadUrl", values);
      return this;
    }
 
    public ApiEventUpdater decDownloadUrl() {
      updateOperations.dec("downloadUrl");
      return this;
    }

    public ApiEventUpdater incDownloadUrl() {
      updateOperations.inc("downloadUrl");
      return this;
    }

    public ApiEventUpdater incDownloadUrl(Number value) {
      updateOperations.inc("downloadUrl", value);
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

    public ApiEventUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public ApiEventUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
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

    public ApiEventUpdater addName(java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToName(java.util.List<java.lang.String> values, boolean addDups) {
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

    public ApiEventUpdater removeAllFromName(java.util.List<java.lang.String> values) {
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

    public ApiEventUpdater addRequestedBy(java.lang.String value, boolean addDups) {
      updateOperations.add("requestedBy", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToRequestedBy(java.util.List<java.lang.String> values, boolean addDups) {
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

    public ApiEventUpdater removeAllFromRequestedBy(java.util.List<java.lang.String> values) {
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

    public ApiEventUpdater addRequestedOn(org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("requestedOn", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToRequestedOn(java.util.List<org.joda.time.DateTime> values, boolean addDups) {
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

    public ApiEventUpdater removeAllFromRequestedOn(java.util.List<org.joda.time.DateTime> values) {
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

    public ApiEventUpdater addState(javabot.model.AdminEvent.State value, boolean addDups) {
      updateOperations.add("state", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToState(java.util.List<javabot.model.AdminEvent.State> values, boolean addDups) {
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

    public ApiEventUpdater removeAllFromState(java.util.List<javabot.model.AdminEvent.State> values) {
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

    public ApiEventUpdater addType(javabot.model.EventType value, boolean addDups) {
      updateOperations.add("type", value, addDups);
      return this;
    }

    public ApiEventUpdater addAllToType(java.util.List<javabot.model.EventType> values, boolean addDups) {
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

    public ApiEventUpdater removeAllFromType(java.util.List<javabot.model.EventType> values) {
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
