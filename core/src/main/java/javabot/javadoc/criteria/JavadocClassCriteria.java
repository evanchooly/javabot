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

  public JavadocClassCriteria api(javabot.javadoc.JavadocApi reference) {
    query.filter("api = ", reference);
    return this;
  }

  public JavadocClassCriteria superClass(javabot.javadoc.JavadocClass reference) {
    query.filter("superClass = ", reference);
    return this;
  }
}
