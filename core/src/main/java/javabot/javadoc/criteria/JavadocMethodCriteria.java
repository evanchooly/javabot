package javabot.javadoc.criteria;

import javabot.javadoc.JavadocMethod;

public class JavadocMethodCriteria extends com.antwerkz.critter.criteria.BaseCriteria<JavadocMethod> {
  private String prefix = "";

  public JavadocMethodCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, JavadocMethod.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId> apiId() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(this, query, prefix + "apiId");
  }

  public org.mongodb.morphia.query.Criteria apiId(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(this, query, prefix + "apiId").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String> directUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "directUrl");
  }

  public org.mongodb.morphia.query.Criteria directUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "directUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId> javadocClassId() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(this, query, prefix + "javadocClassId");
  }

  public org.mongodb.morphia.query.Criteria javadocClassId(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(this, query, prefix + "javadocClassId").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String> longSignatureTypes() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "longSignatureTypes");
  }

  public org.mongodb.morphia.query.Criteria longSignatureTypes(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "longSignatureTypes").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String> longUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "longUrl");
  }

  public org.mongodb.morphia.query.Criteria longUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "longUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String> name() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "name");
  }

  public org.mongodb.morphia.query.Criteria name(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "name").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.Integer> paramCount() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.Integer>(this, query, prefix + "paramCount");
  }

  public org.mongodb.morphia.query.Criteria paramCount(java.lang.Integer value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.Integer>(this, query, prefix + "paramCount").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String> parentClassName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "parentClassName");
  }

  public org.mongodb.morphia.query.Criteria parentClassName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "parentClassName").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String> shortSignatureTypes() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "shortSignatureTypes");
  }

  public org.mongodb.morphia.query.Criteria shortSignatureTypes(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "shortSignatureTypes").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String> shortUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "shortUrl");
  }

  public org.mongodb.morphia.query.Criteria shortUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "shortUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String> upperName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "upperName");
  }

  public org.mongodb.morphia.query.Criteria upperName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, java.lang.String>(this, query, prefix + "upperName").equal(value);
  }


  public JavadocMethodUpdater getUpdater() {
    return new JavadocMethodUpdater();
  }

  public class JavadocMethodUpdater {
    org.mongodb.morphia.query.UpdateOperations<JavadocMethod> updateOperations;

    public JavadocMethodUpdater() {
      updateOperations = ds.createUpdateOperations(JavadocMethod.class);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocMethod> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocMethod> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocMethod> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocMethod> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public JavadocMethodUpdater apiId(org.bson.types.ObjectId value) {
      updateOperations.set("apiId", value);
      return this;
    }

    public JavadocMethodUpdater unsetApiId(org.bson.types.ObjectId value) {
      updateOperations.unset("apiId");
      return this;
    }

    public JavadocMethodUpdater addApiId(org.bson.types.ObjectId value) {
      updateOperations.add("apiId", value);
      return this;
    }

    public JavadocMethodUpdater addApiId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("apiId", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToApiId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("apiId", values, addDups);
      return this;
    }
  
    public JavadocMethodUpdater removeFirstApiId() {
      updateOperations.removeFirst("apiId");
      return this;
    }
  
    public JavadocMethodUpdater removeLastApiId() {
      updateOperations.removeLast("apiId");
      return this;
    }
  
    public JavadocMethodUpdater removeFromApiId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("apiId", value);
      return this;
    }

    public JavadocMethodUpdater removeAllFromApiId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("apiId", values);
      return this;
    }
 
    public JavadocMethodUpdater decApiId() {
      updateOperations.dec("apiId");
      return this;
    }

    public JavadocMethodUpdater incApiId() {
      updateOperations.inc("apiId");
      return this;
    }

    public JavadocMethodUpdater incApiId(Number value) {
      updateOperations.inc("apiId", value);
      return this;
    }
    public JavadocMethodUpdater directUrl(java.lang.String value) {
      updateOperations.set("directUrl", value);
      return this;
    }

    public JavadocMethodUpdater unsetDirectUrl(java.lang.String value) {
      updateOperations.unset("directUrl");
      return this;
    }

    public JavadocMethodUpdater addDirectUrl(java.lang.String value) {
      updateOperations.add("directUrl", value);
      return this;
    }

    public JavadocMethodUpdater addDirectUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("directUrl", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToDirectUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("directUrl", values, addDups);
      return this;
    }
  
    public JavadocMethodUpdater removeFirstDirectUrl() {
      updateOperations.removeFirst("directUrl");
      return this;
    }
  
    public JavadocMethodUpdater removeLastDirectUrl() {
      updateOperations.removeLast("directUrl");
      return this;
    }
  
    public JavadocMethodUpdater removeFromDirectUrl(java.lang.String value) {
      updateOperations.removeAll("directUrl", value);
      return this;
    }

    public JavadocMethodUpdater removeAllFromDirectUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("directUrl", values);
      return this;
    }
 
    public JavadocMethodUpdater decDirectUrl() {
      updateOperations.dec("directUrl");
      return this;
    }

    public JavadocMethodUpdater incDirectUrl() {
      updateOperations.inc("directUrl");
      return this;
    }

    public JavadocMethodUpdater incDirectUrl(Number value) {
      updateOperations.inc("directUrl", value);
      return this;
    }
    public JavadocMethodUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public JavadocMethodUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public JavadocMethodUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public JavadocMethodUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public JavadocMethodUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public JavadocMethodUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public JavadocMethodUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public JavadocMethodUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public JavadocMethodUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public JavadocMethodUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public JavadocMethodUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public JavadocMethodUpdater javadocClassId(org.bson.types.ObjectId value) {
      updateOperations.set("javadocClassId", value);
      return this;
    }

    public JavadocMethodUpdater unsetJavadocClassId(org.bson.types.ObjectId value) {
      updateOperations.unset("javadocClassId");
      return this;
    }

    public JavadocMethodUpdater addJavadocClassId(org.bson.types.ObjectId value) {
      updateOperations.add("javadocClassId", value);
      return this;
    }

    public JavadocMethodUpdater addJavadocClassId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("javadocClassId", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToJavadocClassId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("javadocClassId", values, addDups);
      return this;
    }
  
    public JavadocMethodUpdater removeFirstJavadocClassId() {
      updateOperations.removeFirst("javadocClassId");
      return this;
    }
  
    public JavadocMethodUpdater removeLastJavadocClassId() {
      updateOperations.removeLast("javadocClassId");
      return this;
    }
  
    public JavadocMethodUpdater removeFromJavadocClassId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("javadocClassId", value);
      return this;
    }

    public JavadocMethodUpdater removeAllFromJavadocClassId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("javadocClassId", values);
      return this;
    }
 
    public JavadocMethodUpdater decJavadocClassId() {
      updateOperations.dec("javadocClassId");
      return this;
    }

    public JavadocMethodUpdater incJavadocClassId() {
      updateOperations.inc("javadocClassId");
      return this;
    }

    public JavadocMethodUpdater incJavadocClassId(Number value) {
      updateOperations.inc("javadocClassId", value);
      return this;
    }
    public JavadocMethodUpdater longSignatureTypes(java.lang.String value) {
      updateOperations.set("longSignatureTypes", value);
      return this;
    }

    public JavadocMethodUpdater unsetLongSignatureTypes(java.lang.String value) {
      updateOperations.unset("longSignatureTypes");
      return this;
    }

    public JavadocMethodUpdater addLongSignatureTypes(java.lang.String value) {
      updateOperations.add("longSignatureTypes", value);
      return this;
    }

    public JavadocMethodUpdater addLongSignatureTypes(java.lang.String value, boolean addDups) {
      updateOperations.add("longSignatureTypes", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToLongSignatureTypes(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("longSignatureTypes", values, addDups);
      return this;
    }
  
    public JavadocMethodUpdater removeFirstLongSignatureTypes() {
      updateOperations.removeFirst("longSignatureTypes");
      return this;
    }
  
    public JavadocMethodUpdater removeLastLongSignatureTypes() {
      updateOperations.removeLast("longSignatureTypes");
      return this;
    }
  
    public JavadocMethodUpdater removeFromLongSignatureTypes(java.lang.String value) {
      updateOperations.removeAll("longSignatureTypes", value);
      return this;
    }

    public JavadocMethodUpdater removeAllFromLongSignatureTypes(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("longSignatureTypes", values);
      return this;
    }
 
    public JavadocMethodUpdater decLongSignatureTypes() {
      updateOperations.dec("longSignatureTypes");
      return this;
    }

    public JavadocMethodUpdater incLongSignatureTypes() {
      updateOperations.inc("longSignatureTypes");
      return this;
    }

    public JavadocMethodUpdater incLongSignatureTypes(Number value) {
      updateOperations.inc("longSignatureTypes", value);
      return this;
    }
    public JavadocMethodUpdater longUrl(java.lang.String value) {
      updateOperations.set("longUrl", value);
      return this;
    }

    public JavadocMethodUpdater unsetLongUrl(java.lang.String value) {
      updateOperations.unset("longUrl");
      return this;
    }

    public JavadocMethodUpdater addLongUrl(java.lang.String value) {
      updateOperations.add("longUrl", value);
      return this;
    }

    public JavadocMethodUpdater addLongUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("longUrl", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToLongUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("longUrl", values, addDups);
      return this;
    }
  
    public JavadocMethodUpdater removeFirstLongUrl() {
      updateOperations.removeFirst("longUrl");
      return this;
    }
  
    public JavadocMethodUpdater removeLastLongUrl() {
      updateOperations.removeLast("longUrl");
      return this;
    }
  
    public JavadocMethodUpdater removeFromLongUrl(java.lang.String value) {
      updateOperations.removeAll("longUrl", value);
      return this;
    }

    public JavadocMethodUpdater removeAllFromLongUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("longUrl", values);
      return this;
    }
 
    public JavadocMethodUpdater decLongUrl() {
      updateOperations.dec("longUrl");
      return this;
    }

    public JavadocMethodUpdater incLongUrl() {
      updateOperations.inc("longUrl");
      return this;
    }

    public JavadocMethodUpdater incLongUrl(Number value) {
      updateOperations.inc("longUrl", value);
      return this;
    }
    public JavadocMethodUpdater name(java.lang.String value) {
      updateOperations.set("name", value);
      return this;
    }

    public JavadocMethodUpdater unsetName(java.lang.String value) {
      updateOperations.unset("name");
      return this;
    }

    public JavadocMethodUpdater addName(java.lang.String value) {
      updateOperations.add("name", value);
      return this;
    }

    public JavadocMethodUpdater addName(java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("name", values, addDups);
      return this;
    }
  
    public JavadocMethodUpdater removeFirstName() {
      updateOperations.removeFirst("name");
      return this;
    }
  
    public JavadocMethodUpdater removeLastName() {
      updateOperations.removeLast("name");
      return this;
    }
  
    public JavadocMethodUpdater removeFromName(java.lang.String value) {
      updateOperations.removeAll("name", value);
      return this;
    }

    public JavadocMethodUpdater removeAllFromName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("name", values);
      return this;
    }
 
    public JavadocMethodUpdater decName() {
      updateOperations.dec("name");
      return this;
    }

    public JavadocMethodUpdater incName() {
      updateOperations.inc("name");
      return this;
    }

    public JavadocMethodUpdater incName(Number value) {
      updateOperations.inc("name", value);
      return this;
    }
    public JavadocMethodUpdater paramCount(java.lang.Integer value) {
      updateOperations.set("paramCount", value);
      return this;
    }

    public JavadocMethodUpdater unsetParamCount(java.lang.Integer value) {
      updateOperations.unset("paramCount");
      return this;
    }

    public JavadocMethodUpdater addParamCount(java.lang.Integer value) {
      updateOperations.add("paramCount", value);
      return this;
    }

    public JavadocMethodUpdater addParamCount(java.lang.Integer value, boolean addDups) {
      updateOperations.add("paramCount", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToParamCount(java.util.List<java.lang.Integer> values, boolean addDups) {
      updateOperations.addAll("paramCount", values, addDups);
      return this;
    }
  
    public JavadocMethodUpdater removeFirstParamCount() {
      updateOperations.removeFirst("paramCount");
      return this;
    }
  
    public JavadocMethodUpdater removeLastParamCount() {
      updateOperations.removeLast("paramCount");
      return this;
    }
  
    public JavadocMethodUpdater removeFromParamCount(java.lang.Integer value) {
      updateOperations.removeAll("paramCount", value);
      return this;
    }

    public JavadocMethodUpdater removeAllFromParamCount(java.util.List<java.lang.Integer> values) {
      updateOperations.removeAll("paramCount", values);
      return this;
    }
 
    public JavadocMethodUpdater decParamCount() {
      updateOperations.dec("paramCount");
      return this;
    }

    public JavadocMethodUpdater incParamCount() {
      updateOperations.inc("paramCount");
      return this;
    }

    public JavadocMethodUpdater incParamCount(Number value) {
      updateOperations.inc("paramCount", value);
      return this;
    }
    public JavadocMethodUpdater parentClassName(java.lang.String value) {
      updateOperations.set("parentClassName", value);
      return this;
    }

    public JavadocMethodUpdater unsetParentClassName(java.lang.String value) {
      updateOperations.unset("parentClassName");
      return this;
    }

    public JavadocMethodUpdater addParentClassName(java.lang.String value) {
      updateOperations.add("parentClassName", value);
      return this;
    }

    public JavadocMethodUpdater addParentClassName(java.lang.String value, boolean addDups) {
      updateOperations.add("parentClassName", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToParentClassName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("parentClassName", values, addDups);
      return this;
    }
  
    public JavadocMethodUpdater removeFirstParentClassName() {
      updateOperations.removeFirst("parentClassName");
      return this;
    }
  
    public JavadocMethodUpdater removeLastParentClassName() {
      updateOperations.removeLast("parentClassName");
      return this;
    }
  
    public JavadocMethodUpdater removeFromParentClassName(java.lang.String value) {
      updateOperations.removeAll("parentClassName", value);
      return this;
    }

    public JavadocMethodUpdater removeAllFromParentClassName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("parentClassName", values);
      return this;
    }
 
    public JavadocMethodUpdater decParentClassName() {
      updateOperations.dec("parentClassName");
      return this;
    }

    public JavadocMethodUpdater incParentClassName() {
      updateOperations.inc("parentClassName");
      return this;
    }

    public JavadocMethodUpdater incParentClassName(Number value) {
      updateOperations.inc("parentClassName", value);
      return this;
    }
    public JavadocMethodUpdater shortSignatureTypes(java.lang.String value) {
      updateOperations.set("shortSignatureTypes", value);
      return this;
    }

    public JavadocMethodUpdater unsetShortSignatureTypes(java.lang.String value) {
      updateOperations.unset("shortSignatureTypes");
      return this;
    }

    public JavadocMethodUpdater addShortSignatureTypes(java.lang.String value) {
      updateOperations.add("shortSignatureTypes", value);
      return this;
    }

    public JavadocMethodUpdater addShortSignatureTypes(java.lang.String value, boolean addDups) {
      updateOperations.add("shortSignatureTypes", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToShortSignatureTypes(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("shortSignatureTypes", values, addDups);
      return this;
    }
  
    public JavadocMethodUpdater removeFirstShortSignatureTypes() {
      updateOperations.removeFirst("shortSignatureTypes");
      return this;
    }
  
    public JavadocMethodUpdater removeLastShortSignatureTypes() {
      updateOperations.removeLast("shortSignatureTypes");
      return this;
    }
  
    public JavadocMethodUpdater removeFromShortSignatureTypes(java.lang.String value) {
      updateOperations.removeAll("shortSignatureTypes", value);
      return this;
    }

    public JavadocMethodUpdater removeAllFromShortSignatureTypes(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("shortSignatureTypes", values);
      return this;
    }
 
    public JavadocMethodUpdater decShortSignatureTypes() {
      updateOperations.dec("shortSignatureTypes");
      return this;
    }

    public JavadocMethodUpdater incShortSignatureTypes() {
      updateOperations.inc("shortSignatureTypes");
      return this;
    }

    public JavadocMethodUpdater incShortSignatureTypes(Number value) {
      updateOperations.inc("shortSignatureTypes", value);
      return this;
    }
    public JavadocMethodUpdater shortUrl(java.lang.String value) {
      updateOperations.set("shortUrl", value);
      return this;
    }

    public JavadocMethodUpdater unsetShortUrl(java.lang.String value) {
      updateOperations.unset("shortUrl");
      return this;
    }

    public JavadocMethodUpdater addShortUrl(java.lang.String value) {
      updateOperations.add("shortUrl", value);
      return this;
    }

    public JavadocMethodUpdater addShortUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("shortUrl", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToShortUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("shortUrl", values, addDups);
      return this;
    }
  
    public JavadocMethodUpdater removeFirstShortUrl() {
      updateOperations.removeFirst("shortUrl");
      return this;
    }
  
    public JavadocMethodUpdater removeLastShortUrl() {
      updateOperations.removeLast("shortUrl");
      return this;
    }
  
    public JavadocMethodUpdater removeFromShortUrl(java.lang.String value) {
      updateOperations.removeAll("shortUrl", value);
      return this;
    }

    public JavadocMethodUpdater removeAllFromShortUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("shortUrl", values);
      return this;
    }
 
    public JavadocMethodUpdater decShortUrl() {
      updateOperations.dec("shortUrl");
      return this;
    }

    public JavadocMethodUpdater incShortUrl() {
      updateOperations.inc("shortUrl");
      return this;
    }

    public JavadocMethodUpdater incShortUrl(Number value) {
      updateOperations.inc("shortUrl", value);
      return this;
    }
    public JavadocMethodUpdater upperName(java.lang.String value) {
      updateOperations.set("upperName", value);
      return this;
    }

    public JavadocMethodUpdater unsetUpperName(java.lang.String value) {
      updateOperations.unset("upperName");
      return this;
    }

    public JavadocMethodUpdater addUpperName(java.lang.String value) {
      updateOperations.add("upperName", value);
      return this;
    }

    public JavadocMethodUpdater addUpperName(java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToUpperName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperName", values, addDups);
      return this;
    }
  
    public JavadocMethodUpdater removeFirstUpperName() {
      updateOperations.removeFirst("upperName");
      return this;
    }
  
    public JavadocMethodUpdater removeLastUpperName() {
      updateOperations.removeLast("upperName");
      return this;
    }
  
    public JavadocMethodUpdater removeFromUpperName(java.lang.String value) {
      updateOperations.removeAll("upperName", value);
      return this;
    }

    public JavadocMethodUpdater removeAllFromUpperName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("upperName", values);
      return this;
    }
 
    public JavadocMethodUpdater decUpperName() {
      updateOperations.dec("upperName");
      return this;
    }

    public JavadocMethodUpdater incUpperName() {
      updateOperations.inc("upperName");
      return this;
    }

    public JavadocMethodUpdater incUpperName(Number value) {
      updateOperations.inc("upperName", value);
      return this;
    }
  }
}
