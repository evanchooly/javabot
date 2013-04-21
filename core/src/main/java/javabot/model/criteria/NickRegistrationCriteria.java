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

  public NickRegistrationCriteria distinctHost() {
    ((QueryImpl) query).getCollection().distinct("host");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public NickRegistrationCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, java.lang.String> nick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("nick"));
  }

  public NickRegistrationCriteria distinctNick() {
    ((QueryImpl) query).getCollection().distinct("nick");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, java.lang.String> twitterName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("twitterName"));
  }

  public NickRegistrationCriteria distinctTwitterName() {
    ((QueryImpl) query).getCollection().distinct("twitterName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, java.lang.String> url() {
    return new TypeSafeFieldEnd<>(query, query.criteria("url"));
  }

  public NickRegistrationCriteria distinctUrl() {
    ((QueryImpl) query).getCollection().distinct("url");
    return this;
  }
}
