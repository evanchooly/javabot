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


public class ShunCriteria {
  private Query<javabot.model.Shun> query;
  private Datastore ds;

  public Query<javabot.model.Shun> query() {
    return query;
  }

  public ShunCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Shun.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Shun, org.joda.time.DateTime> expiry() {
    return new TypeSafeFieldEnd<>(query, query.criteria("expiry"));
  }

  public ShunCriteria expiry(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("expiry")).equal(value);
    return this;
  }

  public ShunCriteria orderByExpiry() {
    return orderByExpiry(true);
  }

  public ShunCriteria orderByExpiry(boolean ascending) {
    query.order((!ascending ? "-" : "") + "expiry");
    return this;
  }

  public ShunCriteria distinctExpiry() {
    ((QueryImpl) query).getCollection().distinct("expiry");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Shun, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ShunCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public ShunCriteria orderById() {
    return orderById(true);
  }

  public ShunCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public ShunCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Shun, java.lang.String> nick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("nick"));
  }

  public ShunCriteria nick(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("nick")).equal(value);
    return this;
  }

  public ShunCriteria orderByNick() {
    return orderByNick(true);
  }

  public ShunCriteria orderByNick(boolean ascending) {
    query.order((!ascending ? "-" : "") + "nick");
    return this;
  }

  public ShunCriteria distinctNick() {
    ((QueryImpl) query).getCollection().distinct("nick");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Shun, java.lang.String> upperNick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperNick"));
  }

  public ShunCriteria upperNick(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperNick")).equal(value);
    return this;
  }

  public ShunCriteria orderByUpperNick() {
    return orderByUpperNick(true);
  }

  public ShunCriteria orderByUpperNick(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperNick");
    return this;
  }

  public ShunCriteria distinctUpperNick() {
    ((QueryImpl) query).getCollection().distinct("upperNick");
    return this;
  }

  public ShunUpdater getUpdater() {
    return new ShunUpdater();
  }

  public class ShunUpdater {
    UpdateOperations<javabot.model.Shun> updateOperations;

    public ShunUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.Shun.class);
    }

    public UpdateResults<javabot.model.Shun> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.Shun> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.Shun> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.Shun> upsert(WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public ShunUpdater expiry(org.joda.time.DateTime value) {
      updateOperations.set("expiry", value);
      return this;
    }

    public ShunUpdater unsetExpiry(org.joda.time.DateTime value) {
      updateOperations.unset("expiry");
      return this;
    }

    public ShunUpdater addExpiry(org.joda.time.DateTime value) {
      updateOperations.add("expiry", value);
      return this;
    }

    public ShunUpdater addExpiry(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("expiry", value, addDups);
      return this;
    }

    public ShunUpdater addAllToExpiry(List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("expiry", values, addDups);
      return this;
    }
  
    public ShunUpdater removeFirstExpiry() {
      updateOperations.removeFirst("expiry");
      return this;
    }
  
    public ShunUpdater removeLastExpiry() {
      updateOperations.removeLast("expiry");
      return this;
    }
  
    public ShunUpdater removeFromExpiry(org.joda.time.DateTime value) {
      updateOperations.removeAll("expiry", value);
      return this;
    }

    public ShunUpdater removeAllFromExpiry(List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("expiry", values);
      return this;
    }
 
    public ShunUpdater decExpiry() {
      updateOperations.dec("expiry");
      return this;
    }

    public ShunUpdater incExpiry() {
      updateOperations.inc("expiry");
      return this;
    }

    public ShunUpdater incExpiry(Number value) {
      updateOperations.inc("expiry", value);
      return this;
    }
    public ShunUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public ShunUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public ShunUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public ShunUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ShunUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public ShunUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public ShunUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public ShunUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public ShunUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public ShunUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public ShunUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public ShunUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public ShunUpdater nick(java.lang.String value) {
      updateOperations.set("nick", value);
      return this;
    }

    public ShunUpdater unsetNick(java.lang.String value) {
      updateOperations.unset("nick");
      return this;
    }

    public ShunUpdater addNick(java.lang.String value) {
      updateOperations.add("nick", value);
      return this;
    }

    public ShunUpdater addNick(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("nick", value, addDups);
      return this;
    }

    public ShunUpdater addAllToNick(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("nick", values, addDups);
      return this;
    }
  
    public ShunUpdater removeFirstNick() {
      updateOperations.removeFirst("nick");
      return this;
    }
  
    public ShunUpdater removeLastNick() {
      updateOperations.removeLast("nick");
      return this;
    }
  
    public ShunUpdater removeFromNick(java.lang.String value) {
      updateOperations.removeAll("nick", value);
      return this;
    }

    public ShunUpdater removeAllFromNick(List<java.lang.String> values) {
      updateOperations.removeAll("nick", values);
      return this;
    }
 
    public ShunUpdater decNick() {
      updateOperations.dec("nick");
      return this;
    }

    public ShunUpdater incNick() {
      updateOperations.inc("nick");
      return this;
    }

    public ShunUpdater incNick(Number value) {
      updateOperations.inc("nick", value);
      return this;
    }
    public ShunUpdater upperNick(java.lang.String value) {
      updateOperations.set("upperNick", value);
      return this;
    }

    public ShunUpdater unsetUpperNick(java.lang.String value) {
      updateOperations.unset("upperNick");
      return this;
    }

    public ShunUpdater addUpperNick(java.lang.String value) {
      updateOperations.add("upperNick", value);
      return this;
    }

    public ShunUpdater addUpperNick(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("upperNick", value, addDups);
      return this;
    }

    public ShunUpdater addAllToUpperNick(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperNick", values, addDups);
      return this;
    }
  
    public ShunUpdater removeFirstUpperNick() {
      updateOperations.removeFirst("upperNick");
      return this;
    }
  
    public ShunUpdater removeLastUpperNick() {
      updateOperations.removeLast("upperNick");
      return this;
    }
  
    public ShunUpdater removeFromUpperNick(java.lang.String value) {
      updateOperations.removeAll("upperNick", value);
      return this;
    }

    public ShunUpdater removeAllFromUpperNick(List<java.lang.String> values) {
      updateOperations.removeAll("upperNick", values);
      return this;
    }
 
    public ShunUpdater decUpperNick() {
      updateOperations.dec("upperNick");
      return this;
    }

    public ShunUpdater incUpperNick() {
      updateOperations.inc("upperNick");
      return this;
    }

    public ShunUpdater incUpperNick(Number value) {
      updateOperations.inc("upperNick", value);
      return this;
    }
  }
}
