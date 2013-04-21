package javabot.javadoc.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

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

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> directUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("directUrl"));
  }

  public JavadocClassCriteria distinctDirectUrl() {
    ((QueryImpl) query).getCollection().distinct("directUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.util.List<javabot.javadoc.JavadocField>> fields() {
    return new TypeSafeFieldEnd<>(query, query.criteria("fields"));
  }

  public JavadocClassCriteria distinctFields() {
    ((QueryImpl) query).getCollection().distinct("fields");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public JavadocClassCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> longUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("longUrl"));
  }

  public JavadocClassCriteria distinctLongUrl() {
    ((QueryImpl) query).getCollection().distinct("longUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.util.List<javabot.javadoc.JavadocMethod>> methods() {
    return new TypeSafeFieldEnd<>(query, query.criteria("methods"));
  }

  public JavadocClassCriteria distinctMethods() {
    ((QueryImpl) query).getCollection().distinct("methods");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public JavadocClassCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> packageName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("packageName"));
  }

  public JavadocClassCriteria distinctPackageName() {
    ((QueryImpl) query).getCollection().distinct("packageName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> shortUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("shortUrl"));
  }

  public JavadocClassCriteria distinctShortUrl() {
    ((QueryImpl) query).getCollection().distinct("shortUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public JavadocClassCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocClass, java.lang.String> upperPackage() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperPackage"));
  }

  public JavadocClassCriteria distinctUpperPackage() {
    ((QueryImpl) query).getCollection().distinct("upperPackage");
    return this;
  }

  public JavadocClassCriteria api(javabot.javadoc.JavadocApi reference) {
    query.filter("api = ", reference);
    return this;
  }

  public JavadocClassCriteria superClass(javabot.javadoc.JavadocClass reference) {
    query.filter("superClass = ", reference);
    return this;
  }
}
