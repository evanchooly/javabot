package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;

public class ChannelCriteria {
  private Query<javabot.model.Channel> query;
  private Datastore ds;

  public Query<javabot.model.Channel> query() {
    return query;
  }

  public ChannelCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Channel.class);
  }

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Channel, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ChannelCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public ChannelCriteria orderById() {
    return orderById(true);
  }

  public ChannelCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public ChannelCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Channel, java.lang.String> key() {
    return new TypeSafeFieldEnd<>(query, query.criteria("key"));
  }

  public ChannelCriteria key(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("key")).equal(value);
    return this;
  }

  public ChannelCriteria orderByKey() {
    return orderByKey(true);
  }

  public ChannelCriteria orderByKey(boolean ascending) {
    query.order((!ascending ? "-" : "") + "key");
    return this;
  }

  public ChannelCriteria distinctKey() {
    ((QueryImpl) query).getCollection().distinct("key");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Channel, java.lang.Boolean> logged() {
    return new TypeSafeFieldEnd<>(query, query.criteria("logged"));
  }

  public ChannelCriteria logged(java.lang.Boolean value) {
    new TypeSafeFieldEnd<>(query, query.criteria("logged")).equal(value);
    return this;
  }

  public ChannelCriteria orderByLogged() {
    return orderByLogged(true);
  }

  public ChannelCriteria orderByLogged(boolean ascending) {
    query.order((!ascending ? "-" : "") + "logged");
    return this;
  }

  public ChannelCriteria distinctLogged() {
    ((QueryImpl) query).getCollection().distinct("logged");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Channel, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public ChannelCriteria name(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("name")).equal(value);
    return this;
  }

  public ChannelCriteria orderByName() {
    return orderByName(true);
  }

  public ChannelCriteria orderByName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "name");
    return this;
  }

  public ChannelCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Channel, java.util.Date> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public ChannelCriteria updated(java.util.Date value) {
    new TypeSafeFieldEnd<>(query, query.criteria("updated")).equal(value);
    return this;
  }

  public ChannelCriteria orderByUpdated() {
    return orderByUpdated(true);
  }

  public ChannelCriteria orderByUpdated(boolean ascending) {
    query.order((!ascending ? "-" : "") + "updated");
    return this;
  }

  public ChannelCriteria distinctUpdated() {
    ((QueryImpl) query).getCollection().distinct("updated");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Channel, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public ChannelCriteria upperName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperName")).equal(value);
    return this;
  }

  public ChannelCriteria orderByUpperName() {
    return orderByUpperName(true);
  }

  public ChannelCriteria orderByUpperName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperName");
    return this;
  }

  public ChannelCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }
}
