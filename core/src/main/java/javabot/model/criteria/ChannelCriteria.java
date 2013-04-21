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

  public ChannelCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Channel, java.lang.String> key() {
    return new TypeSafeFieldEnd<>(query, query.criteria("key"));
  }

  public ChannelCriteria distinctKey() {
    ((QueryImpl) query).getCollection().distinct("key");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Channel, java.lang.Boolean> logged() {
    return new TypeSafeFieldEnd<>(query, query.criteria("logged"));
  }

  public ChannelCriteria distinctLogged() {
    ((QueryImpl) query).getCollection().distinct("logged");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Channel, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public ChannelCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Channel, java.util.Date> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public ChannelCriteria distinctUpdated() {
    ((QueryImpl) query).getCollection().distinct("updated");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Channel, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public ChannelCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }
}
