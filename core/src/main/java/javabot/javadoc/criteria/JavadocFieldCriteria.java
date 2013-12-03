package javabot.javadoc.criteria;

import javabot.javadoc.JavadocField;

public class JavadocFieldCriteria extends com.antwerkz.critter.criteria.BaseCriteria<JavadocField> {
  private String prefix = "";

  public JavadocFieldCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, JavadocField.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId> apiId() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(this, query, prefix + "apiId");
  }

  public org.mongodb.morphia.query.Criteria apiId(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(this, query, prefix + "apiId").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String> directUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "directUrl");
  }

  public org.mongodb.morphia.query.Criteria directUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "directUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId> javadocClassId() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(this, query, prefix + "javadocClassId");
  }

  public org.mongodb.morphia.query.Criteria javadocClassId(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(this, query, prefix + "javadocClassId").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String> longUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "longUrl");
  }

  public org.mongodb.morphia.query.Criteria longUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "longUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String> name() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "name");
  }

  public org.mongodb.morphia.query.Criteria name(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "name").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String> parentClassName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "parentClassName");
  }

  public org.mongodb.morphia.query.Criteria parentClassName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "parentClassName").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String> shortUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "shortUrl");
  }

  public org.mongodb.morphia.query.Criteria shortUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "shortUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String> type() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "type");
  }

  public org.mongodb.morphia.query.Criteria type(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "type").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String> upperName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "upperName");
  }

  public org.mongodb.morphia.query.Criteria upperName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, java.lang.String>(this, query, prefix + "upperName").equal(value);
  }


  public JavadocFieldUpdater getUpdater() {
    return new JavadocFieldUpdater();
  }

  public class JavadocFieldUpdater {
    org.mongodb.morphia.query.UpdateOperations<JavadocField> updateOperations;

    public JavadocFieldUpdater() {
      updateOperations = ds.createUpdateOperations(JavadocField.class);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocField> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocField> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocField> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocField> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public JavadocFieldUpdater apiId(org.bson.types.ObjectId value) {
      updateOperations.set("apiId", value);
      return this;
    }

    public JavadocFieldUpdater unsetApiId(org.bson.types.ObjectId value) {
      updateOperations.unset("apiId");
      return this;
    }

    public JavadocFieldUpdater addApiId(org.bson.types.ObjectId value) {
      updateOperations.add("apiId", value);
      return this;
    }

    public JavadocFieldUpdater addApiId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("apiId", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToApiId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("apiId", values, addDups);
      return this;
    }
  
    public JavadocFieldUpdater removeFirstApiId() {
      updateOperations.removeFirst("apiId");
      return this;
    }
  
    public JavadocFieldUpdater removeLastApiId() {
      updateOperations.removeLast("apiId");
      return this;
    }
  
    public JavadocFieldUpdater removeFromApiId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("apiId", value);
      return this;
    }

    public JavadocFieldUpdater removeAllFromApiId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("apiId", values);
      return this;
    }
 
    public JavadocFieldUpdater decApiId() {
      updateOperations.dec("apiId");
      return this;
    }

    public JavadocFieldUpdater incApiId() {
      updateOperations.inc("apiId");
      return this;
    }

    public JavadocFieldUpdater incApiId(Number value) {
      updateOperations.inc("apiId", value);
      return this;
    }
    public JavadocFieldUpdater directUrl(java.lang.String value) {
      updateOperations.set("directUrl", value);
      return this;
    }

    public JavadocFieldUpdater unsetDirectUrl(java.lang.String value) {
      updateOperations.unset("directUrl");
      return this;
    }

    public JavadocFieldUpdater addDirectUrl(java.lang.String value) {
      updateOperations.add("directUrl", value);
      return this;
    }

    public JavadocFieldUpdater addDirectUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("directUrl", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToDirectUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("directUrl", values, addDups);
      return this;
    }
  
    public JavadocFieldUpdater removeFirstDirectUrl() {
      updateOperations.removeFirst("directUrl");
      return this;
    }
  
    public JavadocFieldUpdater removeLastDirectUrl() {
      updateOperations.removeLast("directUrl");
      return this;
    }
  
    public JavadocFieldUpdater removeFromDirectUrl(java.lang.String value) {
      updateOperations.removeAll("directUrl", value);
      return this;
    }

    public JavadocFieldUpdater removeAllFromDirectUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("directUrl", values);
      return this;
    }
 
    public JavadocFieldUpdater decDirectUrl() {
      updateOperations.dec("directUrl");
      return this;
    }

    public JavadocFieldUpdater incDirectUrl() {
      updateOperations.inc("directUrl");
      return this;
    }

    public JavadocFieldUpdater incDirectUrl(Number value) {
      updateOperations.inc("directUrl", value);
      return this;
    }
    public JavadocFieldUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public JavadocFieldUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public JavadocFieldUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public JavadocFieldUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public JavadocFieldUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public JavadocFieldUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public JavadocFieldUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public JavadocFieldUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public JavadocFieldUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public JavadocFieldUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public JavadocFieldUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public JavadocFieldUpdater javadocClassId(org.bson.types.ObjectId value) {
      updateOperations.set("javadocClassId", value);
      return this;
    }

    public JavadocFieldUpdater unsetJavadocClassId(org.bson.types.ObjectId value) {
      updateOperations.unset("javadocClassId");
      return this;
    }

    public JavadocFieldUpdater addJavadocClassId(org.bson.types.ObjectId value) {
      updateOperations.add("javadocClassId", value);
      return this;
    }

    public JavadocFieldUpdater addJavadocClassId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("javadocClassId", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToJavadocClassId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("javadocClassId", values, addDups);
      return this;
    }
  
    public JavadocFieldUpdater removeFirstJavadocClassId() {
      updateOperations.removeFirst("javadocClassId");
      return this;
    }
  
    public JavadocFieldUpdater removeLastJavadocClassId() {
      updateOperations.removeLast("javadocClassId");
      return this;
    }
  
    public JavadocFieldUpdater removeFromJavadocClassId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("javadocClassId", value);
      return this;
    }

    public JavadocFieldUpdater removeAllFromJavadocClassId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("javadocClassId", values);
      return this;
    }
 
    public JavadocFieldUpdater decJavadocClassId() {
      updateOperations.dec("javadocClassId");
      return this;
    }

    public JavadocFieldUpdater incJavadocClassId() {
      updateOperations.inc("javadocClassId");
      return this;
    }

    public JavadocFieldUpdater incJavadocClassId(Number value) {
      updateOperations.inc("javadocClassId", value);
      return this;
    }
    public JavadocFieldUpdater longUrl(java.lang.String value) {
      updateOperations.set("longUrl", value);
      return this;
    }

    public JavadocFieldUpdater unsetLongUrl(java.lang.String value) {
      updateOperations.unset("longUrl");
      return this;
    }

    public JavadocFieldUpdater addLongUrl(java.lang.String value) {
      updateOperations.add("longUrl", value);
      return this;
    }

    public JavadocFieldUpdater addLongUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("longUrl", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToLongUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("longUrl", values, addDups);
      return this;
    }
  
    public JavadocFieldUpdater removeFirstLongUrl() {
      updateOperations.removeFirst("longUrl");
      return this;
    }
  
    public JavadocFieldUpdater removeLastLongUrl() {
      updateOperations.removeLast("longUrl");
      return this;
    }
  
    public JavadocFieldUpdater removeFromLongUrl(java.lang.String value) {
      updateOperations.removeAll("longUrl", value);
      return this;
    }

    public JavadocFieldUpdater removeAllFromLongUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("longUrl", values);
      return this;
    }
 
    public JavadocFieldUpdater decLongUrl() {
      updateOperations.dec("longUrl");
      return this;
    }

    public JavadocFieldUpdater incLongUrl() {
      updateOperations.inc("longUrl");
      return this;
    }

    public JavadocFieldUpdater incLongUrl(Number value) {
      updateOperations.inc("longUrl", value);
      return this;
    }
    public JavadocFieldUpdater name(java.lang.String value) {
      updateOperations.set("name", value);
      return this;
    }

    public JavadocFieldUpdater unsetName(java.lang.String value) {
      updateOperations.unset("name");
      return this;
    }

    public JavadocFieldUpdater addName(java.lang.String value) {
      updateOperations.add("name", value);
      return this;
    }

    public JavadocFieldUpdater addName(java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("name", values, addDups);
      return this;
    }
  
    public JavadocFieldUpdater removeFirstName() {
      updateOperations.removeFirst("name");
      return this;
    }
  
    public JavadocFieldUpdater removeLastName() {
      updateOperations.removeLast("name");
      return this;
    }
  
    public JavadocFieldUpdater removeFromName(java.lang.String value) {
      updateOperations.removeAll("name", value);
      return this;
    }

    public JavadocFieldUpdater removeAllFromName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("name", values);
      return this;
    }
 
    public JavadocFieldUpdater decName() {
      updateOperations.dec("name");
      return this;
    }

    public JavadocFieldUpdater incName() {
      updateOperations.inc("name");
      return this;
    }

    public JavadocFieldUpdater incName(Number value) {
      updateOperations.inc("name", value);
      return this;
    }
    public JavadocFieldUpdater parentClassName(java.lang.String value) {
      updateOperations.set("parentClassName", value);
      return this;
    }

    public JavadocFieldUpdater unsetParentClassName(java.lang.String value) {
      updateOperations.unset("parentClassName");
      return this;
    }

    public JavadocFieldUpdater addParentClassName(java.lang.String value) {
      updateOperations.add("parentClassName", value);
      return this;
    }

    public JavadocFieldUpdater addParentClassName(java.lang.String value, boolean addDups) {
      updateOperations.add("parentClassName", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToParentClassName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("parentClassName", values, addDups);
      return this;
    }
  
    public JavadocFieldUpdater removeFirstParentClassName() {
      updateOperations.removeFirst("parentClassName");
      return this;
    }
  
    public JavadocFieldUpdater removeLastParentClassName() {
      updateOperations.removeLast("parentClassName");
      return this;
    }
  
    public JavadocFieldUpdater removeFromParentClassName(java.lang.String value) {
      updateOperations.removeAll("parentClassName", value);
      return this;
    }

    public JavadocFieldUpdater removeAllFromParentClassName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("parentClassName", values);
      return this;
    }
 
    public JavadocFieldUpdater decParentClassName() {
      updateOperations.dec("parentClassName");
      return this;
    }

    public JavadocFieldUpdater incParentClassName() {
      updateOperations.inc("parentClassName");
      return this;
    }

    public JavadocFieldUpdater incParentClassName(Number value) {
      updateOperations.inc("parentClassName", value);
      return this;
    }
    public JavadocFieldUpdater shortUrl(java.lang.String value) {
      updateOperations.set("shortUrl", value);
      return this;
    }

    public JavadocFieldUpdater unsetShortUrl(java.lang.String value) {
      updateOperations.unset("shortUrl");
      return this;
    }

    public JavadocFieldUpdater addShortUrl(java.lang.String value) {
      updateOperations.add("shortUrl", value);
      return this;
    }

    public JavadocFieldUpdater addShortUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("shortUrl", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToShortUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("shortUrl", values, addDups);
      return this;
    }
  
    public JavadocFieldUpdater removeFirstShortUrl() {
      updateOperations.removeFirst("shortUrl");
      return this;
    }
  
    public JavadocFieldUpdater removeLastShortUrl() {
      updateOperations.removeLast("shortUrl");
      return this;
    }
  
    public JavadocFieldUpdater removeFromShortUrl(java.lang.String value) {
      updateOperations.removeAll("shortUrl", value);
      return this;
    }

    public JavadocFieldUpdater removeAllFromShortUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("shortUrl", values);
      return this;
    }
 
    public JavadocFieldUpdater decShortUrl() {
      updateOperations.dec("shortUrl");
      return this;
    }

    public JavadocFieldUpdater incShortUrl() {
      updateOperations.inc("shortUrl");
      return this;
    }

    public JavadocFieldUpdater incShortUrl(Number value) {
      updateOperations.inc("shortUrl", value);
      return this;
    }
    public JavadocFieldUpdater type(java.lang.String value) {
      updateOperations.set("type", value);
      return this;
    }

    public JavadocFieldUpdater unsetType(java.lang.String value) {
      updateOperations.unset("type");
      return this;
    }

    public JavadocFieldUpdater addType(java.lang.String value) {
      updateOperations.add("type", value);
      return this;
    }

    public JavadocFieldUpdater addType(java.lang.String value, boolean addDups) {
      updateOperations.add("type", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToType(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("type", values, addDups);
      return this;
    }
  
    public JavadocFieldUpdater removeFirstType() {
      updateOperations.removeFirst("type");
      return this;
    }
  
    public JavadocFieldUpdater removeLastType() {
      updateOperations.removeLast("type");
      return this;
    }
  
    public JavadocFieldUpdater removeFromType(java.lang.String value) {
      updateOperations.removeAll("type", value);
      return this;
    }

    public JavadocFieldUpdater removeAllFromType(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("type", values);
      return this;
    }
 
    public JavadocFieldUpdater decType() {
      updateOperations.dec("type");
      return this;
    }

    public JavadocFieldUpdater incType() {
      updateOperations.inc("type");
      return this;
    }

    public JavadocFieldUpdater incType(Number value) {
      updateOperations.inc("type", value);
      return this;
    }
    public JavadocFieldUpdater upperName(java.lang.String value) {
      updateOperations.set("upperName", value);
      return this;
    }

    public JavadocFieldUpdater unsetUpperName(java.lang.String value) {
      updateOperations.unset("upperName");
      return this;
    }

    public JavadocFieldUpdater addUpperName(java.lang.String value) {
      updateOperations.add("upperName", value);
      return this;
    }

    public JavadocFieldUpdater addUpperName(java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToUpperName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperName", values, addDups);
      return this;
    }
  
    public JavadocFieldUpdater removeFirstUpperName() {
      updateOperations.removeFirst("upperName");
      return this;
    }
  
    public JavadocFieldUpdater removeLastUpperName() {
      updateOperations.removeLast("upperName");
      return this;
    }
  
    public JavadocFieldUpdater removeFromUpperName(java.lang.String value) {
      updateOperations.removeAll("upperName", value);
      return this;
    }

    public JavadocFieldUpdater removeAllFromUpperName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("upperName", values);
      return this;
    }
 
    public JavadocFieldUpdater decUpperName() {
      updateOperations.dec("upperName");
      return this;
    }

    public JavadocFieldUpdater incUpperName() {
      updateOperations.inc("upperName");
      return this;
    }

    public JavadocFieldUpdater incUpperName(Number value) {
      updateOperations.inc("upperName", value);
      return this;
    }
  }
}
