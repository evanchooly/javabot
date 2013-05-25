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


public class FactoidCriteria {
  private Query<javabot.model.Factoid> query;
  private Datastore ds;

  public Query<javabot.model.Factoid> query() {
    return query;
  }

  public FactoidCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Factoid.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public FactoidCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public FactoidCriteria orderById() {
    return orderById(true);
  }

  public FactoidCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public FactoidCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, org.joda.time.DateTime> lastUsed() {
    return new TypeSafeFieldEnd<>(query, query.criteria("lastUsed"));
  }

  public FactoidCriteria lastUsed(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("lastUsed")).equal(value);
    return this;
  }

  public FactoidCriteria orderByLastUsed() {
    return orderByLastUsed(true);
  }

  public FactoidCriteria orderByLastUsed(boolean ascending) {
    query.order((!ascending ? "-" : "") + "lastUsed");
    return this;
  }

  public FactoidCriteria distinctLastUsed() {
    ((QueryImpl) query).getCollection().distinct("lastUsed");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.Boolean> locked() {
    return new TypeSafeFieldEnd<>(query, query.criteria("locked"));
  }

  public FactoidCriteria locked(java.lang.Boolean value) {
    new TypeSafeFieldEnd<>(query, query.criteria("locked")).equal(value);
    return this;
  }

  public FactoidCriteria orderByLocked() {
    return orderByLocked(true);
  }

  public FactoidCriteria orderByLocked(boolean ascending) {
    query.order((!ascending ? "-" : "") + "locked");
    return this;
  }

  public FactoidCriteria distinctLocked() {
    ((QueryImpl) query).getCollection().distinct("locked");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public FactoidCriteria name(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("name")).equal(value);
    return this;
  }

  public FactoidCriteria orderByName() {
    return orderByName(true);
  }

  public FactoidCriteria orderByName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "name");
    return this;
  }

  public FactoidCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, org.joda.time.DateTime> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public FactoidCriteria updated(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("updated")).equal(value);
    return this;
  }

  public FactoidCriteria orderByUpdated() {
    return orderByUpdated(true);
  }

  public FactoidCriteria orderByUpdated(boolean ascending) {
    query.order((!ascending ? "-" : "") + "updated");
    return this;
  }

  public FactoidCriteria distinctUpdated() {
    ((QueryImpl) query).getCollection().distinct("updated");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public FactoidCriteria upperName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperName")).equal(value);
    return this;
  }

  public FactoidCriteria orderByUpperName() {
    return orderByUpperName(true);
  }

  public FactoidCriteria orderByUpperName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperName");
    return this;
  }

  public FactoidCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> upperUserName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperUserName"));
  }

  public FactoidCriteria upperUserName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperUserName")).equal(value);
    return this;
  }

  public FactoidCriteria orderByUpperUserName() {
    return orderByUpperUserName(true);
  }

  public FactoidCriteria orderByUpperUserName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperUserName");
    return this;
  }

  public FactoidCriteria distinctUpperUserName() {
    ((QueryImpl) query).getCollection().distinct("upperUserName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> userName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("userName"));
  }

  public FactoidCriteria userName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("userName")).equal(value);
    return this;
  }

  public FactoidCriteria orderByUserName() {
    return orderByUserName(true);
  }

  public FactoidCriteria orderByUserName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "userName");
    return this;
  }

  public FactoidCriteria distinctUserName() {
    ((QueryImpl) query).getCollection().distinct("userName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Factoid, java.lang.String> value() {
    return new TypeSafeFieldEnd<>(query, query.criteria("value"));
  }

  public FactoidCriteria value(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("value")).equal(value);
    return this;
  }

  public FactoidCriteria orderByValue() {
    return orderByValue(true);
  }

  public FactoidCriteria orderByValue(boolean ascending) {
    query.order((!ascending ? "-" : "") + "value");
    return this;
  }

  public FactoidCriteria distinctValue() {
    ((QueryImpl) query).getCollection().distinct("value");
    return this;
  }

  public FactoidUpdater getUpdater() {
    return new FactoidUpdater();
  }

  public class FactoidUpdater {
    UpdateOperations<javabot.model.Factoid> updateOperations;

    public FactoidUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.Factoid.class);
    }

    public UpdateResults<javabot.model.Factoid> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.Factoid> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.Factoid> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.Factoid> upsert(WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public FactoidUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public FactoidUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public FactoidUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public FactoidUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public FactoidUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public FactoidUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public FactoidUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public FactoidUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public FactoidUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public FactoidUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public FactoidUpdater lastUsed(org.joda.time.DateTime value) {
      updateOperations.set("lastUsed", value);
      return this;
    }

    public FactoidUpdater unsetLastUsed(org.joda.time.DateTime value) {
      updateOperations.unset("lastUsed");
      return this;
    }

    public FactoidUpdater addLastUsed(org.joda.time.DateTime value) {
      updateOperations.add("lastUsed", value);
      return this;
    }

    public FactoidUpdater addLastUsed(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("lastUsed", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToLastUsed(List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("lastUsed", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstLastUsed() {
      updateOperations.removeFirst("lastUsed");
      return this;
    }
  
    public FactoidUpdater removeLastLastUsed() {
      updateOperations.removeLast("lastUsed");
      return this;
    }
  
    public FactoidUpdater removeFromLastUsed(org.joda.time.DateTime value) {
      updateOperations.removeAll("lastUsed", value);
      return this;
    }

    public FactoidUpdater removeAllFromLastUsed(List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("lastUsed", values);
      return this;
    }
 
    public FactoidUpdater decLastUsed() {
      updateOperations.dec("lastUsed");
      return this;
    }

    public FactoidUpdater incLastUsed() {
      updateOperations.inc("lastUsed");
      return this;
    }

    public FactoidUpdater incLastUsed(Number value) {
      updateOperations.inc("lastUsed", value);
      return this;
    }
    public FactoidUpdater locked(java.lang.Boolean value) {
      updateOperations.set("locked", value);
      return this;
    }

    public FactoidUpdater unsetLocked(java.lang.Boolean value) {
      updateOperations.unset("locked");
      return this;
    }

    public FactoidUpdater addLocked(java.lang.Boolean value) {
      updateOperations.add("locked", value);
      return this;
    }

    public FactoidUpdater addLocked(String fieldExpr, java.lang.Boolean value, boolean addDups) {
      updateOperations.add("locked", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToLocked(List<java.lang.Boolean> values, boolean addDups) {
      updateOperations.addAll("locked", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstLocked() {
      updateOperations.removeFirst("locked");
      return this;
    }
  
    public FactoidUpdater removeLastLocked() {
      updateOperations.removeLast("locked");
      return this;
    }
  
    public FactoidUpdater removeFromLocked(java.lang.Boolean value) {
      updateOperations.removeAll("locked", value);
      return this;
    }

    public FactoidUpdater removeAllFromLocked(List<java.lang.Boolean> values) {
      updateOperations.removeAll("locked", values);
      return this;
    }
 
    public FactoidUpdater decLocked() {
      updateOperations.dec("locked");
      return this;
    }

    public FactoidUpdater incLocked() {
      updateOperations.inc("locked");
      return this;
    }

    public FactoidUpdater incLocked(Number value) {
      updateOperations.inc("locked", value);
      return this;
    }
    public FactoidUpdater name(java.lang.String value) {
      updateOperations.set("name", value);
      return this;
    }

    public FactoidUpdater unsetName(java.lang.String value) {
      updateOperations.unset("name");
      return this;
    }

    public FactoidUpdater addName(java.lang.String value) {
      updateOperations.add("name", value);
      return this;
    }

    public FactoidUpdater addName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("name", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstName() {
      updateOperations.removeFirst("name");
      return this;
    }
  
    public FactoidUpdater removeLastName() {
      updateOperations.removeLast("name");
      return this;
    }
  
    public FactoidUpdater removeFromName(java.lang.String value) {
      updateOperations.removeAll("name", value);
      return this;
    }

    public FactoidUpdater removeAllFromName(List<java.lang.String> values) {
      updateOperations.removeAll("name", values);
      return this;
    }
 
    public FactoidUpdater decName() {
      updateOperations.dec("name");
      return this;
    }

    public FactoidUpdater incName() {
      updateOperations.inc("name");
      return this;
    }

    public FactoidUpdater incName(Number value) {
      updateOperations.inc("name", value);
      return this;
    }
    public FactoidUpdater updated(org.joda.time.DateTime value) {
      updateOperations.set("updated", value);
      return this;
    }

    public FactoidUpdater unsetUpdated(org.joda.time.DateTime value) {
      updateOperations.unset("updated");
      return this;
    }

    public FactoidUpdater addUpdated(org.joda.time.DateTime value) {
      updateOperations.add("updated", value);
      return this;
    }

    public FactoidUpdater addUpdated(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("updated", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToUpdated(List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("updated", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstUpdated() {
      updateOperations.removeFirst("updated");
      return this;
    }
  
    public FactoidUpdater removeLastUpdated() {
      updateOperations.removeLast("updated");
      return this;
    }
  
    public FactoidUpdater removeFromUpdated(org.joda.time.DateTime value) {
      updateOperations.removeAll("updated", value);
      return this;
    }

    public FactoidUpdater removeAllFromUpdated(List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("updated", values);
      return this;
    }
 
    public FactoidUpdater decUpdated() {
      updateOperations.dec("updated");
      return this;
    }

    public FactoidUpdater incUpdated() {
      updateOperations.inc("updated");
      return this;
    }

    public FactoidUpdater incUpdated(Number value) {
      updateOperations.inc("updated", value);
      return this;
    }
    public FactoidUpdater upperName(java.lang.String value) {
      updateOperations.set("upperName", value);
      return this;
    }

    public FactoidUpdater unsetUpperName(java.lang.String value) {
      updateOperations.unset("upperName");
      return this;
    }

    public FactoidUpdater addUpperName(java.lang.String value) {
      updateOperations.add("upperName", value);
      return this;
    }

    public FactoidUpdater addUpperName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToUpperName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperName", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstUpperName() {
      updateOperations.removeFirst("upperName");
      return this;
    }
  
    public FactoidUpdater removeLastUpperName() {
      updateOperations.removeLast("upperName");
      return this;
    }
  
    public FactoidUpdater removeFromUpperName(java.lang.String value) {
      updateOperations.removeAll("upperName", value);
      return this;
    }

    public FactoidUpdater removeAllFromUpperName(List<java.lang.String> values) {
      updateOperations.removeAll("upperName", values);
      return this;
    }
 
    public FactoidUpdater decUpperName() {
      updateOperations.dec("upperName");
      return this;
    }

    public FactoidUpdater incUpperName() {
      updateOperations.inc("upperName");
      return this;
    }

    public FactoidUpdater incUpperName(Number value) {
      updateOperations.inc("upperName", value);
      return this;
    }
    public FactoidUpdater upperUserName(java.lang.String value) {
      updateOperations.set("upperUserName", value);
      return this;
    }

    public FactoidUpdater unsetUpperUserName(java.lang.String value) {
      updateOperations.unset("upperUserName");
      return this;
    }

    public FactoidUpdater addUpperUserName(java.lang.String value) {
      updateOperations.add("upperUserName", value);
      return this;
    }

    public FactoidUpdater addUpperUserName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("upperUserName", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToUpperUserName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperUserName", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstUpperUserName() {
      updateOperations.removeFirst("upperUserName");
      return this;
    }
  
    public FactoidUpdater removeLastUpperUserName() {
      updateOperations.removeLast("upperUserName");
      return this;
    }
  
    public FactoidUpdater removeFromUpperUserName(java.lang.String value) {
      updateOperations.removeAll("upperUserName", value);
      return this;
    }

    public FactoidUpdater removeAllFromUpperUserName(List<java.lang.String> values) {
      updateOperations.removeAll("upperUserName", values);
      return this;
    }
 
    public FactoidUpdater decUpperUserName() {
      updateOperations.dec("upperUserName");
      return this;
    }

    public FactoidUpdater incUpperUserName() {
      updateOperations.inc("upperUserName");
      return this;
    }

    public FactoidUpdater incUpperUserName(Number value) {
      updateOperations.inc("upperUserName", value);
      return this;
    }
    public FactoidUpdater userName(java.lang.String value) {
      updateOperations.set("userName", value);
      return this;
    }

    public FactoidUpdater unsetUserName(java.lang.String value) {
      updateOperations.unset("userName");
      return this;
    }

    public FactoidUpdater addUserName(java.lang.String value) {
      updateOperations.add("userName", value);
      return this;
    }

    public FactoidUpdater addUserName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("userName", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToUserName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("userName", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstUserName() {
      updateOperations.removeFirst("userName");
      return this;
    }
  
    public FactoidUpdater removeLastUserName() {
      updateOperations.removeLast("userName");
      return this;
    }
  
    public FactoidUpdater removeFromUserName(java.lang.String value) {
      updateOperations.removeAll("userName", value);
      return this;
    }

    public FactoidUpdater removeAllFromUserName(List<java.lang.String> values) {
      updateOperations.removeAll("userName", values);
      return this;
    }
 
    public FactoidUpdater decUserName() {
      updateOperations.dec("userName");
      return this;
    }

    public FactoidUpdater incUserName() {
      updateOperations.inc("userName");
      return this;
    }

    public FactoidUpdater incUserName(Number value) {
      updateOperations.inc("userName", value);
      return this;
    }
    public FactoidUpdater value(java.lang.String value) {
      updateOperations.set("value", value);
      return this;
    }

    public FactoidUpdater unsetValue(java.lang.String value) {
      updateOperations.unset("value");
      return this;
    }

    public FactoidUpdater addValue(java.lang.String value) {
      updateOperations.add("value", value);
      return this;
    }

    public FactoidUpdater addValue(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("value", value, addDups);
      return this;
    }

    public FactoidUpdater addAllToValue(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("value", values, addDups);
      return this;
    }
  
    public FactoidUpdater removeFirstValue() {
      updateOperations.removeFirst("value");
      return this;
    }
  
    public FactoidUpdater removeLastValue() {
      updateOperations.removeLast("value");
      return this;
    }
  
    public FactoidUpdater removeFromValue(java.lang.String value) {
      updateOperations.removeAll("value", value);
      return this;
    }

    public FactoidUpdater removeAllFromValue(List<java.lang.String> values) {
      updateOperations.removeAll("value", values);
      return this;
    }
 
    public FactoidUpdater decValue() {
      updateOperations.dec("value");
      return this;
    }

    public FactoidUpdater incValue() {
      updateOperations.inc("value");
      return this;
    }

    public FactoidUpdater incValue(Number value) {
      updateOperations.inc("value", value);
      return this;
    }
  }
}
