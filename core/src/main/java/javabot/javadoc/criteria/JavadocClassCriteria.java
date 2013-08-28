package javabot.javadoc.criteria;

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


public class JavadocClassCriteria {
  private Query<javabot.javadoc.JavadocClass> query;
  private Datastore ds;

  public Query<javabot.javadoc.JavadocClass> query() {
    return query;
  }

  public JavadocClassCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.javadoc.JavadocClass.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, org.bson.types.ObjectId> apiId() {
    return new TypeSafeFieldEnd<>(query, query.criteria("apiId"));
  }

  public JavadocClassCriteria apiId(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("apiId")).equal(value);
    return this;
  }

  public JavadocClassCriteria orderByApiId() {
    return orderByApiId(true);
  }

  public JavadocClassCriteria orderByApiId(boolean ascending) {
    query.order((!ascending ? "-" : "") + "apiId");
    return this;
  }

  public JavadocClassCriteria distinctApiId() {
    ((QueryImpl) query).getCollection().distinct("apiId");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> directUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("directUrl"));
  }

  public JavadocClassCriteria directUrl(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("directUrl")).equal(value);
    return this;
  }

  public JavadocClassCriteria orderByDirectUrl() {
    return orderByDirectUrl(true);
  }

  public JavadocClassCriteria orderByDirectUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "directUrl");
    return this;
  }

  public JavadocClassCriteria distinctDirectUrl() {
    ((QueryImpl) query).getCollection().distinct("directUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.util.List<javabot.javadoc.JavadocField>> fields() {
    return new TypeSafeFieldEnd<>(query, query.criteria("fields"));
  }

  public JavadocClassCriteria fields(java.util.List<javabot.javadoc.JavadocField> value) {
    new TypeSafeFieldEnd<>(query, query.criteria("fields")).equal(value);
    return this;
  }

  public JavadocClassCriteria orderByFields() {
    return orderByFields(true);
  }

  public JavadocClassCriteria orderByFields(boolean ascending) {
    query.order((!ascending ? "-" : "") + "fields");
    return this;
  }

  public JavadocClassCriteria distinctFields() {
    ((QueryImpl) query).getCollection().distinct("fields");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public JavadocClassCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public JavadocClassCriteria orderById() {
    return orderById(true);
  }

  public JavadocClassCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public JavadocClassCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> longUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("longUrl"));
  }

  public JavadocClassCriteria longUrl(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("longUrl")).equal(value);
    return this;
  }

  public JavadocClassCriteria orderByLongUrl() {
    return orderByLongUrl(true);
  }

  public JavadocClassCriteria orderByLongUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "longUrl");
    return this;
  }

  public JavadocClassCriteria distinctLongUrl() {
    ((QueryImpl) query).getCollection().distinct("longUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.util.List<javabot.javadoc.JavadocMethod>> methods() {
    return new TypeSafeFieldEnd<>(query, query.criteria("methods"));
  }

  public JavadocClassCriteria methods(java.util.List<javabot.javadoc.JavadocMethod> value) {
    new TypeSafeFieldEnd<>(query, query.criteria("methods")).equal(value);
    return this;
  }

  public JavadocClassCriteria orderByMethods() {
    return orderByMethods(true);
  }

  public JavadocClassCriteria orderByMethods(boolean ascending) {
    query.order((!ascending ? "-" : "") + "methods");
    return this;
  }

  public JavadocClassCriteria distinctMethods() {
    ((QueryImpl) query).getCollection().distinct("methods");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public JavadocClassCriteria name(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("name")).equal(value);
    return this;
  }

  public JavadocClassCriteria orderByName() {
    return orderByName(true);
  }

  public JavadocClassCriteria orderByName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "name");
    return this;
  }

  public JavadocClassCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> packageName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("packageName"));
  }

  public JavadocClassCriteria packageName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("packageName")).equal(value);
    return this;
  }

  public JavadocClassCriteria orderByPackageName() {
    return orderByPackageName(true);
  }

  public JavadocClassCriteria orderByPackageName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "packageName");
    return this;
  }

  public JavadocClassCriteria distinctPackageName() {
    ((QueryImpl) query).getCollection().distinct("packageName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> shortUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("shortUrl"));
  }

  public JavadocClassCriteria shortUrl(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("shortUrl")).equal(value);
    return this;
  }

  public JavadocClassCriteria orderByShortUrl() {
    return orderByShortUrl(true);
  }

  public JavadocClassCriteria orderByShortUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "shortUrl");
    return this;
  }

  public JavadocClassCriteria distinctShortUrl() {
    ((QueryImpl) query).getCollection().distinct("shortUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, org.bson.types.ObjectId> superClassId() {
    return new TypeSafeFieldEnd<>(query, query.criteria("superClassId"));
  }

  public JavadocClassCriteria superClassId(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("superClassId")).equal(value);
    return this;
  }

  public JavadocClassCriteria orderBySuperClassId() {
    return orderBySuperClassId(true);
  }

  public JavadocClassCriteria orderBySuperClassId(boolean ascending) {
    query.order((!ascending ? "-" : "") + "superClassId");
    return this;
  }

  public JavadocClassCriteria distinctSuperClassId() {
    ((QueryImpl) query).getCollection().distinct("superClassId");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public JavadocClassCriteria upperName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperName")).equal(value);
    return this;
  }

  public JavadocClassCriteria orderByUpperName() {
    return orderByUpperName(true);
  }

  public JavadocClassCriteria orderByUpperName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperName");
    return this;
  }

  public JavadocClassCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> upperPackageName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperPackageName"));
  }

  public JavadocClassCriteria upperPackageName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperPackageName")).equal(value);
    return this;
  }

  public JavadocClassCriteria orderByUpperPackageName() {
    return orderByUpperPackageName(true);
  }

  public JavadocClassCriteria orderByUpperPackageName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperPackageName");
    return this;
  }

  public JavadocClassCriteria distinctUpperPackageName() {
    ((QueryImpl) query).getCollection().distinct("upperPackageName");
    return this;
  }

  public JavadocClassUpdater getUpdater() {
    return new JavadocClassUpdater();
  }

  public class JavadocClassUpdater {
    UpdateOperations<javabot.javadoc.JavadocClass> updateOperations;

    public JavadocClassUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.javadoc.JavadocClass.class);
    }

    public UpdateResults<javabot.javadoc.JavadocClass> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.javadoc.JavadocClass> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.javadoc.JavadocClass> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.javadoc.JavadocClass> upsert(WriteConcern wc) {
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

    public JavadocClassUpdater addApiId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("apiId", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToApiId(List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public JavadocClassUpdater removeAllFromApiId(List<org.bson.types.ObjectId> values) {
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

    public JavadocClassUpdater addDirectUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("directUrl", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToDirectUrl(List<java.lang.String> values, boolean addDups) {
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

    public JavadocClassUpdater removeAllFromDirectUrl(List<java.lang.String> values) {
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

    public JavadocClassUpdater addFields(String fieldExpr, java.util.List<javabot.javadoc.JavadocField> value, boolean addDups) {
      updateOperations.add("fields", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToFields(List<java.util.List<javabot.javadoc.JavadocField>> values, boolean addDups) {
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

    public JavadocClassUpdater removeAllFromFields(List<java.util.List<javabot.javadoc.JavadocField>> values) {
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

    public JavadocClassUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public JavadocClassUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
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

    public JavadocClassUpdater addLongUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("longUrl", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToLongUrl(List<java.lang.String> values, boolean addDups) {
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

    public JavadocClassUpdater removeAllFromLongUrl(List<java.lang.String> values) {
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

    public JavadocClassUpdater addMethods(String fieldExpr, java.util.List<javabot.javadoc.JavadocMethod> value, boolean addDups) {
      updateOperations.add("methods", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToMethods(List<java.util.List<javabot.javadoc.JavadocMethod>> values, boolean addDups) {
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

    public JavadocClassUpdater removeAllFromMethods(List<java.util.List<javabot.javadoc.JavadocMethod>> values) {
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

    public JavadocClassUpdater addName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToName(List<java.lang.String> values, boolean addDups) {
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

    public JavadocClassUpdater removeAllFromName(List<java.lang.String> values) {
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

    public JavadocClassUpdater addPackageName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("packageName", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToPackageName(List<java.lang.String> values, boolean addDups) {
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

    public JavadocClassUpdater removeAllFromPackageName(List<java.lang.String> values) {
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

    public JavadocClassUpdater addShortUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("shortUrl", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToShortUrl(List<java.lang.String> values, boolean addDups) {
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

    public JavadocClassUpdater removeAllFromShortUrl(List<java.lang.String> values) {
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

    public JavadocClassUpdater addSuperClassId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("superClassId", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToSuperClassId(List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public JavadocClassUpdater removeAllFromSuperClassId(List<org.bson.types.ObjectId> values) {
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

    public JavadocClassUpdater addUpperName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToUpperName(List<java.lang.String> values, boolean addDups) {
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

    public JavadocClassUpdater removeAllFromUpperName(List<java.lang.String> values) {
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

    public JavadocClassUpdater addUpperPackageName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("upperPackageName", value, addDups);
      return this;
    }

    public JavadocClassUpdater addAllToUpperPackageName(List<java.lang.String> values, boolean addDups) {
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

    public JavadocClassUpdater removeAllFromUpperPackageName(List<java.lang.String> values) {
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
