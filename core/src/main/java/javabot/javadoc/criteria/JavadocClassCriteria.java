package javabot.javadoc.criteria;

import javabot.javadoc.JavadocClass;

public class JavadocClassCriteria extends com.antwerkz.critter.criteria.BaseCriteria<JavadocClass> {
  private String prefix = "";

  public JavadocClassCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, JavadocClass.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId> apiId() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(this, query, prefix + "apiId");
  }

  public org.mongodb.morphia.query.Criteria apiId(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(this, query, prefix + "apiId").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String> directUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "directUrl");
  }

  public org.mongodb.morphia.query.Criteria directUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "directUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.util.List<javabot.javadoc.JavadocField>> fields() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.util.List<javabot.javadoc.JavadocField>>(this, query, prefix + "fields");
  }

  public org.mongodb.morphia.query.Criteria fields(java.util.List<javabot.javadoc.JavadocField> value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.util.List<javabot.javadoc.JavadocField>>(this, query, prefix + "fields").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String> longUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "longUrl");
  }

  public org.mongodb.morphia.query.Criteria longUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "longUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.util.List<javabot.javadoc.JavadocMethod>> methods() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.util.List<javabot.javadoc.JavadocMethod>>(this, query, prefix + "methods");
  }

  public org.mongodb.morphia.query.Criteria methods(java.util.List<javabot.javadoc.JavadocMethod> value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.util.List<javabot.javadoc.JavadocMethod>>(this, query, prefix + "methods").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String> name() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "name");
  }

  public org.mongodb.morphia.query.Criteria name(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "name").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String> packageName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "packageName");
  }

  public org.mongodb.morphia.query.Criteria packageName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "packageName").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String> shortUrl() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "shortUrl");
  }

  public org.mongodb.morphia.query.Criteria shortUrl(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "shortUrl").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId> superClassId() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(this, query, prefix + "superClassId");
  }

  public org.mongodb.morphia.query.Criteria superClassId(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(this, query, prefix + "superClassId").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String> upperName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "upperName");
  }

  public org.mongodb.morphia.query.Criteria upperName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "upperName").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String> upperPackageName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "upperPackageName");
  }

  public org.mongodb.morphia.query.Criteria upperPackageName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, java.lang.String>(this, query, prefix + "upperPackageName").equal(value);
  }


  public JavadocClassUpdater getUpdater() {
    return new JavadocClassUpdater();
  }

  public class JavadocClassUpdater {
    org.mongodb.morphia.query.UpdateOperations<JavadocClass> updateOperations;

    public JavadocClassUpdater() {
      updateOperations = ds.createUpdateOperations(JavadocClass.class);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocClass> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocClass> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocClass> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<JavadocClass> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public JavadocClassUpdater apiId(org.bson.types.ObjectId value) {
      updateOperations.set("apiId", value);
      return this;
    }

    public JavadocClassUpdater unsetApiId(org.bson.types.ObjectId value) {
      updateOperations.unset("apiId");
      return this;
    }

    public JavadocClassUpdater addApiId(org.bson.types.ObjectId value) {
      updateOperations.add("apiId", value);
      return this;
    }

    public JavadocClassUpdater addApiId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("apiId", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToApiId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("apiId", values, addDups);
      return this;
    }
  
    public JavadocClassUpdater removeFirstApiId() {
      updateOperations.removeFirst("apiId");
      return this;
    }
  
    public JavadocClassUpdater removeLastApiId() {
      updateOperations.removeLast("apiId");
      return this;
    }
  
    public JavadocClassUpdater removeFromApiId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("apiId", value);
      return this;
    }

    public JavadocClassUpdater removeAllFromApiId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("apiId", values);
      return this;
    }
 
    public JavadocClassUpdater decApiId() {
      updateOperations.dec("apiId");
      return this;
    }

    public JavadocClassUpdater incApiId() {
      updateOperations.inc("apiId");
      return this;
    }

    public JavadocClassUpdater incApiId(Number value) {
      updateOperations.inc("apiId", value);
      return this;
    }
    public JavadocClassUpdater directUrl(java.lang.String value) {
      updateOperations.set("directUrl", value);
      return this;
    }

    public JavadocClassUpdater unsetDirectUrl(java.lang.String value) {
      updateOperations.unset("directUrl");
      return this;
    }

    public JavadocClassUpdater addDirectUrl(java.lang.String value) {
      updateOperations.add("directUrl", value);
      return this;
    }

    public JavadocClassUpdater addDirectUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("directUrl", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToDirectUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("directUrl", values, addDups);
      return this;
    }
  
    public JavadocClassUpdater removeFirstDirectUrl() {
      updateOperations.removeFirst("directUrl");
      return this;
    }
  
    public JavadocClassUpdater removeLastDirectUrl() {
      updateOperations.removeLast("directUrl");
      return this;
    }
  
    public JavadocClassUpdater removeFromDirectUrl(java.lang.String value) {
      updateOperations.removeAll("directUrl", value);
      return this;
    }

    public JavadocClassUpdater removeAllFromDirectUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("directUrl", values);
      return this;
    }
 
    public JavadocClassUpdater decDirectUrl() {
      updateOperations.dec("directUrl");
      return this;
    }

    public JavadocClassUpdater incDirectUrl() {
      updateOperations.inc("directUrl");
      return this;
    }

    public JavadocClassUpdater incDirectUrl(Number value) {
      updateOperations.inc("directUrl", value);
      return this;
    }
    public JavadocClassUpdater fields(java.util.List<javabot.javadoc.JavadocField> value) {
      updateOperations.set("fields", value);
      return this;
    }

    public JavadocClassUpdater unsetFields(java.util.List<javabot.javadoc.JavadocField> value) {
      updateOperations.unset("fields");
      return this;
    }

    public JavadocClassUpdater addFields(java.util.List<javabot.javadoc.JavadocField> value) {
      updateOperations.add("fields", value);
      return this;
    }

    public JavadocClassUpdater addFields(java.util.List<javabot.javadoc.JavadocField> value, boolean addDups) {
      updateOperations.add("fields", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToFields(java.util.List<java.util.List<javabot.javadoc.JavadocField>> values, boolean addDups) {
      updateOperations.addAll("fields", values, addDups);
      return this;
    }
  
    public JavadocClassUpdater removeFirstFields() {
      updateOperations.removeFirst("fields");
      return this;
    }
  
    public JavadocClassUpdater removeLastFields() {
      updateOperations.removeLast("fields");
      return this;
    }
  
    public JavadocClassUpdater removeFromFields(java.util.List<javabot.javadoc.JavadocField> value) {
      updateOperations.removeAll("fields", value);
      return this;
    }

    public JavadocClassUpdater removeAllFromFields(java.util.List<java.util.List<javabot.javadoc.JavadocField>> values) {
      updateOperations.removeAll("fields", values);
      return this;
    }
 
    public JavadocClassUpdater decFields() {
      updateOperations.dec("fields");
      return this;
    }

    public JavadocClassUpdater incFields() {
      updateOperations.inc("fields");
      return this;
    }

    public JavadocClassUpdater incFields(Number value) {
      updateOperations.inc("fields", value);
      return this;
    }
    public JavadocClassUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public JavadocClassUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public JavadocClassUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public JavadocClassUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public JavadocClassUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public JavadocClassUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public JavadocClassUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public JavadocClassUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public JavadocClassUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public JavadocClassUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public JavadocClassUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public JavadocClassUpdater longUrl(java.lang.String value) {
      updateOperations.set("longUrl", value);
      return this;
    }

    public JavadocClassUpdater unsetLongUrl(java.lang.String value) {
      updateOperations.unset("longUrl");
      return this;
    }

    public JavadocClassUpdater addLongUrl(java.lang.String value) {
      updateOperations.add("longUrl", value);
      return this;
    }

    public JavadocClassUpdater addLongUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("longUrl", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToLongUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("longUrl", values, addDups);
      return this;
    }
  
    public JavadocClassUpdater removeFirstLongUrl() {
      updateOperations.removeFirst("longUrl");
      return this;
    }
  
    public JavadocClassUpdater removeLastLongUrl() {
      updateOperations.removeLast("longUrl");
      return this;
    }
  
    public JavadocClassUpdater removeFromLongUrl(java.lang.String value) {
      updateOperations.removeAll("longUrl", value);
      return this;
    }

    public JavadocClassUpdater removeAllFromLongUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("longUrl", values);
      return this;
    }
 
    public JavadocClassUpdater decLongUrl() {
      updateOperations.dec("longUrl");
      return this;
    }

    public JavadocClassUpdater incLongUrl() {
      updateOperations.inc("longUrl");
      return this;
    }

    public JavadocClassUpdater incLongUrl(Number value) {
      updateOperations.inc("longUrl", value);
      return this;
    }
    public JavadocClassUpdater methods(java.util.List<javabot.javadoc.JavadocMethod> value) {
      updateOperations.set("methods", value);
      return this;
    }

    public JavadocClassUpdater unsetMethods(java.util.List<javabot.javadoc.JavadocMethod> value) {
      updateOperations.unset("methods");
      return this;
    }

    public JavadocClassUpdater addMethods(java.util.List<javabot.javadoc.JavadocMethod> value) {
      updateOperations.add("methods", value);
      return this;
    }

    public JavadocClassUpdater addMethods(java.util.List<javabot.javadoc.JavadocMethod> value, boolean addDups) {
      updateOperations.add("methods", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToMethods(java.util.List<java.util.List<javabot.javadoc.JavadocMethod>> values, boolean addDups) {
      updateOperations.addAll("methods", values, addDups);
      return this;
    }
  
    public JavadocClassUpdater removeFirstMethods() {
      updateOperations.removeFirst("methods");
      return this;
    }
  
    public JavadocClassUpdater removeLastMethods() {
      updateOperations.removeLast("methods");
      return this;
    }
  
    public JavadocClassUpdater removeFromMethods(java.util.List<javabot.javadoc.JavadocMethod> value) {
      updateOperations.removeAll("methods", value);
      return this;
    }

    public JavadocClassUpdater removeAllFromMethods(java.util.List<java.util.List<javabot.javadoc.JavadocMethod>> values) {
      updateOperations.removeAll("methods", values);
      return this;
    }
 
    public JavadocClassUpdater decMethods() {
      updateOperations.dec("methods");
      return this;
    }

    public JavadocClassUpdater incMethods() {
      updateOperations.inc("methods");
      return this;
    }

    public JavadocClassUpdater incMethods(Number value) {
      updateOperations.inc("methods", value);
      return this;
    }
    public JavadocClassUpdater name(java.lang.String value) {
      updateOperations.set("name", value);
      return this;
    }

    public JavadocClassUpdater unsetName(java.lang.String value) {
      updateOperations.unset("name");
      return this;
    }

    public JavadocClassUpdater addName(java.lang.String value) {
      updateOperations.add("name", value);
      return this;
    }

    public JavadocClassUpdater addName(java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("name", values, addDups);
      return this;
    }
  
    public JavadocClassUpdater removeFirstName() {
      updateOperations.removeFirst("name");
      return this;
    }
  
    public JavadocClassUpdater removeLastName() {
      updateOperations.removeLast("name");
      return this;
    }
  
    public JavadocClassUpdater removeFromName(java.lang.String value) {
      updateOperations.removeAll("name", value);
      return this;
    }

    public JavadocClassUpdater removeAllFromName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("name", values);
      return this;
    }
 
    public JavadocClassUpdater decName() {
      updateOperations.dec("name");
      return this;
    }

    public JavadocClassUpdater incName() {
      updateOperations.inc("name");
      return this;
    }

    public JavadocClassUpdater incName(Number value) {
      updateOperations.inc("name", value);
      return this;
    }
    public JavadocClassUpdater packageName(java.lang.String value) {
      updateOperations.set("packageName", value);
      return this;
    }

    public JavadocClassUpdater unsetPackageName(java.lang.String value) {
      updateOperations.unset("packageName");
      return this;
    }

    public JavadocClassUpdater addPackageName(java.lang.String value) {
      updateOperations.add("packageName", value);
      return this;
    }

    public JavadocClassUpdater addPackageName(java.lang.String value, boolean addDups) {
      updateOperations.add("packageName", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToPackageName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("packageName", values, addDups);
      return this;
    }
  
    public JavadocClassUpdater removeFirstPackageName() {
      updateOperations.removeFirst("packageName");
      return this;
    }
  
    public JavadocClassUpdater removeLastPackageName() {
      updateOperations.removeLast("packageName");
      return this;
    }
  
    public JavadocClassUpdater removeFromPackageName(java.lang.String value) {
      updateOperations.removeAll("packageName", value);
      return this;
    }

    public JavadocClassUpdater removeAllFromPackageName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("packageName", values);
      return this;
    }
 
    public JavadocClassUpdater decPackageName() {
      updateOperations.dec("packageName");
      return this;
    }

    public JavadocClassUpdater incPackageName() {
      updateOperations.inc("packageName");
      return this;
    }

    public JavadocClassUpdater incPackageName(Number value) {
      updateOperations.inc("packageName", value);
      return this;
    }
    public JavadocClassUpdater shortUrl(java.lang.String value) {
      updateOperations.set("shortUrl", value);
      return this;
    }

    public JavadocClassUpdater unsetShortUrl(java.lang.String value) {
      updateOperations.unset("shortUrl");
      return this;
    }

    public JavadocClassUpdater addShortUrl(java.lang.String value) {
      updateOperations.add("shortUrl", value);
      return this;
    }

    public JavadocClassUpdater addShortUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("shortUrl", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToShortUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("shortUrl", values, addDups);
      return this;
    }
  
    public JavadocClassUpdater removeFirstShortUrl() {
      updateOperations.removeFirst("shortUrl");
      return this;
    }
  
    public JavadocClassUpdater removeLastShortUrl() {
      updateOperations.removeLast("shortUrl");
      return this;
    }
  
    public JavadocClassUpdater removeFromShortUrl(java.lang.String value) {
      updateOperations.removeAll("shortUrl", value);
      return this;
    }

    public JavadocClassUpdater removeAllFromShortUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("shortUrl", values);
      return this;
    }
 
    public JavadocClassUpdater decShortUrl() {
      updateOperations.dec("shortUrl");
      return this;
    }

    public JavadocClassUpdater incShortUrl() {
      updateOperations.inc("shortUrl");
      return this;
    }

    public JavadocClassUpdater incShortUrl(Number value) {
      updateOperations.inc("shortUrl", value);
      return this;
    }
    public JavadocClassUpdater superClassId(org.bson.types.ObjectId value) {
      updateOperations.set("superClassId", value);
      return this;
    }

    public JavadocClassUpdater unsetSuperClassId(org.bson.types.ObjectId value) {
      updateOperations.unset("superClassId");
      return this;
    }

    public JavadocClassUpdater addSuperClassId(org.bson.types.ObjectId value) {
      updateOperations.add("superClassId", value);
      return this;
    }

    public JavadocClassUpdater addSuperClassId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("superClassId", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToSuperClassId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("superClassId", values, addDups);
      return this;
    }
  
    public JavadocClassUpdater removeFirstSuperClassId() {
      updateOperations.removeFirst("superClassId");
      return this;
    }
  
    public JavadocClassUpdater removeLastSuperClassId() {
      updateOperations.removeLast("superClassId");
      return this;
    }
  
    public JavadocClassUpdater removeFromSuperClassId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("superClassId", value);
      return this;
    }

    public JavadocClassUpdater removeAllFromSuperClassId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("superClassId", values);
      return this;
    }
 
    public JavadocClassUpdater decSuperClassId() {
      updateOperations.dec("superClassId");
      return this;
    }

    public JavadocClassUpdater incSuperClassId() {
      updateOperations.inc("superClassId");
      return this;
    }

    public JavadocClassUpdater incSuperClassId(Number value) {
      updateOperations.inc("superClassId", value);
      return this;
    }
    public JavadocClassUpdater upperName(java.lang.String value) {
      updateOperations.set("upperName", value);
      return this;
    }

    public JavadocClassUpdater unsetUpperName(java.lang.String value) {
      updateOperations.unset("upperName");
      return this;
    }

    public JavadocClassUpdater addUpperName(java.lang.String value) {
      updateOperations.add("upperName", value);
      return this;
    }

    public JavadocClassUpdater addUpperName(java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToUpperName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperName", values, addDups);
      return this;
    }
  
    public JavadocClassUpdater removeFirstUpperName() {
      updateOperations.removeFirst("upperName");
      return this;
    }
  
    public JavadocClassUpdater removeLastUpperName() {
      updateOperations.removeLast("upperName");
      return this;
    }
  
    public JavadocClassUpdater removeFromUpperName(java.lang.String value) {
      updateOperations.removeAll("upperName", value);
      return this;
    }

    public JavadocClassUpdater removeAllFromUpperName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("upperName", values);
      return this;
    }
 
    public JavadocClassUpdater decUpperName() {
      updateOperations.dec("upperName");
      return this;
    }

    public JavadocClassUpdater incUpperName() {
      updateOperations.inc("upperName");
      return this;
    }

    public JavadocClassUpdater incUpperName(Number value) {
      updateOperations.inc("upperName", value);
      return this;
    }
    public JavadocClassUpdater upperPackageName(java.lang.String value) {
      updateOperations.set("upperPackageName", value);
      return this;
    }

    public JavadocClassUpdater unsetUpperPackageName(java.lang.String value) {
      updateOperations.unset("upperPackageName");
      return this;
    }

    public JavadocClassUpdater addUpperPackageName(java.lang.String value) {
      updateOperations.add("upperPackageName", value);
      return this;
    }

    public JavadocClassUpdater addUpperPackageName(java.lang.String value, boolean addDups) {
      updateOperations.add("upperPackageName", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToUpperPackageName(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperPackageName", values, addDups);
      return this;
    }
  
    public JavadocClassUpdater removeFirstUpperPackageName() {
      updateOperations.removeFirst("upperPackageName");
      return this;
    }
  
    public JavadocClassUpdater removeLastUpperPackageName() {
      updateOperations.removeLast("upperPackageName");
      return this;
    }
  
    public JavadocClassUpdater removeFromUpperPackageName(java.lang.String value) {
      updateOperations.removeAll("upperPackageName", value);
      return this;
    }

    public JavadocClassUpdater removeAllFromUpperPackageName(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("upperPackageName", values);
      return this;
    }
 
    public JavadocClassUpdater decUpperPackageName() {
      updateOperations.dec("upperPackageName");
      return this;
    }

    public JavadocClassUpdater incUpperPackageName() {
      updateOperations.inc("upperPackageName");
      return this;
    }

    public JavadocClassUpdater incUpperPackageName(Number value) {
      updateOperations.inc("upperPackageName", value);
      return this;
    }
  }
}
