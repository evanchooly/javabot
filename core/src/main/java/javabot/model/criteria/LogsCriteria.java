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


public class LogsCriteria {
  private Query<javabot.model.Logs> query;
  private Datastore ds;

  public Query<javabot.model.Logs> query() {
    return query;
  }

  public LogsCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Logs.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, java.lang.String> channel() {
    return new TypeSafeFieldEnd<>(query, query.criteria("channel"));
  }

  public LogsCriteria channel(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("channel")).equal(value);
    return this;
  }

  public LogsCriteria orderByChannel() {
    return orderByChannel(true);
  }

  public LogsCriteria orderByChannel(boolean ascending) {
    query.order((!ascending ? "-" : "") + "channel");
    return this;
  }

  public LogsCriteria distinctChannel() {
    ((QueryImpl) query).getCollection().distinct("channel");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public LogsCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public LogsCriteria orderById() {
    return orderById(true);
  }

  public LogsCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public LogsCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, java.lang.String> message() {
    return new TypeSafeFieldEnd<>(query, query.criteria("message"));
  }

  public LogsCriteria message(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("message")).equal(value);
    return this;
  }

  public LogsCriteria orderByMessage() {
    return orderByMessage(true);
  }

  public LogsCriteria orderByMessage(boolean ascending) {
    query.order((!ascending ? "-" : "") + "message");
    return this;
  }

  public LogsCriteria distinctMessage() {
    ((QueryImpl) query).getCollection().distinct("message");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, java.lang.String> nick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("nick"));
  }

  public LogsCriteria nick(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("nick")).equal(value);
    return this;
  }

  public LogsCriteria orderByNick() {
    return orderByNick(true);
  }

  public LogsCriteria orderByNick(boolean ascending) {
    query.order((!ascending ? "-" : "") + "nick");
    return this;
  }

  public LogsCriteria distinctNick() {
    ((QueryImpl) query).getCollection().distinct("nick");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, javabot.model.Logs.Type> type() {
    return new TypeSafeFieldEnd<>(query, query.criteria("type"));
  }

  public LogsCriteria type(javabot.model.Logs.Type value) {
    new TypeSafeFieldEnd<>(query, query.criteria("type")).equal(value);
    return this;
  }

  public LogsCriteria orderByType() {
    return orderByType(true);
  }

  public LogsCriteria orderByType(boolean ascending) {
    query.order((!ascending ? "-" : "") + "type");
    return this;
  }

  public LogsCriteria distinctType() {
    ((QueryImpl) query).getCollection().distinct("type");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, org.joda.time.DateTime> updated() {
    return new TypeSafeFieldEnd<>(query, query.criteria("updated"));
  }

  public LogsCriteria updated(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("updated")).equal(value);
    return this;
  }

  public LogsCriteria orderByUpdated() {
    return orderByUpdated(true);
  }

  public LogsCriteria orderByUpdated(boolean ascending) {
    query.order((!ascending ? "-" : "") + "updated");
    return this;
  }

  public LogsCriteria distinctUpdated() {
    ((QueryImpl) query).getCollection().distinct("updated");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Logs, java.lang.String> upperNick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("upperNick"));
  }

  public LogsCriteria upperNick(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("upperNick")).equal(value);
    return this;
  }

  public LogsCriteria orderByUpperNick() {
    return orderByUpperNick(true);
  }

  public LogsCriteria orderByUpperNick(boolean ascending) {
    query.order((!ascending ? "-" : "") + "upperNick");
    return this;
  }

  public LogsCriteria distinctUpperNick() {
    ((QueryImpl) query).getCollection().distinct("upperNick");
    return this;
  }

  public LogsUpdater getUpdater() {
    return new LogsUpdater();
  }

  public class LogsUpdater {
    UpdateOperations<javabot.model.Logs> updateOperations;

    public LogsUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.Logs.class);
    }

    public UpdateResults<javabot.model.Logs> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.Logs> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.Logs> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.Logs> upsert(WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public LogsUpdater channel(java.lang.String value) {
      updateOperations.set("channel", value);
      return this;
    }

    public LogsUpdater unsetChannel(java.lang.String value) {
      updateOperations.unset("channel");
      return this;
    }

    public LogsUpdater addChannel(java.lang.String value) {
      updateOperations.add("channel", value);
      return this;
    }

    public LogsUpdater addChannel(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("channel", value, addDups);
      return this;
    }

    public LogsUpdater addAllToChannel(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("channel", values, addDups);
      return this;
    }
  
    public LogsUpdater removeFirstChannel() {
      updateOperations.removeFirst("channel");
      return this;
    }
  
    public LogsUpdater removeLastChannel() {
      updateOperations.removeLast("channel");
      return this;
    }
  
    public LogsUpdater removeFromChannel(java.lang.String value) {
      updateOperations.removeAll("channel", value);
      return this;
    }

    public LogsUpdater removeAllFromChannel(List<java.lang.String> values) {
      updateOperations.removeAll("channel", values);
      return this;
    }
 
    public LogsUpdater decChannel() {
      updateOperations.dec("channel");
      return this;
    }

    public LogsUpdater incChannel() {
      updateOperations.inc("channel");
      return this;
    }

    public LogsUpdater incChannel(Number value) {
      updateOperations.inc("channel", value);
      return this;
    }
    public LogsUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public LogsUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public LogsUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public LogsUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public LogsUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public LogsUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public LogsUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public LogsUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public LogsUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public LogsUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public LogsUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public LogsUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public LogsUpdater message(java.lang.String value) {
      updateOperations.set("message", value);
      return this;
    }

    public LogsUpdater unsetMessage(java.lang.String value) {
      updateOperations.unset("message");
      return this;
    }

    public LogsUpdater addMessage(java.lang.String value) {
      updateOperations.add("message", value);
      return this;
    }

    public LogsUpdater addMessage(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("message", value, addDups);
      return this;
    }

    public LogsUpdater addAllToMessage(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("message", values, addDups);
      return this;
    }
  
    public LogsUpdater removeFirstMessage() {
      updateOperations.removeFirst("message");
      return this;
    }
  
    public LogsUpdater removeLastMessage() {
      updateOperations.removeLast("message");
      return this;
    }
  
    public LogsUpdater removeFromMessage(java.lang.String value) {
      updateOperations.removeAll("message", value);
      return this;
    }

    public LogsUpdater removeAllFromMessage(List<java.lang.String> values) {
      updateOperations.removeAll("message", values);
      return this;
    }
 
    public LogsUpdater decMessage() {
      updateOperations.dec("message");
      return this;
    }

    public LogsUpdater incMessage() {
      updateOperations.inc("message");
      return this;
    }

    public LogsUpdater incMessage(Number value) {
      updateOperations.inc("message", value);
      return this;
    }
    public LogsUpdater nick(java.lang.String value) {
      updateOperations.set("nick", value);
      return this;
    }

    public LogsUpdater unsetNick(java.lang.String value) {
      updateOperations.unset("nick");
      return this;
    }

    public LogsUpdater addNick(java.lang.String value) {
      updateOperations.add("nick", value);
      return this;
    }

    public LogsUpdater addNick(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("nick", value, addDups);
      return this;
    }

    public LogsUpdater addAllToNick(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("nick", values, addDups);
      return this;
    }
  
    public LogsUpdater removeFirstNick() {
      updateOperations.removeFirst("nick");
      return this;
    }
  
    public LogsUpdater removeLastNick() {
      updateOperations.removeLast("nick");
      return this;
    }
  
    public LogsUpdater removeFromNick(java.lang.String value) {
      updateOperations.removeAll("nick", value);
      return this;
    }

    public LogsUpdater removeAllFromNick(List<java.lang.String> values) {
      updateOperations.removeAll("nick", values);
      return this;
    }
 
    public LogsUpdater decNick() {
      updateOperations.dec("nick");
      return this;
    }

    public LogsUpdater incNick() {
      updateOperations.inc("nick");
      return this;
    }

    public LogsUpdater incNick(Number value) {
      updateOperations.inc("nick", value);
      return this;
    }
    public LogsUpdater type(javabot.model.Logs.Type value) {
      updateOperations.set("type", value);
      return this;
    }

    public LogsUpdater unsetType(javabot.model.Logs.Type value) {
      updateOperations.unset("type");
      return this;
    }

    public LogsUpdater addType(javabot.model.Logs.Type value) {
      updateOperations.add("type", value);
      return this;
    }

    public LogsUpdater addType(String fieldExpr, javabot.model.Logs.Type value, boolean addDups) {
      updateOperations.add("type", value, addDups);
      return this;
    }

    public LogsUpdater addAllToType(List<javabot.model.Logs.Type> values, boolean addDups) {
      updateOperations.addAll("type", values, addDups);
      return this;
    }
  
    public LogsUpdater removeFirstType() {
      updateOperations.removeFirst("type");
      return this;
    }
  
    public LogsUpdater removeLastType() {
      updateOperations.removeLast("type");
      return this;
    }
  
    public LogsUpdater removeFromType(javabot.model.Logs.Type value) {
      updateOperations.removeAll("type", value);
      return this;
    }

    public LogsUpdater removeAllFromType(List<javabot.model.Logs.Type> values) {
      updateOperations.removeAll("type", values);
      return this;
    }
 
    public LogsUpdater decType() {
      updateOperations.dec("type");
      return this;
    }

    public LogsUpdater incType() {
      updateOperations.inc("type");
      return this;
    }

    public LogsUpdater incType(Number value) {
      updateOperations.inc("type", value);
      return this;
    }
    public LogsUpdater updated(org.joda.time.DateTime value) {
      updateOperations.set("updated", value);
      return this;
    }

    public LogsUpdater unsetUpdated(org.joda.time.DateTime value) {
      updateOperations.unset("updated");
      return this;
    }

    public LogsUpdater addUpdated(org.joda.time.DateTime value) {
      updateOperations.add("updated", value);
      return this;
    }

    public LogsUpdater addUpdated(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("updated", value, addDups);
      return this;
    }

    public LogsUpdater addAllToUpdated(List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("updated", values, addDups);
      return this;
    }
  
    public LogsUpdater removeFirstUpdated() {
      updateOperations.removeFirst("updated");
      return this;
    }
  
    public LogsUpdater removeLastUpdated() {
      updateOperations.removeLast("updated");
      return this;
    }
  
    public LogsUpdater removeFromUpdated(org.joda.time.DateTime value) {
      updateOperations.removeAll("updated", value);
      return this;
    }

    public LogsUpdater removeAllFromUpdated(List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("updated", values);
      return this;
    }
 
    public LogsUpdater decUpdated() {
      updateOperations.dec("updated");
      return this;
    }

    public LogsUpdater incUpdated() {
      updateOperations.inc("updated");
      return this;
    }

    public LogsUpdater incUpdated(Number value) {
      updateOperations.inc("updated", value);
      return this;
    }
    public LogsUpdater upperNick(java.lang.String value) {
      updateOperations.set("upperNick", value);
      return this;
    }

    public LogsUpdater unsetUpperNick(java.lang.String value) {
      updateOperations.unset("upperNick");
      return this;
    }

    public LogsUpdater addUpperNick(java.lang.String value) {
      updateOperations.add("upperNick", value);
      return this;
    }

    public LogsUpdater addUpperNick(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("upperNick", value, addDups);
      return this;
    }

    public LogsUpdater addAllToUpperNick(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("upperNick", values, addDups);
      return this;
    }
  
    public LogsUpdater removeFirstUpperNick() {
      updateOperations.removeFirst("upperNick");
      return this;
    }
  
    public LogsUpdater removeLastUpperNick() {
      updateOperations.removeLast("upperNick");
      return this;
    }
  
    public LogsUpdater removeFromUpperNick(java.lang.String value) {
      updateOperations.removeAll("upperNick", value);
      return this;
    }

    public LogsUpdater removeAllFromUpperNick(List<java.lang.String> values) {
      updateOperations.removeAll("upperNick", values);
      return this;
    }
 
    public LogsUpdater decUpperNick() {
      updateOperations.dec("upperNick");
      return this;
    }

    public LogsUpdater incUpperNick() {
      updateOperations.inc("upperNick");
      return this;
    }

    public LogsUpdater incUpperNick(Number value) {
      updateOperations.inc("upperNick", value);
      return this;
    }
  }
}
