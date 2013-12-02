package javabot.javadoc.criteria;

import javabot.javadoc.JavadocApi;

public class JavadocApiCriteria extends com.antwerkz.critter.criteria.BaseCriteria<JavadocApi> {
  private String prefix = "";

  public JavadocApiCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, JavadocApi.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, java.lang.String> baseUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, java.lang.String>(this, query, prefix + "baseUrl");
  }

  public org.mongodb.morphia.query.Criteria baseUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, java.lang.String>(this, query, prefix + "baseUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, java.lang.String> downloadUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, java.lang.String>(this, query, prefix + "downloadUrl");
  }

  public org.mongodb.morphia.query.Criteria downloadUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, java.lang.String>(this, query, prefix + "downloadUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, java.lang.String> name() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, java.lang.String>(this, query, prefix + "name");
  }

  public org.mongodb.morphia.query.Criteria name(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, java.lang.String>(this, query, prefix + "name").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, java.lang.String> upperName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, java.lang.String>(this, query, prefix + "upperName");
  }

  public org.mongodb.morphia.query.Criteria upperName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, java.lang.String>(this, query, prefix + "upperName").equal(value);
  }


  public JavadocApiUpdater getUpdater() {
    return new JavadocApiUpdater();
  }

  public class JavadocApiUpdater {
    org.mongodb.morphia.query.UpdateOperations<JavadocApi> updateOperations;

    public JavadocApiUpdater() {
      updateOperations = ds.createUpdateOperations(JavadocApi.class);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocApi> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocApi> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocApi> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocApi> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public JavadocApiUpdater baseUrl(java.lang.String value) {
      updateOperations.set("baseUrl", value);
      return this;
    }

    public JavadocApiUpdater unsetBaseUrl(java.lang.String value) {
      updateOperations.unset("baseUrl");
      return this;
    }

    public JavadocApiUpdater addBaseUrl(java.lang.String value) {
      updateOperations.add("baseUrl", value);
      return this;
    }

    public JavadocApiUpdater addBaseUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("baseUrl", value, addDups);
      return this;
    }

    public JavadocApiUpdater addAllToBaseUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("baseUrl", values, addDups);
      return this;
    }
  
    public JavadocApiUpdater removeFirstBaseUrl() {
      updateOperations.removeFirst("baseUrl");
      return this;
    }
  
    public JavadocApiUpdater removeLastBaseUrl() {
      updateOperations.removeLast("baseUrl");
      return this;
    }
  
    public JavadocApiUpdater removeFromBaseUrl(java.lang.String value) {
      updateOperations.removeAll("baseUrl", value);
      return this;
    }

    public JavadocApiUpdater removeAllFromBaseUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("baseUrl", values);
      return this;
    }
 
    public JavadocApiUpdater decBaseUrl() {
      updateOperations.dec("baseUrl");
      return this;
    }

    public JavadocApiUpdater incBaseUrl() {
      updateOperations.inc("baseUrl");
      return this;
    }

    public JavadocApiUpdater incBaseUrl(Number value) {
      updateOperations.inc("baseUrl", value);
      return this;
    }
    public JavadocApiUpdater downloadUrl(java.lang.String value) {
      updateOperations.set("downloadUrl", value);
      return this;
    }

    public JavadocApiUpdater unsetDownloadUrl(java.lang.String value) {
      updateOperations.unset("downloadUrl");
      return this;
    }

    public JavadocApiUpdater addDownloadUrl(java.lang.String value) {
      updateOperations.add("downloadUrl", value);
      return this;
    }

    public JavadocApiUpdater addDownloadUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("downloadUrl", value, addDups);
      return this;
    }

    public JavadocApiUpdater addAllToDownloadUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("downloadUrl", values, addDups);
      return this;
    }
  
    public JavadocApiUpdater removeFirstDownloadUrl() {
      updateOperations.removeFirst("downloadUrl");
      return this;
    }
  
    public JavadocApiUpdater removeLastDownloadUrl() {
      updateOperations.removeLast("downloadUrl");
      return this;
    }
  
    public JavadocApiUpdater removeFromDownloadUrl(java.lang.String value) {
      updateOperations.removeAll("downloadUrl", value);
      return this;
    }

    public JavadocApiUpdater removeAllFromDownloadUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("downloadUrl", values);
      return this;
    }
 
    public JavadocApiUpdater decDownloadUrl() {
      updateOperations.dec("downloadUrl");
      return this;
    }

    public JavadocApiUpdater incDownloadUrl() {
      updateOperations.inc("downloadUrl");
      return this;
    }

    public JavadocApiUpdater incDownloadUrl(Number value) {
      updateOperations.inc("downloadUrl", value);
      return this;
    }
    public JavadocApiUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public JavadocApiUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public JavadocApiUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public JavadocApiUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public JavadocApiUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public JavadocApiUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public JavadocApiUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public JavadocApiUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public JavadocApiUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public JavadocApiUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public JavadocApiUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public JavadocApiUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public JavadocApiUpdater name(java.lang.String value) {
      updateOperations.set("name", value);
      return this;
    }

    public JavadocApiUpdater unsetName(java.lang.String value) {
      updateOperations.unset("name");
      return this;
    }

    public JavadocApiUpdater addName(java.lang.String value) {
      updateOperations.add("name", value);
      return this;
    }

    public JavadocApiUpdater addName(java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public JavadocApiUpdater addAllToName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("name", values, addDups);
      return this;
    }
  
    public JavadocApiUpdater removeFirstName() {
      updateOperations.removeFirst("name");
      return this;
    }
  
    public JavadocApiUpdater removeLastName() {
      updateOperations.removeLast("name");
      return this;
    }
  
    public JavadocApiUpdater removeFromName(java.lang.String value) {
      updateOperations.removeAll("name", value);
      return this;
    }

    public JavadocApiUpdater removeAllFromName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("name", values);
      return this;
    }
 
    public JavadocApiUpdater decName() {
      updateOperations.dec("name");
      return this;
    }

    public JavadocApiUpdater incName() {
      updateOperations.inc("name");
      return this;
    }

    public JavadocApiUpdater incName(Number value) {
      updateOperations.inc("name", value);
      return this;
    }
    public JavadocApiUpdater upperName(java.lang.String value) {
      updateOperations.set("upperName", value);
      return this;
    }

    public JavadocApiUpdater unsetUpperName(java.lang.String value) {
      updateOperations.unset("upperName");
      return this;
    }

    public JavadocApiUpdater addUpperName(java.lang.String value) {
      updateOperations.add("upperName", value);
      return this;
    }

    public JavadocApiUpdater addUpperName(java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public JavadocApiUpdater addAllToUpperName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperName", values, addDups);
      return this;
    }
  
    public JavadocApiUpdater removeFirstUpperName() {
      updateOperations.removeFirst("upperName");
      return this;
    }
  
    public JavadocApiUpdater removeLastUpperName() {
      updateOperations.removeLast("upperName");
      return this;
    }
  
    public JavadocApiUpdater removeFromUpperName(java.lang.String value) {
      updateOperations.removeAll("upperName", value);
      return this;
    }

    public JavadocApiUpdater removeAllFromUpperName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("upperName", values);
      return this;
    }
 
    public JavadocApiUpdater decUpperName() {
      updateOperations.dec("upperName");
      return this;
    }

    public JavadocApiUpdater incUpperName() {
      updateOperations.inc("upperName");
      return this;
    }

    public JavadocApiUpdater incUpperName(Number value) {
      updateOperations.inc("upperName", value);
      return this;
    }
  }
}
