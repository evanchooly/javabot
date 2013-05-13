package javabot.javadoc.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

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
}
