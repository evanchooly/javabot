package javabot.javadoc.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

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
}
