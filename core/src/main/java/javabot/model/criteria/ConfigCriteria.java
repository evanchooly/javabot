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

  public ConfigCriteria historyLength(java.lang.Integer value) {
    new TypeSafeFieldEnd<>(query, query.criteria("historyLength")).equal(value);
    return this;
  }

  public ConfigCriteria orderByHistoryLength() {
    return orderByHistoryLength(true);
  }

  public ConfigCriteria orderByHistoryLength(boolean ascending) {
    query.order((!ascending ? "-" : "") + "historyLength");
    return this;
  }

  public ConfigCriteria distinctHistoryLength() {
    ((QueryImpl) query).getCollection().distinct("historyLength");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ConfigCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public ConfigCriteria orderById() {
    return orderById(true);
  }

  public ConfigCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public ConfigCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> nick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("nick"));
  }

  public ConfigCriteria nick(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("nick")).equal(value);
    return this;
  }

  public ConfigCriteria orderByNick() {
    return orderByNick(true);
  }

  public ConfigCriteria orderByNick(boolean ascending) {
    query.order((!ascending ? "-" : "") + "nick");
    return this;
  }

  public ConfigCriteria distinctNick() {
    ((QueryImpl) query).getCollection().distinct("nick");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.util.Set<java.lang.String>> operations() {
    return new TypeSafeFieldEnd<>(query, query.criteria("operations"));
  }

  public ConfigCriteria operations(java.util.Set<java.lang.String> value) {
    new TypeSafeFieldEnd<>(query, query.criteria("operations")).equal(value);
    return this;
  }

  public ConfigCriteria orderByOperations() {
    return orderByOperations(true);
  }

  public ConfigCriteria orderByOperations(boolean ascending) {
    query.order((!ascending ? "-" : "") + "operations");
    return this;
  }

  public ConfigCriteria distinctOperations() {
    ((QueryImpl) query).getCollection().distinct("operations");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> password() {
    return new TypeSafeFieldEnd<>(query, query.criteria("password"));
  }

  public ConfigCriteria password(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("password")).equal(value);
    return this;
  }

  public ConfigCriteria orderByPassword() {
    return orderByPassword(true);
  }

  public ConfigCriteria orderByPassword(boolean ascending) {
    query.order((!ascending ? "-" : "") + "password");
    return this;
  }

  public ConfigCriteria distinctPassword() {
    ((QueryImpl) query).getCollection().distinct("password");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.Integer> port() {
    return new TypeSafeFieldEnd<>(query, query.criteria("port"));
  }

  public ConfigCriteria port(java.lang.Integer value) {
    new TypeSafeFieldEnd<>(query, query.criteria("port")).equal(value);
    return this;
  }

  public ConfigCriteria orderByPort() {
    return orderByPort(true);
  }

  public ConfigCriteria orderByPort(boolean ascending) {
    query.order((!ascending ? "-" : "") + "port");
    return this;
  }

  public ConfigCriteria distinctPort() {
    ((QueryImpl) query).getCollection().distinct("port");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.Integer> schemaVersion() {
    return new TypeSafeFieldEnd<>(query, query.criteria("schemaVersion"));
  }

  public ConfigCriteria schemaVersion(java.lang.Integer value) {
    new TypeSafeFieldEnd<>(query, query.criteria("schemaVersion")).equal(value);
    return this;
  }

  public ConfigCriteria orderBySchemaVersion() {
    return orderBySchemaVersion(true);
  }

  public ConfigCriteria orderBySchemaVersion(boolean ascending) {
    query.order((!ascending ? "-" : "") + "schemaVersion");
    return this;
  }

  public ConfigCriteria distinctSchemaVersion() {
    ((QueryImpl) query).getCollection().distinct("schemaVersion");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> server() {
    return new TypeSafeFieldEnd<>(query, query.criteria("server"));
  }

  public ConfigCriteria server(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("server")).equal(value);
    return this;
  }

  public ConfigCriteria orderByServer() {
    return orderByServer(true);
  }

  public ConfigCriteria orderByServer(boolean ascending) {
    query.order((!ascending ? "-" : "") + "server");
    return this;
  }

  public ConfigCriteria distinctServer() {
    ((QueryImpl) query).getCollection().distinct("server");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> trigger() {
    return new TypeSafeFieldEnd<>(query, query.criteria("trigger"));
  }

  public ConfigCriteria trigger(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("trigger")).equal(value);
    return this;
  }

  public ConfigCriteria orderByTrigger() {
    return orderByTrigger(true);
  }

  public ConfigCriteria orderByTrigger(boolean ascending) {
    query.order((!ascending ? "-" : "") + "trigger");
    return this;
  }

  public ConfigCriteria distinctTrigger() {
    ((QueryImpl) query).getCollection().distinct("trigger");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> url() {
    return new TypeSafeFieldEnd<>(query, query.criteria("url"));
  }

  public ConfigCriteria url(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("url")).equal(value);
    return this;
  }

  public ConfigCriteria orderByUrl() {
    return orderByUrl(true);
  }

  public ConfigCriteria orderByUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "url");
    return this;
  }

  public ConfigCriteria distinctUrl() {
    ((QueryImpl) query).getCollection().distinct("url");
    return this;
  }
}
