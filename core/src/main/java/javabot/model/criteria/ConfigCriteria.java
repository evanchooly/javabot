package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

public class ConfigCriteria {
  private Query<javabot.model.Config> query;
  private Datastore ds;

  public Query<javabot.model.Config> query() {
    return query;
  }

  public ConfigCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Config.class);
  }

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.Integer> historyLength() {
    return new TypeSafeFieldEnd<>(query, query.criteria("historyLength"));
  }

  public ConfigCriteria distinctHistoryLength() {
    ((QueryImpl) query).getCollection().distinct("historyLength");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ConfigCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> nick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("nick"));
  }

  public ConfigCriteria distinctNick() {
    ((QueryImpl) query).getCollection().distinct("nick");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.util.Set<java.lang.String>> operations() {
    return new TypeSafeFieldEnd<>(query, query.criteria("operations"));
  }

  public ConfigCriteria distinctOperations() {
    ((QueryImpl) query).getCollection().distinct("operations");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> password() {
    return new TypeSafeFieldEnd<>(query, query.criteria("password"));
  }

  public ConfigCriteria distinctPassword() {
    ((QueryImpl) query).getCollection().distinct("password");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.Integer> port() {
    return new TypeSafeFieldEnd<>(query, query.criteria("port"));
  }

  public ConfigCriteria distinctPort() {
    ((QueryImpl) query).getCollection().distinct("port");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.Integer> schemaVersion() {
    return new TypeSafeFieldEnd<>(query, query.criteria("schemaVersion"));
  }

  public ConfigCriteria distinctSchemaVersion() {
    ((QueryImpl) query).getCollection().distinct("schemaVersion");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> server() {
    return new TypeSafeFieldEnd<>(query, query.criteria("server"));
  }

  public ConfigCriteria distinctServer() {
    ((QueryImpl) query).getCollection().distinct("server");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> trigger() {
    return new TypeSafeFieldEnd<>(query, query.criteria("trigger"));
  }

  public ConfigCriteria distinctTrigger() {
    ((QueryImpl) query).getCollection().distinct("trigger");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> url() {
    return new TypeSafeFieldEnd<>(query, query.criteria("url"));
  }

  public ConfigCriteria distinctUrl() {
    ((QueryImpl) query).getCollection().distinct("url");
    return this;
  }
}
