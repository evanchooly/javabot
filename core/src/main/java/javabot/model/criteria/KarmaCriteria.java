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


public class KarmaCriteria {
  private Query<javabot.model.Karma> query;
  private Datastore ds;

  public Query<javabot.model.Karma> query() {
    return query;
  }

  public KarmaCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Karma.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Karma, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public KarmaCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public KarmaCriteria orderById() {
    return orderById(true);
  }

  public KarmaCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public KarmaCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Karma, java.lang.String> name() {
    return new TypeSafeFieldEnd<>(query, query.criteria("name"));
  }

  public KarmaCriteria name(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("name")).equal(value);
    return this;
  }

  public KarmaCriteria orderByName() {
    return orderByName(true);
  }

  public KarmaCriteria orderByName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "name");
    return this;
  }

  public KarmaCriteria distinctName() {
    ((QueryImpl) query).getCollection().distinct("name");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Karma, org.joda.time.DateTime> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public KarmaCriteria updated(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("updated")).equal(value);
    return this;
  }

  public KarmaCriteria orderByUpdated() {
    return orderByUpdated(true);
  }

  public KarmaCriteria orderByUpdated(boolean ascending) {
    query.order((!ascending ? "-" : "") + "updated");
    return this;
  }

  public KarmaCriteria distinctUpdated() {
    ((QueryImpl) query).getCollection().distinct("updated");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Karma, java.lang.String> upperName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperName"));
  }

  public KarmaCriteria upperName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperName")).equal(value);
    return this;
  }

  public KarmaCriteria orderByUpperName() {
    return orderByUpperName(true);
  }

  public KarmaCriteria orderByUpperName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperName");
    return this;
  }

  public KarmaCriteria distinctUpperName() {
    ((QueryImpl) query).getCollection().distinct("upperName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Karma, java.lang.String> userName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("userName"));
  }

  public KarmaCriteria userName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("userName")).equal(value);
    return this;
  }

  public KarmaCriteria orderByUserName() {
    return orderByUserName(true);
  }

  public KarmaCriteria orderByUserName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "userName");
    return this;
  }

  public KarmaCriteria distinctUserName() {
    ((QueryImpl) query).getCollection().distinct("userName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Karma, java.lang.Integer> value() {
    return new TypeSafeFieldEnd<>(query, query.criteria("value"));
  }

  public KarmaCriteria value(java.lang.Integer value) {
    new TypeSafeFieldEnd<>(query, query.criteria("value")).equal(value);
    return this;
  }

  public KarmaCriteria orderByValue() {
    return orderByValue(true);
  }

  public KarmaCriteria orderByValue(boolean ascending) {
    query.order((!ascending ? "-" : "") + "value");
    return this;
  }

  public KarmaCriteria distinctValue() {
    ((QueryImpl) query).getCollection().distinct("value");
    return this;
  }

  public KarmaUpdater getUpdater() {
    return new KarmaUpdater();
  }

  public class KarmaUpdater {
    UpdateOperations<javabot.model.Karma> updateOperations;

    public KarmaUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.Karma.class);
    }

    public UpdateResults<javabot.model.Karma> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.Karma> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.Karma> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.Karma> upsert(WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public KarmaUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public KarmaUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public KarmaUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public KarmaUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public KarmaUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public KarmaUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public KarmaUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public KarmaUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public KarmaUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public KarmaUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public KarmaUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public KarmaUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public KarmaUpdater name(java.lang.String value) {
      updateOperations.set("name", value);
      return this;
    }

    public KarmaUpdater unsetName(java.lang.String value) {
      updateOperations.unset("name");
      return this;
    }

    public KarmaUpdater addName(java.lang.String value) {
      updateOperations.add("name", value);
      return this;
    }

    public KarmaUpdater addName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public KarmaUpdater addAllToName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("name", values, addDups);
      return this;
    }
  
    public KarmaUpdater removeFirstName() {
      updateOperations.removeFirst("name");
      return this;
    }
  
    public KarmaUpdater removeLastName() {
      updateOperations.removeLast("name");
      return this;
    }
  
    public KarmaUpdater removeFromName(java.lang.String value) {
      updateOperations.removeAll("name", value);
      return this;
    }

    public KarmaUpdater removeAllFromName(List<java.lang.String> values) {
      updateOperations.removeAll("name", values);
      return this;
    }
 
    public KarmaUpdater decName() {
      updateOperations.dec("name");
      return this;
    }

    public KarmaUpdater incName() {
      updateOperations.inc("name");
      return this;
    }

    public KarmaUpdater incName(Number value) {
      updateOperations.inc("name", value);
      return this;
    }
    public KarmaUpdater updated(org.joda.time.DateTime value) {
      updateOperations.set("updated", value);
      return this;
    }

    public KarmaUpdater unsetUpdated(org.joda.time.DateTime value) {
      updateOperations.unset("updated");
      return this;
    }

    public KarmaUpdater addUpdated(org.joda.time.DateTime value) {
      updateOperations.add("updated", value);
      return this;
    }

    public KarmaUpdater addUpdated(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("updated", value, addDups);
      return this;
    }

    public KarmaUpdater addAllToUpdated(List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("updated", values, addDups);
      return this;
    }
  
    public KarmaUpdater removeFirstUpdated() {
      updateOperations.removeFirst("updated");
      return this;
    }
  
    public KarmaUpdater removeLastUpdated() {
      updateOperations.removeLast("updated");
      return this;
    }
  
    public KarmaUpdater removeFromUpdated(org.joda.time.DateTime value) {
      updateOperations.removeAll("updated", value);
      return this;
    }

    public KarmaUpdater removeAllFromUpdated(List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("updated", values);
      return this;
    }
 
    public KarmaUpdater decUpdated() {
      updateOperations.dec("updated");
      return this;
    }

    public KarmaUpdater incUpdated() {
      updateOperations.inc("updated");
      return this;
    }

    public KarmaUpdater incUpdated(Number value) {
      updateOperations.inc("updated", value);
      return this;
    }
    public KarmaUpdater upperName(java.lang.String value) {
      updateOperations.set("upperName", value);
      return this;
    }

    public KarmaUpdater unsetUpperName(java.lang.String value) {
      updateOperations.unset("upperName");
      return this;
    }

    public KarmaUpdater addUpperName(java.lang.String value) {
      updateOperations.add("upperName", value);
      return this;
    }

    public KarmaUpdater addUpperName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public KarmaUpdater addAllToUpperName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperName", values, addDups);
      return this;
    }
  
    public KarmaUpdater removeFirstUpperName() {
      updateOperations.removeFirst("upperName");
      return this;
    }
  
    public KarmaUpdater removeLastUpperName() {
      updateOperations.removeLast("upperName");
      return this;
    }
  
    public KarmaUpdater removeFromUpperName(java.lang.String value) {
      updateOperations.removeAll("upperName", value);
      return this;
    }

    public KarmaUpdater removeAllFromUpperName(List<java.lang.String> values) {
      updateOperations.removeAll("upperName", values);
      return this;
    }
 
    public KarmaUpdater decUpperName() {
      updateOperations.dec("upperName");
      return this;
    }

    public KarmaUpdater incUpperName() {
      updateOperations.inc("upperName");
      return this;
    }

    public KarmaUpdater incUpperName(Number value) {
      updateOperations.inc("upperName", value);
      return this;
    }
    public KarmaUpdater userName(java.lang.String value) {
      updateOperations.set("userName", value);
      return this;
    }

    public KarmaUpdater unsetUserName(java.lang.String value) {
      updateOperations.unset("userName");
      return this;
    }

    public KarmaUpdater addUserName(java.lang.String value) {
      updateOperations.add("userName", value);
      return this;
    }

    public KarmaUpdater addUserName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("userName", value, addDups);
      return this;
    }

    public KarmaUpdater addAllToUserName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("userName", values, addDups);
      return this;
    }
  
    public KarmaUpdater removeFirstUserName() {
      updateOperations.removeFirst("userName");
      return this;
    }
  
    public KarmaUpdater removeLastUserName() {
      updateOperations.removeLast("userName");
      return this;
    }
  
    public KarmaUpdater removeFromUserName(java.lang.String value) {
      updateOperations.removeAll("userName", value);
      return this;
    }

    public KarmaUpdater removeAllFromUserName(List<java.lang.String> values) {
      updateOperations.removeAll("userName", values);
      return this;
    }
 
    public KarmaUpdater decUserName() {
      updateOperations.dec("userName");
      return this;
    }

    public KarmaUpdater incUserName() {
      updateOperations.inc("userName");
      return this;
    }

    public KarmaUpdater incUserName(Number value) {
      updateOperations.inc("userName", value);
      return this;
    }
    public KarmaUpdater value(java.lang.Integer value) {
      updateOperations.set("value", value);
      return this;
    }

    public KarmaUpdater unsetValue(java.lang.Integer value) {
      updateOperations.unset("value");
      return this;
    }

    public KarmaUpdater addValue(java.lang.Integer value) {
      updateOperations.add("value", value);
      return this;
    }

    public KarmaUpdater addValue(String fieldExpr, java.lang.Integer value, boolean addDups) {
      updateOperations.add("value", value, addDups);
      return this;
    }

    public KarmaUpdater addAllToValue(List<java.lang.Integer> values, boolean addDups) {
      updateOperations.addAll("value", values, addDups);
      return this;
    }
  
    public KarmaUpdater removeFirstValue() {
      updateOperations.removeFirst("value");
      return this;
    }
  
    public KarmaUpdater removeLastValue() {
      updateOperations.removeLast("value");
      return this;
    }
  
    public KarmaUpdater removeFromValue(java.lang.Integer value) {
      updateOperations.removeAll("value", value);
      return this;
    }

    public KarmaUpdater removeAllFromValue(List<java.lang.Integer> values) {
      updateOperations.removeAll("value", values);
      return this;
    }
 
    public KarmaUpdater decValue() {
      updateOperations.dec("value");
      return this;
    }

    public KarmaUpdater incValue() {
      updateOperations.inc("value");
      return this;
    }

    public KarmaUpdater incValue(Number value) {
      updateOperations.inc("value", value);
      return this;
    }
  }
}
