package javabot.model.criteria;

import javabot.model.Logs;

public class LogsCriteria extends com.antwerkz.critter.criteria.BaseCriteria<Logs> {
  private String prefix = "";

  public LogsCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, Logs.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.lang.String> channel() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.lang.String>(this, query, prefix + "channel");
  }

  public org.mongodb.morphia.query.Criteria channel(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.lang.String>(this, query, prefix + "channel").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.lang.String> message() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.lang.String>(this, query, prefix + "message");
  }

  public org.mongodb.morphia.query.Criteria message(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.lang.String>(this, query, prefix + "message").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.lang.String> nick() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.lang.String>(this, query, prefix + "nick");
  }

  public org.mongodb.morphia.query.Criteria nick(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.lang.String>(this, query, prefix + "nick").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, javabot.model.Logs.Type> type() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, javabot.model.Logs.Type>(this, query, prefix + "type");
  }

  public org.mongodb.morphia.query.Criteria type(javabot.model.Logs.Type value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, javabot.model.Logs.Type>(this, query, prefix + "type").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.time.LocalDateTime> updated() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.time.LocalDateTime>(this, query, prefix + "updated");
  }

  public org.mongodb.morphia.query.Criteria updated(java.time.LocalDateTime value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.time.LocalDateTime>(this, query, prefix + "updated").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.lang.String> upperNick() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.lang.String>(this, query, prefix + "upperNick");
  }

  public org.mongodb.morphia.query.Criteria upperNick(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<LogsCriteria, Logs, java.lang.String>(this, query, prefix + "upperNick").equal(value);
  }


  public LogsUpdater getUpdater() {
    return new LogsUpdater();
  }

  public class LogsUpdater {
    org.mongodb.morphia.query.UpdateOperations<Logs> updateOperations;

    public LogsUpdater() {
      updateOperations = ds.createUpdateOperations(Logs.class);
    }

    public org.mongodb.morphia.query.UpdateResults<Logs> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<Logs> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<Logs> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<Logs> upsert(com.mongodb.WriteConcern wc) {
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

    public LogsUpdater addChannel(java.lang.String value, boolean addDups) {
      updateOperations.add("channel", value, addDups);
      return this;
    }

    public LogsUpdater addAllToChannel(java.util.List<java.lang.String> values, boolean addDups) {
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

    public LogsUpdater removeAllFromChannel(java.util.List<java.lang.String> values) {
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

    public LogsUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public LogsUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public LogsUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
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

    public LogsUpdater addMessage(java.lang.String value, boolean addDups) {
      updateOperations.add("message", value, addDups);
      return this;
    }

    public LogsUpdater addAllToMessage(java.util.List<java.lang.String> values, boolean addDups) {
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

    public LogsUpdater removeAllFromMessage(java.util.List<java.lang.String> values) {
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

    public LogsUpdater addNick(java.lang.String value, boolean addDups) {
      updateOperations.add("nick", value, addDups);
      return this;
    }

    public LogsUpdater addAllToNick(java.util.List<java.lang.String> values, boolean addDups) {
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

    public LogsUpdater removeAllFromNick(java.util.List<java.lang.String> values) {
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

    public LogsUpdater addType(javabot.model.Logs.Type value, boolean addDups) {
      updateOperations.add("type", value, addDups);
      return this;
    }

    public LogsUpdater addAllToType(java.util.List<javabot.model.Logs.Type> values, boolean addDups) {
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

    public LogsUpdater removeAllFromType(java.util.List<javabot.model.Logs.Type> values) {
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
    public LogsUpdater updated(java.time.LocalDateTime value) {
      updateOperations.set("updated", value);
      return this;
    }

    public LogsUpdater unsetUpdated(java.time.LocalDateTime value) {
      updateOperations.unset("updated");
      return this;
    }

    public LogsUpdater addUpdated(java.time.LocalDateTime value) {
      updateOperations.add("updated", value);
      return this;
    }

    public LogsUpdater addUpdated(java.time.LocalDateTime value, boolean addDups) {
      updateOperations.add("updated", value, addDups);
      return this;
    }

    public LogsUpdater addAllToUpdated(java.util.List<java.time.LocalDateTime> values, boolean addDups) {
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
  
    public LogsUpdater removeFromUpdated(java.time.LocalDateTime value) {
      updateOperations.removeAll("updated", value);
      return this;
    }

    public LogsUpdater removeAllFromUpdated(java.util.List<java.time.LocalDateTime> values) {
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

    public LogsUpdater addUpperNick(java.lang.String value, boolean addDups) {
      updateOperations.add("upperNick", value, addDups);
      return this;
    }

    public LogsUpdater addAllToUpperNick(java.util.List<java.lang.String> values, boolean addDups) {
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

    public LogsUpdater removeAllFromUpperNick(java.util.List<java.lang.String> values) {
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
