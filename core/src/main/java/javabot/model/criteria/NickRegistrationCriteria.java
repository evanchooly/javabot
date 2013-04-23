package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

public class NickRegistrationCriteria {
  private Query<javabot.model.NickRegistration> query;
  private Datastore ds;

  public Query<javabot.model.NickRegistration> query() {
    return query;
  }

  public NickRegistrationCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.NickRegistration.class);
  }

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, java.lang.String> host() {
    return new TypeSafeFieldEnd<>(query, query.criteria("host"));
  }

  public NickRegistrationCriteria host(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("host")).equal(value);
    return this;
  }

  public NickRegistrationCriteria distinctHost() {
    ((QueryImpl) query).getCollection().distinct("host");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public NickRegistrationCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public NickRegistrationCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, java.lang.String> nick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("nick"));
  }

  public NickRegistrationCriteria nick(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("nick")).equal(value);
    return this;
  }

  public NickRegistrationCriteria distinctNick() {
    ((QueryImpl) query).getCollection().distinct("nick");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, java.lang.String> twitterName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("twitterName"));
  }

  public NickRegistrationCriteria twitterName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("twitterName")).equal(value);
    return this;
  }

  public NickRegistrationCriteria distinctTwitterName() {
    ((QueryImpl) query).getCollection().distinct("twitterName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, java.lang.String> url() {
    return new TypeSafeFieldEnd<>(query, query.criteria("url"));
  }

  public NickRegistrationCriteria url(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("url")).equal(value);
    return this;
  }

  public NickRegistrationCriteria distinctUrl() {
    ((QueryImpl) query).getCollection().distinct("url");
    return this;
  }
}
