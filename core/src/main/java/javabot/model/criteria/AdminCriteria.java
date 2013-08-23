package javabot.model.criteria;

import com.antwerkz.critter.TypeSafeFieldEnd;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.CriteriaContainer;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryImpl;
import com.google.code.morphia.query.UpdateOperations;
import com.google.code.morphia.query.UpdateResults;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import java.util.List;


public class AdminCriteria {
  private Query<javabot.model.Admin> query;
  private Datastore ds;

  public Query<javabot.model.Admin> query() {
    return query;
  }

  public AdminCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Admin.class);
  }

  public WriteResult delete() {
     return ds.delete(query());
  }

  public WriteResult delete(WriteConcern wc) {
     return ds.delete(query(), wc);
  }

  public CriteriaContainer or(Criteria... criteria) {
    return query.or(criteria);
  }

  public CriteriaContainer and(Criteria... criteria) {
    return query.and(criteria);
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, java.lang.String> addedBy() {
    return new TypeSafeFieldEnd<>(query, query.criteria("addedBy"));
  }

  public AdminCriteria addedBy(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("addedBy")).equal(value);
    return this;
  }

  public AdminCriteria orderByAddedBy() {
    return orderByAddedBy(true);
  }

  public AdminCriteria orderByAddedBy(boolean ascending) {
    query.order((!ascending ? "-" : "") + "addedBy");
    return this;
  }

  public AdminCriteria distinctAddedBy() {
    ((QueryImpl) query).getCollection().distinct("addedBy");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, java.lang.Boolean> botOwner() {
    return new TypeSafeFieldEnd<>(query, query.criteria("botOwner"));
  }

  public AdminCriteria botOwner(java.lang.Boolean value) {
    new TypeSafeFieldEnd<>(query, query.criteria("botOwner")).equal(value);
    return this;
  }

  public AdminCriteria orderByBotOwner() {
    return orderByBotOwner(true);
  }

  public AdminCriteria orderByBotOwner(boolean ascending) {
    query.order((!ascending ? "-" : "") + "botOwner");
    return this;
  }

  public AdminCriteria distinctBotOwner() {
    ((QueryImpl) query).getCollection().distinct("botOwner");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, java.lang.String> hostName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("hostName"));
  }

  public AdminCriteria hostName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("hostName")).equal(value);
    return this;
  }

  public AdminCriteria orderByHostName() {
    return orderByHostName(true);
  }

  public AdminCriteria orderByHostName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "hostName");
    return this;
  }

  public AdminCriteria distinctHostName() {
    ((QueryImpl) query).getCollection().distinct("hostName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public AdminCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public AdminCriteria orderById() {
    return orderById(true);
  }

  public AdminCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public AdminCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, java.lang.String> ircName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("ircName"));
  }

  public AdminCriteria ircName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("ircName")).equal(value);
    return this;
  }

  public AdminCriteria orderByIrcName() {
    return orderByIrcName(true);
  }

  public AdminCriteria orderByIrcName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "ircName");
    return this;
  }

  public AdminCriteria distinctIrcName() {
    ((QueryImpl) query).getCollection().distinct("ircName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, org.joda.time.DateTime> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public AdminCriteria updated(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("updated")).equal(value);
    return this;
  }

  public AdminCriteria orderByUpdated() {
    return orderByUpdated(true);
  }

  public AdminCriteria orderByUpdated(boolean ascending) {
    query.order((!ascending ? "-" : "") + "updated");
    return this;
  }

  public AdminCriteria distinctUpdated() {
    ((QueryImpl) query).getCollection().distinct("updated");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Admin, java.lang.String> userName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("userName"));
  }

  public AdminCriteria userName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("userName")).equal(value);
    return this;
  }

  public AdminCriteria orderByUserName() {
    return orderByUserName(true);
  }

  public AdminCriteria orderByUserName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "userName");
    return this;
  }

  public AdminCriteria distinctUserName() {
    ((QueryImpl) query).getCollection().distinct("userName");
    return this;
  }

  public AdminUpdater getUpdater() {
    return new AdminUpdater();
  }

  public class AdminUpdater {
    UpdateOperations<javabot.model.Admin> updateOperations;

    public AdminUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.Admin.class);
    }

    public UpdateResults<javabot.model.Admin> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.Admin> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.Admin> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.Admin> upsert(WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public AdminUpdater addedBy(java.lang.String value) {
      updateOperations.set("addedBy", value);
      return this;
    }

    public AdminUpdater unsetAddedBy(java.lang.String value) {
      updateOperations.unset("addedBy");
      return this;
    }

    public AdminUpdater addAddedBy(java.lang.String value) {
      updateOperations.add("addedBy", value);
      return this;
    }

    public AdminUpdater addAddedBy(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("addedBy", value, addDups);
      return this;
    }

    public AdminUpdater addAllToAddedBy(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("addedBy", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstAddedBy() {
      updateOperations.removeFirst("addedBy");
      return this;
    }
  
    public AdminUpdater removeLastAddedBy() {
      updateOperations.removeLast("addedBy");
      return this;
    }
  
    public AdminUpdater removeFromAddedBy(java.lang.String value) {
      updateOperations.removeAll("addedBy", value);
      return this;
    }

    public AdminUpdater removeAllFromAddedBy(List<java.lang.String> values) {
      updateOperations.removeAll("addedBy", values);
      return this;
    }
 
    public AdminUpdater decAddedBy() {
      updateOperations.dec("addedBy");
      return this;
    }

    public AdminUpdater incAddedBy() {
      updateOperations.inc("addedBy");
      return this;
    }

    public AdminUpdater incAddedBy(Number value) {
      updateOperations.inc("addedBy", value);
      return this;
    }
    public AdminUpdater botOwner(java.lang.Boolean value) {
      updateOperations.set("botOwner", value);
      return this;
    }

    public AdminUpdater unsetBotOwner(java.lang.Boolean value) {
      updateOperations.unset("botOwner");
      return this;
    }

    public AdminUpdater addBotOwner(java.lang.Boolean value) {
      updateOperations.add("botOwner", value);
      return this;
    }

    public AdminUpdater addBotOwner(String fieldExpr, java.lang.Boolean value, boolean addDups) {
      updateOperations.add("botOwner", value, addDups);
      return this;
    }

    public AdminUpdater addAllToBotOwner(List<java.lang.Boolean> values, boolean addDups) {
      updateOperations.addAll("botOwner", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstBotOwner() {
      updateOperations.removeFirst("botOwner");
      return this;
    }
  
    public AdminUpdater removeLastBotOwner() {
      updateOperations.removeLast("botOwner");
      return this;
    }
  
    public AdminUpdater removeFromBotOwner(java.lang.Boolean value) {
      updateOperations.removeAll("botOwner", value);
      return this;
    }

    public AdminUpdater removeAllFromBotOwner(List<java.lang.Boolean> values) {
      updateOperations.removeAll("botOwner", values);
      return this;
    }
 
    public AdminUpdater decBotOwner() {
      updateOperations.dec("botOwner");
      return this;
    }

    public AdminUpdater incBotOwner() {
      updateOperations.inc("botOwner");
      return this;
    }

    public AdminUpdater incBotOwner(Number value) {
      updateOperations.inc("botOwner", value);
      return this;
    }
    public AdminUpdater hostName(java.lang.String value) {
      updateOperations.set("hostName", value);
      return this;
    }

    public AdminUpdater unsetHostName(java.lang.String value) {
      updateOperations.unset("hostName");
      return this;
    }

    public AdminUpdater addHostName(java.lang.String value) {
      updateOperations.add("hostName", value);
      return this;
    }

    public AdminUpdater addHostName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("hostName", value, addDups);
      return this;
    }

    public AdminUpdater addAllToHostName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("hostName", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstHostName() {
      updateOperations.removeFirst("hostName");
      return this;
    }
  
    public AdminUpdater removeLastHostName() {
      updateOperations.removeLast("hostName");
      return this;
    }
  
    public AdminUpdater removeFromHostName(java.lang.String value) {
      updateOperations.removeAll("hostName", value);
      return this;
    }

    public AdminUpdater removeAllFromHostName(List<java.lang.String> values) {
      updateOperations.removeAll("hostName", values);
      return this;
    }
 
    public AdminUpdater decHostName() {
      updateOperations.dec("hostName");
      return this;
    }

    public AdminUpdater incHostName() {
      updateOperations.inc("hostName");
      return this;
    }

    public AdminUpdater incHostName(Number value) {
      updateOperations.inc("hostName", value);
      return this;
    }
    public AdminUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public AdminUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public AdminUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public AdminUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public AdminUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public AdminUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public AdminUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public AdminUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public AdminUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public AdminUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public AdminUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public AdminUpdater ircName(java.lang.String value) {
      updateOperations.set("ircName", value);
      return this;
    }

    public AdminUpdater unsetIrcName(java.lang.String value) {
      updateOperations.unset("ircName");
      return this;
    }

    public AdminUpdater addIrcName(java.lang.String value) {
      updateOperations.add("ircName", value);
      return this;
    }

    public AdminUpdater addIrcName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("ircName", value, addDups);
      return this;
    }

    public AdminUpdater addAllToIrcName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("ircName", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstIrcName() {
      updateOperations.removeFirst("ircName");
      return this;
    }
  
    public AdminUpdater removeLastIrcName() {
      updateOperations.removeLast("ircName");
      return this;
    }
  
    public AdminUpdater removeFromIrcName(java.lang.String value) {
      updateOperations.removeAll("ircName", value);
      return this;
    }

    public AdminUpdater removeAllFromIrcName(List<java.lang.String> values) {
      updateOperations.removeAll("ircName", values);
      return this;
    }
 
    public AdminUpdater decIrcName() {
      updateOperations.dec("ircName");
      return this;
    }

    public AdminUpdater incIrcName() {
      updateOperations.inc("ircName");
      return this;
    }

    public AdminUpdater incIrcName(Number value) {
      updateOperations.inc("ircName", value);
      return this;
    }
    public AdminUpdater updated(org.joda.time.DateTime value) {
      updateOperations.set("updated", value);
      return this;
    }

    public AdminUpdater unsetUpdated(org.joda.time.DateTime value) {
      updateOperations.unset("updated");
      return this;
    }

    public AdminUpdater addUpdated(org.joda.time.DateTime value) {
      updateOperations.add("updated", value);
      return this;
    }

    public AdminUpdater addUpdated(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("updated", value, addDups);
      return this;
    }

    public AdminUpdater addAllToUpdated(List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("updated", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstUpdated() {
      updateOperations.removeFirst("updated");
      return this;
    }
  
    public AdminUpdater removeLastUpdated() {
      updateOperations.removeLast("updated");
      return this;
    }
  
    public AdminUpdater removeFromUpdated(org.joda.time.DateTime value) {
      updateOperations.removeAll("updated", value);
      return this;
    }

    public AdminUpdater removeAllFromUpdated(List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("updated", values);
      return this;
    }
 
    public AdminUpdater decUpdated() {
      updateOperations.dec("updated");
      return this;
    }

    public AdminUpdater incUpdated() {
      updateOperations.inc("updated");
      return this;
    }

    public AdminUpdater incUpdated(Number value) {
      updateOperations.inc("updated", value);
      return this;
    }
    public AdminUpdater userName(java.lang.String value) {
      updateOperations.set("userName", value);
      return this;
    }

    public AdminUpdater unsetUserName(java.lang.String value) {
      updateOperations.unset("userName");
      return this;
    }

    public AdminUpdater addUserName(java.lang.String value) {
      updateOperations.add("userName", value);
      return this;
    }

    public AdminUpdater addUserName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("userName", value, addDups);
      return this;
    }

    public AdminUpdater addAllToUserName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("userName", values, addDups);
      return this;
    }
  
    public AdminUpdater removeFirstUserName() {
      updateOperations.removeFirst("userName");
      return this;
    }
  
    public AdminUpdater removeLastUserName() {
      updateOperations.removeLast("userName");
      return this;
    }
  
    public AdminUpdater removeFromUserName(java.lang.String value) {
      updateOperations.removeAll("userName", value);
      return this;
    }

    public AdminUpdater removeAllFromUserName(List<java.lang.String> values) {
      updateOperations.removeAll("userName", values);
      return this;
    }
 
    public AdminUpdater decUserName() {
      updateOperations.dec("userName");
      return this;
    }

    public AdminUpdater incUserName() {
      updateOperations.inc("userName");
      return this;
    }

    public AdminUpdater incUserName(Number value) {
      updateOperations.inc("userName", value);
      return this;
    }
  }
}
