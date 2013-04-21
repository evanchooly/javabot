package javabot.javadoc.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

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

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> directUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("directUrl"));
  }

  public JavadocFieldCriteria distinctDirectUrl() {
    ((QueryImpl) query).getCollection().distinct("directUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public JavadocFieldCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> longUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("longUrl"));
  }

  public JavadocFieldCriteria distinctLongUrl() {
    ((QueryImpl) query).getCollection().distinct("longUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public JavadocFieldCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> shortUrl() {
    return new TypeSafeFieldEnd<>(query, query.criteria("shortUrl"));
  }

  public JavadocFieldCriteria distinctShortUrl() {
    ((QueryImpl) query).getCollection().distinct("shortUrl");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> type() {
    return new TypeSafeFieldEnd<>(query, query.criteria("type"));
  }

  public JavadocFieldCriteria distinctType() {
    ((QueryImpl) query).getCollection().distinct("type");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.javadoc.JavadocField, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public JavadocFieldCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }

  public JavadocFieldCriteria api(javabot.javadoc.JavadocApi reference) {
    query.filter("api = ", reference);
    return this;
  }

  public JavadocFieldCriteria javadocClass(javabot.javadoc.JavadocClass reference) {
    query.filter("javadocClass = ", reference);
    return this;
  }
}
