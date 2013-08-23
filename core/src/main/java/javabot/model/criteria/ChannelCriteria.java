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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Channel, org.joda.time.DateTime> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public ChannelCriteria updated(org.joda.time.DateTime value) {
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

  public ChannelUpdater getUpdater() {
    return new ChannelUpdater();
  }

  public class ChannelUpdater {
    UpdateOperations<javabot.model.Channel> updateOperations;

    public ChannelUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.Channel.class);
    }

    public UpdateResults<javabot.model.Channel> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.Channel> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.Channel> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.Channel> upsert(WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public ChannelUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public ChannelUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public ChannelUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public ChannelUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ChannelUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public ChannelUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public ChannelUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public ChannelUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public ChannelUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public ChannelUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public ChannelUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public ChannelUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public ChannelUpdater key(java.lang.String value) {
      updateOperations.set("key", value);
      return this;
    }

    public ChannelUpdater unsetKey(java.lang.String value) {
      updateOperations.unset("key");
      return this;
    }

    public ChannelUpdater addKey(java.lang.String value) {
      updateOperations.add("key", value);
      return this;
    }

    public ChannelUpdater addKey(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("key", value, addDups);
      return this;
    }

    public ChannelUpdater addAllToKey(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("key", values, addDups);
      return this;
    }
  
    public ChannelUpdater removeFirstKey() {
      updateOperations.removeFirst("key");
      return this;
    }
  
    public ChannelUpdater removeLastKey() {
      updateOperations.removeLast("key");
      return this;
    }
  
    public ChannelUpdater removeFromKey(java.lang.String value) {
      updateOperations.removeAll("key", value);
      return this;
    }

    public ChannelUpdater removeAllFromKey(List<java.lang.String> values) {
      updateOperations.removeAll("key", values);
      return this;
    }
 
    public ChannelUpdater decKey() {
      updateOperations.dec("key");
      return this;
    }

    public ChannelUpdater incKey() {
      updateOperations.inc("key");
      return this;
    }

    public ChannelUpdater incKey(Number value) {
      updateOperations.inc("key", value);
      return this;
    }
    public ChannelUpdater logged(java.lang.Boolean value) {
      updateOperations.set("logged", value);
      return this;
    }

    public ChannelUpdater unsetLogged(java.lang.Boolean value) {
      updateOperations.unset("logged");
      return this;
    }

    public ChannelUpdater addLogged(java.lang.Boolean value) {
      updateOperations.add("logged", value);
      return this;
    }

    public ChannelUpdater addLogged(String fieldExpr, java.lang.Boolean value, boolean addDups) {
      updateOperations.add("logged", value, addDups);
      return this;
    }

    public ChannelUpdater addAllToLogged(List<java.lang.Boolean> values, boolean addDups) {
      updateOperations.addAll("logged", values, addDups);
      return this;
    }
  
    public ChannelUpdater removeFirstLogged() {
      updateOperations.removeFirst("logged");
      return this;
    }
  
    public ChannelUpdater removeLastLogged() {
      updateOperations.removeLast("logged");
      return this;
    }
  
    public ChannelUpdater removeFromLogged(java.lang.Boolean value) {
      updateOperations.removeAll("logged", value);
      return this;
    }

    public ChannelUpdater removeAllFromLogged(List<java.lang.Boolean> values) {
      updateOperations.removeAll("logged", values);
      return this;
    }
 
    public ChannelUpdater decLogged() {
      updateOperations.dec("logged");
      return this;
    }

    public ChannelUpdater incLogged() {
      updateOperations.inc("logged");
      return this;
    }

    public ChannelUpdater incLogged(Number value) {
      updateOperations.inc("logged", value);
      return this;
    }
    public ChannelUpdater name(java.lang.String value) {
      updateOperations.set("name", value);
      return this;
    }

    public ChannelUpdater unsetName(java.lang.String value) {
      updateOperations.unset("name");
      return this;
    }

    public ChannelUpdater addName(java.lang.String value) {
      updateOperations.add("name", value);
      return this;
    }

    public ChannelUpdater addName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("name", value, addDups);
      return this;
    }

    public ChannelUpdater addAllToName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("name", values, addDups);
      return this;
    }
  
    public ChannelUpdater removeFirstName() {
      updateOperations.removeFirst("name");
      return this;
    }
  
    public ChannelUpdater removeLastName() {
      updateOperations.removeLast("name");
      return this;
    }
  
    public ChannelUpdater removeFromName(java.lang.String value) {
      updateOperations.removeAll("name", value);
      return this;
    }

    public ChannelUpdater removeAllFromName(List<java.lang.String> values) {
      updateOperations.removeAll("name", values);
      return this;
    }
 
    public ChannelUpdater decName() {
      updateOperations.dec("name");
      return this;
    }

    public ChannelUpdater incName() {
      updateOperations.inc("name");
      return this;
    }

    public ChannelUpdater incName(Number value) {
      updateOperations.inc("name", value);
      return this;
    }
    public ChannelUpdater updated(org.joda.time.DateTime value) {
      updateOperations.set("updated", value);
      return this;
    }

    public ChannelUpdater unsetUpdated(org.joda.time.DateTime value) {
      updateOperations.unset("updated");
      return this;
    }

    public ChannelUpdater addUpdated(org.joda.time.DateTime value) {
      updateOperations.add("updated", value);
      return this;
    }

    public ChannelUpdater addUpdated(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("updated", value, addDups);
      return this;
    }

    public ChannelUpdater addAllToUpdated(List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("updated", values, addDups);
      return this;
    }
  
    public ChannelUpdater removeFirstUpdated() {
      updateOperations.removeFirst("updated");
      return this;
    }
  
    public ChannelUpdater removeLastUpdated() {
      updateOperations.removeLast("updated");
      return this;
    }
  
    public ChannelUpdater removeFromUpdated(org.joda.time.DateTime value) {
      updateOperations.removeAll("updated", value);
      return this;
    }

    public ChannelUpdater removeAllFromUpdated(List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("updated", values);
      return this;
    }
 
    public ChannelUpdater decUpdated() {
      updateOperations.dec("updated");
      return this;
    }

    public ChannelUpdater incUpdated() {
      updateOperations.inc("updated");
      return this;
    }

    public ChannelUpdater incUpdated(Number value) {
      updateOperations.inc("updated", value);
      return this;
    }
    public ChannelUpdater upperName(java.lang.String value) {
      updateOperations.set("upperName", value);
      return this;
    }

    public ChannelUpdater unsetUpperName(java.lang.String value) {
      updateOperations.unset("upperName");
      return this;
    }

    public ChannelUpdater addUpperName(java.lang.String value) {
      updateOperations.add("upperName", value);
      return this;
    }

    public ChannelUpdater addUpperName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("upperName", value, addDups);
      return this;
    }

    public ChannelUpdater addAllToUpperName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperName", values, addDups);
      return this;
    }
  
    public ChannelUpdater removeFirstUpperName() {
      updateOperations.removeFirst("upperName");
      return this;
    }
  
    public ChannelUpdater removeLastUpperName() {
      updateOperations.removeLast("upperName");
      return this;
    }
  
    public ChannelUpdater removeFromUpperName(java.lang.String value) {
      updateOperations.removeAll("upperName", value);
      return this;
    }

    public ChannelUpdater removeAllFromUpperName(List<java.lang.String> values) {
      updateOperations.removeAll("upperName", values);
      return this;
    }
 
    public ChannelUpdater decUpperName() {
      updateOperations.dec("upperName");
      return this;
    }

    public ChannelUpdater incUpperName() {
      updateOperations.inc("upperName");
      return this;
    }

    public ChannelUpdater incUpperName(Number value) {
      updateOperations.inc("upperName", value);
      return this;
    }
  }
}
