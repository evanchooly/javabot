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


public class JavadocFieldCriteria {
  private Query<javabot.javadoc.JavadocField> query;
  private Datastore ds;

  public Query<javabot.javadoc.JavadocField> query() {
    return query;
  }

  public JavadocFieldCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.javadoc.JavadocField.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, org.bson.types.ObjectId> apiId() {
    return new TypeSafeFieldEnd<>(query, query.criteria("apiId"));
  }

  public JavadocFieldCriteria apiId(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("apiId")).equal(value);
    return this;
  }

  public JavadocFieldCriteria orderByApiId() {
    return orderByApiId(true);
  }

  public JavadocFieldCriteria orderByApiId(boolean ascending) {
    query.order((!ascending ? "-" : "") + "apiId");
    return this;
  }

  public JavadocFieldCriteria distinctApiId() {
    ((QueryImpl) query).getCollection().distinct("apiId");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> directUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("directUrl"));
  }

  public JavadocFieldCriteria directUrl(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("directUrl")).equal(value);
    return this;
  }

  public JavadocFieldCriteria orderByDirectUrl() {
    return orderByDirectUrl(true);
  }

  public JavadocFieldCriteria orderByDirectUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "directUrl");
    return this;
  }

  public JavadocFieldCriteria distinctDirectUrl() {
    ((QueryImpl) query).getCollection().distinct("directUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public JavadocFieldCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public JavadocFieldCriteria orderById() {
    return orderById(true);
  }

  public JavadocFieldCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public JavadocFieldCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, org.bson.types.ObjectId> javadocClassId() {
    return new TypeSafeFieldEnd<>(query, query.criteria("javadocClassId"));
  }

  public JavadocFieldCriteria javadocClassId(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("javadocClassId")).equal(value);
    return this;
  }

  public JavadocFieldCriteria orderByJavadocClassId() {
    return orderByJavadocClassId(true);
  }

  public JavadocFieldCriteria orderByJavadocClassId(boolean ascending) {
    query.order((!ascending ? "-" : "") + "javadocClassId");
    return this;
  }

  public JavadocFieldCriteria distinctJavadocClassId() {
    ((QueryImpl) query).getCollection().distinct("javadocClassId");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> longUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("longUrl"));
  }

  public JavadocFieldCriteria longUrl(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("longUrl")).equal(value);
    return this;
  }

  public JavadocFieldCriteria orderByLongUrl() {
    return orderByLongUrl(true);
  }

  public JavadocFieldCriteria orderByLongUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "longUrl");
    return this;
  }

  public JavadocFieldCriteria distinctLongUrl() {
    ((QueryImpl) query).getCollection().distinct("longUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public JavadocFieldCriteria name(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("name")).equal(value);
    return this;
  }

  public JavadocFieldCriteria orderByName() {
    return orderByName(true);
  }

  public JavadocFieldCriteria orderByName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "name");
    return this;
  }

  public JavadocFieldCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> parentClassName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("parentClassName"));
  }

  public JavadocFieldCriteria parentClassName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("parentClassName")).equal(value);
    return this;
  }

  public JavadocFieldCriteria orderByParentClassName() {
    return orderByParentClassName(true);
  }

  public JavadocFieldCriteria orderByParentClassName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "parentClassName");
    return this;
  }

  public JavadocFieldCriteria distinctParentClassName() {
    ((QueryImpl) query).getCollection().distinct("parentClassName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> shortUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("shortUrl"));
  }

  public JavadocFieldCriteria shortUrl(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("shortUrl")).equal(value);
    return this;
  }

  public JavadocFieldCriteria orderByShortUrl() {
    return orderByShortUrl(true);
  }

  public JavadocFieldCriteria orderByShortUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "shortUrl");
    return this;
  }

  public JavadocFieldCriteria distinctShortUrl() {
    ((QueryImpl) query).getCollection().distinct("shortUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> type() {
    return new TypeSafeFieldEnd<>(query, query.criteria("type"));
  }

  public JavadocFieldCriteria type(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("type")).equal(value);
    return this;
  }

  public JavadocFieldCriteria orderByType() {
    return orderByType(true);
  }

  public JavadocFieldCriteria orderByType(boolean ascending) {
    query.order((!ascending ? "-" : "") + "type");
    return this;
  }

  public JavadocFieldCriteria distinctType() {
    ((QueryImpl) query).getCollection().distinct("type");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public JavadocFieldCriteria upperName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperName")).equal(value);
    return this;
  }

  public JavadocFieldCriteria orderByUpperName() {
    return orderByUpperName(true);
  }

  public JavadocFieldCriteria orderByUpperName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperName");
    return this;
  }

  public JavadocFieldCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }

  public JavadocFieldUpdater getUpdater() {
    return new JavadocFieldUpdater();
  }

  public class JavadocFieldUpdater {
    UpdateOperations<javabot.javadoc.JavadocField> updateOperations;

    public JavadocFieldUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.javadoc.JavadocField.class);
    }

    public UpdateResults<javabot.javadoc.JavadocField> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.javadoc.JavadocField> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.javadoc.JavadocField> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.javadoc.JavadocField> upsert(WriteConcern wc) {
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

    public JavadocFieldUpdater addApiId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("apiId", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToApiId(List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public JavadocFieldUpdater removeAllFromApiId(List<org.bson.types.ObjectId> values) {
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

    public JavadocFieldUpdater addDirectUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("directUrl", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToDirectUrl(List<java.lang.String> values, boolean addDups) {
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

    public JavadocFieldUpdater removeAllFromDirectUrl(List<java.lang.String> values) {
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

    public JavadocFieldUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public JavadocFieldUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
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

    public JavadocFieldUpdater addJavadocClassId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("javadocClassId", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToJavadocClassId(List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public JavadocFieldUpdater removeAllFromJavadocClassId(List<org.bson.types.ObjectId> values) {
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

    public JavadocFieldUpdater addLongUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("longUrl", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToLongUrl(List<java.lang.String> values, boolean addDups) {
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

    public JavadocFieldUpdater removeAllFromLongUrl(List<java.lang.String> values) {
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

    public JavadocFieldUpdater addName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToName(List<java.lang.String> values, boolean addDups) {
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

    public JavadocFieldUpdater removeAllFromName(List<java.lang.String> values) {
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

    public JavadocFieldUpdater addParentClassName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("parentClassName", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToParentClassName(List<java.lang.String> values, boolean addDups) {
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

    public JavadocFieldUpdater removeAllFromParentClassName(List<java.lang.String> values) {
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

    public JavadocFieldUpdater addShortUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("shortUrl", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToShortUrl(List<java.lang.String> values, boolean addDups) {
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

    public JavadocFieldUpdater removeAllFromShortUrl(List<java.lang.String> values) {
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

    public JavadocFieldUpdater addType(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("type", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToType(List<java.lang.String> values, boolean addDups) {
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

    public JavadocFieldUpdater removeAllFromType(List<java.lang.String> values) {
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

    public JavadocFieldUpdater addUpperName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public JavadocFieldUpdater addAllToUpperName(List<java.lang.String> values, boolean addDups) {
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

    public JavadocFieldUpdater removeAllFromUpperName(List<java.lang.String> values) {
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
