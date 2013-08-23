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


public class JavadocApiCriteria {
  private Query<javabot.javadoc.JavadocApi> query;
  private Datastore ds;

  public Query<javabot.javadoc.JavadocApi> query() {
    return query;
  }

  public JavadocApiCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.javadoc.JavadocApi.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocApi, java.lang.String> baseUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("baseUrl"));
  }

  public JavadocApiCriteria baseUrl(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("baseUrl")).equal(value);
    return this;
  }

  public JavadocApiCriteria orderByBaseUrl() {
    return orderByBaseUrl(true);
  }

  public JavadocApiCriteria orderByBaseUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "baseUrl");
    return this;
  }

  public JavadocApiCriteria distinctBaseUrl() {
    ((QueryImpl) query).getCollection().distinct("baseUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocApi, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public JavadocApiCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public JavadocApiCriteria orderById() {
    return orderById(true);
  }

  public JavadocApiCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public JavadocApiCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocApi, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public JavadocApiCriteria name(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("name")).equal(value);
    return this;
  }

  public JavadocApiCriteria orderByName() {
    return orderByName(true);
  }

  public JavadocApiCriteria orderByName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "name");
    return this;
  }

  public JavadocApiCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocApi, java.lang.String> packages() {
    return new TypeSafeFieldEnd<>(query, query.criteria("packages"));
  }

  public JavadocApiCriteria packages(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("packages")).equal(value);
    return this;
  }

  public JavadocApiCriteria orderByPackages() {
    return orderByPackages(true);
  }

  public JavadocApiCriteria orderByPackages(boolean ascending) {
    query.order((!ascending ? "-" : "") + "packages");
    return this;
  }

  public JavadocApiCriteria distinctPackages() {
    ((QueryImpl) query).getCollection().distinct("packages");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocApi, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public JavadocApiCriteria upperName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperName")).equal(value);
    return this;
  }

  public JavadocApiCriteria orderByUpperName() {
    return orderByUpperName(true);
  }

  public JavadocApiCriteria orderByUpperName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperName");
    return this;
  }

  public JavadocApiCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }

  public JavadocApiUpdater getUpdater() {
    return new JavadocApiUpdater();
  }

  public class JavadocApiUpdater {
    UpdateOperations<javabot.javadoc.JavadocApi> updateOperations;

    public JavadocApiUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.javadoc.JavadocApi.class);
    }

    public UpdateResults<javabot.javadoc.JavadocApi> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.javadoc.JavadocApi> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.javadoc.JavadocApi> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.javadoc.JavadocApi> upsert(WriteConcern wc) {
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

    public JavadocApiUpdater addBaseUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("baseUrl", value, addDups);
      return this;
    }

    public JavadocApiUpdater addAllToBaseUrl(List<java.lang.String> values, boolean addDups) {
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

    public JavadocApiUpdater removeAllFromBaseUrl(List<java.lang.String> values) {
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

    public JavadocApiUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public JavadocApiUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public JavadocApiUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
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

    public JavadocApiUpdater addName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public JavadocApiUpdater addAllToName(List<java.lang.String> values, boolean addDups) {
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

    public JavadocApiUpdater removeAllFromName(List<java.lang.String> values) {
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
    public JavadocApiUpdater packages(java.lang.String value) {
      updateOperations.set("packages", value);
      return this;
    }

    public JavadocApiUpdater unsetPackages(java.lang.String value) {
      updateOperations.unset("packages");
      return this;
    }

    public JavadocApiUpdater addPackages(java.lang.String value) {
      updateOperations.add("packages", value);
      return this;
    }

    public JavadocApiUpdater addPackages(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("packages", value, addDups);
      return this;
    }

    public JavadocApiUpdater addAllToPackages(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("packages", values, addDups);
      return this;
    }
  
    public JavadocApiUpdater removeFirstPackages() {
      updateOperations.removeFirst("packages");
      return this;
    }
  
    public JavadocApiUpdater removeLastPackages() {
      updateOperations.removeLast("packages");
      return this;
    }
  
    public JavadocApiUpdater removeFromPackages(java.lang.String value) {
      updateOperations.removeAll("packages", value);
      return this;
    }

    public JavadocApiUpdater removeAllFromPackages(List<java.lang.String> values) {
      updateOperations.removeAll("packages", values);
      return this;
    }
 
    public JavadocApiUpdater decPackages() {
      updateOperations.dec("packages");
      return this;
    }

    public JavadocApiUpdater incPackages() {
      updateOperations.inc("packages");
      return this;
    }

    public JavadocApiUpdater incPackages(Number value) {
      updateOperations.inc("packages", value);
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

    public JavadocApiUpdater addUpperName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public JavadocApiUpdater addAllToUpperName(List<java.lang.String> values, boolean addDups) {
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

    public JavadocApiUpdater removeAllFromUpperName(List<java.lang.String> values) {
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
