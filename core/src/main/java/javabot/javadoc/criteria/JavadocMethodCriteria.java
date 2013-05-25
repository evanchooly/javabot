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


public class JavadocMethodCriteria {
  private Query<javabot.javadoc.JavadocMethod> query;
  private Datastore ds;

  public Query<javabot.javadoc.JavadocMethod> query() {
    return query;
  }

  public JavadocMethodCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.javadoc.JavadocMethod.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocMethod, java.lang.String> directUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("directUrl"));
  }

  public JavadocMethodCriteria directUrl(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("directUrl")).equal(value);
    return this;
  }

  public JavadocMethodCriteria orderByDirectUrl() {
    return orderByDirectUrl(true);
  }

  public JavadocMethodCriteria orderByDirectUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "directUrl");
    return this;
  }

  public JavadocMethodCriteria distinctDirectUrl() {
    ((QueryImpl) query).getCollection().distinct("directUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocMethod, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public JavadocMethodCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public JavadocMethodCriteria orderById() {
    return orderById(true);
  }

  public JavadocMethodCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public JavadocMethodCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocMethod, java.lang.String> longSignatureTypes() {
    return new TypeSafeFieldEnd<>(query, query.criteria("longSignatureTypes"));
  }

  public JavadocMethodCriteria longSignatureTypes(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("longSignatureTypes")).equal(value);
    return this;
  }

  public JavadocMethodCriteria orderByLongSignatureTypes() {
    return orderByLongSignatureTypes(true);
  }

  public JavadocMethodCriteria orderByLongSignatureTypes(boolean ascending) {
    query.order((!ascending ? "-" : "") + "longSignatureTypes");
    return this;
  }

  public JavadocMethodCriteria distinctLongSignatureTypes() {
    ((QueryImpl) query).getCollection().distinct("longSignatureTypes");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocMethod, java.lang.String> longUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("longUrl"));
  }

  public JavadocMethodCriteria longUrl(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("longUrl")).equal(value);
    return this;
  }

  public JavadocMethodCriteria orderByLongUrl() {
    return orderByLongUrl(true);
  }

  public JavadocMethodCriteria orderByLongUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "longUrl");
    return this;
  }

  public JavadocMethodCriteria distinctLongUrl() {
    ((QueryImpl) query).getCollection().distinct("longUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocMethod, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public JavadocMethodCriteria name(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("name")).equal(value);
    return this;
  }

  public JavadocMethodCriteria orderByName() {
    return orderByName(true);
  }

  public JavadocMethodCriteria orderByName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "name");
    return this;
  }

  public JavadocMethodCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocMethod, java.lang.Integer> paramCount() {
    return new TypeSafeFieldEnd<>(query, query.criteria("paramCount"));
  }

  public JavadocMethodCriteria paramCount(java.lang.Integer value) {
    new TypeSafeFieldEnd<>(query, query.criteria("paramCount")).equal(value);
    return this;
  }

  public JavadocMethodCriteria orderByParamCount() {
    return orderByParamCount(true);
  }

  public JavadocMethodCriteria orderByParamCount(boolean ascending) {
    query.order((!ascending ? "-" : "") + "paramCount");
    return this;
  }

  public JavadocMethodCriteria distinctParamCount() {
    ((QueryImpl) query).getCollection().distinct("paramCount");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocMethod, java.lang.String> shortSignatureTypes() {
    return new TypeSafeFieldEnd<>(query, query.criteria("shortSignatureTypes"));
  }

  public JavadocMethodCriteria shortSignatureTypes(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("shortSignatureTypes")).equal(value);
    return this;
  }

  public JavadocMethodCriteria orderByShortSignatureTypes() {
    return orderByShortSignatureTypes(true);
  }

  public JavadocMethodCriteria orderByShortSignatureTypes(boolean ascending) {
    query.order((!ascending ? "-" : "") + "shortSignatureTypes");
    return this;
  }

  public JavadocMethodCriteria distinctShortSignatureTypes() {
    ((QueryImpl) query).getCollection().distinct("shortSignatureTypes");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocMethod, java.lang.String> shortUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("shortUrl"));
  }

  public JavadocMethodCriteria shortUrl(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("shortUrl")).equal(value);
    return this;
  }

  public JavadocMethodCriteria orderByShortUrl() {
    return orderByShortUrl(true);
  }

  public JavadocMethodCriteria orderByShortUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "shortUrl");
    return this;
  }

  public JavadocMethodCriteria distinctShortUrl() {
    ((QueryImpl) query).getCollection().distinct("shortUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocMethod, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public JavadocMethodCriteria upperName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperName")).equal(value);
    return this;
  }

  public JavadocMethodCriteria orderByUpperName() {
    return orderByUpperName(true);
  }

  public JavadocMethodCriteria orderByUpperName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperName");
    return this;
  }

  public JavadocMethodCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }

  public JavadocMethodCriteria api(javabot.javadoc.JavadocApi reference) {
    query.filter("api = ", reference);
    return this;
  }

  public JavadocMethodCriteria javadocClass(javabot.javadoc.JavadocClass reference) {
    query.filter("javadocClass = ", reference);
    return this;
  }

  public JavadocMethodUpdater getUpdater() {
    return new JavadocMethodUpdater();
  }

  public class JavadocMethodUpdater {
    UpdateOperations<javabot.javadoc.JavadocMethod> updateOperations;

    public JavadocMethodUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.javadoc.JavadocMethod.class);
    }

    public UpdateResults<javabot.javadoc.JavadocMethod> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.javadoc.JavadocMethod> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.javadoc.JavadocMethod> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.javadoc.JavadocMethod> upsert(WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
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

    public JavadocMethodUpdater addDirectUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("directUrl", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToDirectUrl(List<java.lang.String> values, boolean addDups) {
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

    public JavadocMethodUpdater removeAllFromDirectUrl(List<java.lang.String> values) {
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

    public JavadocMethodUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public JavadocMethodUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
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

    public JavadocMethodUpdater addLongSignatureTypes(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("longSignatureTypes", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToLongSignatureTypes(List<java.lang.String> values, boolean addDups) {
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

    public JavadocMethodUpdater removeAllFromLongSignatureTypes(List<java.lang.String> values) {
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

    public JavadocMethodUpdater addLongUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("longUrl", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToLongUrl(List<java.lang.String> values, boolean addDups) {
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

    public JavadocMethodUpdater removeAllFromLongUrl(List<java.lang.String> values) {
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

    public JavadocMethodUpdater addName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToName(List<java.lang.String> values, boolean addDups) {
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

    public JavadocMethodUpdater removeAllFromName(List<java.lang.String> values) {
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

    public JavadocMethodUpdater addParamCount(String fieldExpr, java.lang.Integer value, boolean addDups) {
      updateOperations.add("paramCount", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToParamCount(List<java.lang.Integer> values, boolean addDups) {
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

    public JavadocMethodUpdater removeAllFromParamCount(List<java.lang.Integer> values) {
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

    public JavadocMethodUpdater addShortSignatureTypes(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("shortSignatureTypes", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToShortSignatureTypes(List<java.lang.String> values, boolean addDups) {
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

    public JavadocMethodUpdater removeAllFromShortSignatureTypes(List<java.lang.String> values) {
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

    public JavadocMethodUpdater addShortUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("shortUrl", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToShortUrl(List<java.lang.String> values, boolean addDups) {
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

    public JavadocMethodUpdater removeAllFromShortUrl(List<java.lang.String> values) {
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

    public JavadocMethodUpdater addUpperName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public JavadocMethodUpdater addAllToUpperName(List<java.lang.String> values, boolean addDups) {
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

    public JavadocMethodUpdater removeAllFromUpperName(List<java.lang.String> values) {
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
