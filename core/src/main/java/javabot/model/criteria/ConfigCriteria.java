package javabot.model.criteria;

import javabot.model.Config;

public class ConfigCriteria extends com.antwerkz.critter.criteria.BaseCriteria<Config> {
  private String prefix = "";

  public ConfigCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, Config.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer> historyLength() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer>(this, query, prefix + "historyLength");
  }

  public org.mongodb.morphia.query.Criteria historyLength(java.lang.Integer value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer>(this, query, prefix + "historyLength").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer> minimumNickServAge() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer>(this, query, prefix + "minimumNickServAge");
  }

  public org.mongodb.morphia.query.Criteria minimumNickServAge(java.lang.Integer value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer>(this, query, prefix + "minimumNickServAge").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String> nick() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String>(this, query, prefix + "nick");
  }

  public org.mongodb.morphia.query.Criteria nick(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String>(this, query, prefix + "nick").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.util.Set<java.lang.String>> operations() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.util.Set<java.lang.String>>(this, query, prefix + "operations");
  }

  public org.mongodb.morphia.query.Criteria operations(java.util.Set<java.lang.String> value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.util.Set<java.lang.String>>(this, query, prefix + "operations").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String> password() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String>(this, query, prefix + "password");
  }

  public org.mongodb.morphia.query.Criteria password(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String>(this, query, prefix + "password").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer> port() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer>(this, query, prefix + "port");
  }

  public org.mongodb.morphia.query.Criteria port(java.lang.Integer value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer>(this, query, prefix + "port").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer> schemaVersion() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer>(this, query, prefix + "schemaVersion");
  }

  public org.mongodb.morphia.query.Criteria schemaVersion(java.lang.Integer value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer>(this, query, prefix + "schemaVersion").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String> server() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String>(this, query, prefix + "server");
  }

  public org.mongodb.morphia.query.Criteria server(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String>(this, query, prefix + "server").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer> throttleThreshold() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer>(this, query, prefix + "throttleThreshold");
  }

  public org.mongodb.morphia.query.Criteria throttleThreshold(java.lang.Integer value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.Integer>(this, query, prefix + "throttleThreshold").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String> trigger() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String>(this, query, prefix + "trigger");
  }

  public org.mongodb.morphia.query.Criteria trigger(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String>(this, query, prefix + "trigger").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String> url() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String>(this, query, prefix + "url");
  }

  public org.mongodb.morphia.query.Criteria url(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<ConfigCriteria, Config, java.lang.String>(this, query, prefix + "url").equal(value);
  }


  public ConfigUpdater getUpdater() {
    return new ConfigUpdater();
  }

  public class ConfigUpdater {
    org.mongodb.morphia.query.UpdateOperations<Config> updateOperations;

    public ConfigUpdater() {
      updateOperations = ds.createUpdateOperations(Config.class);
    }

    public org.mongodb.morphia.query.UpdateResults<Config> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<Config> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<Config> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<Config> upsert(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public ConfigUpdater historyLength(java.lang.Integer value) {
      updateOperations.set("historyLength", value);
      return this;
    }

    public ConfigUpdater unsetHistoryLength(java.lang.Integer value) {
      updateOperations.unset("historyLength");
      return this;
    }

    public ConfigUpdater addHistoryLength(java.lang.Integer value) {
      updateOperations.add("historyLength", value);
      return this;
    }

    public ConfigUpdater addHistoryLength(java.lang.Integer value, boolean addDups) {
      updateOperations.add("historyLength", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToHistoryLength(java.util.List<java.lang.Integer> values, boolean addDups) {
      updateOperations.addAll("historyLength", values, addDups);
      return this;
    }
  
    public ConfigUpdater removeFirstHistoryLength() {
      updateOperations.removeFirst("historyLength");
      return this;
    }
  
    public ConfigUpdater removeLastHistoryLength() {
      updateOperations.removeLast("historyLength");
      return this;
    }
  
    public ConfigUpdater removeFromHistoryLength(java.lang.Integer value) {
      updateOperations.removeAll("historyLength", value);
      return this;
    }

    public ConfigUpdater removeAllFromHistoryLength(java.util.List<java.lang.Integer> values) {
      updateOperations.removeAll("historyLength", values);
      return this;
    }
 
    public ConfigUpdater decHistoryLength() {
      updateOperations.dec("historyLength");
      return this;
    }

    public ConfigUpdater incHistoryLength() {
      updateOperations.inc("historyLength");
      return this;
    }

    public ConfigUpdater incHistoryLength(Number value) {
      updateOperations.inc("historyLength", value);
      return this;
    }
    public ConfigUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public ConfigUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public ConfigUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public ConfigUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public ConfigUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public ConfigUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public ConfigUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public ConfigUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public ConfigUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public ConfigUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public ConfigUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public ConfigUpdater minimumNickServAge(java.lang.Integer value) {
      updateOperations.set("minimumNickServAge", value);
      return this;
    }

    public ConfigUpdater unsetMinimumNickServAge(java.lang.Integer value) {
      updateOperations.unset("minimumNickServAge");
      return this;
    }

    public ConfigUpdater addMinimumNickServAge(java.lang.Integer value) {
      updateOperations.add("minimumNickServAge", value);
      return this;
    }

    public ConfigUpdater addMinimumNickServAge(java.lang.Integer value, boolean addDups) {
      updateOperations.add("minimumNickServAge", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToMinimumNickServAge(java.util.List<java.lang.Integer> values, boolean addDups) {
      updateOperations.addAll("minimumNickServAge", values, addDups);
      return this;
    }
  
    public ConfigUpdater removeFirstMinimumNickServAge() {
      updateOperations.removeFirst("minimumNickServAge");
      return this;
    }
  
    public ConfigUpdater removeLastMinimumNickServAge() {
      updateOperations.removeLast("minimumNickServAge");
      return this;
    }
  
    public ConfigUpdater removeFromMinimumNickServAge(java.lang.Integer value) {
      updateOperations.removeAll("minimumNickServAge", value);
      return this;
    }

    public ConfigUpdater removeAllFromMinimumNickServAge(java.util.List<java.lang.Integer> values) {
      updateOperations.removeAll("minimumNickServAge", values);
      return this;
    }
 
    public ConfigUpdater decMinimumNickServAge() {
      updateOperations.dec("minimumNickServAge");
      return this;
    }

    public ConfigUpdater incMinimumNickServAge() {
      updateOperations.inc("minimumNickServAge");
      return this;
    }

    public ConfigUpdater incMinimumNickServAge(Number value) {
      updateOperations.inc("minimumNickServAge", value);
      return this;
    }
    public ConfigUpdater nick(java.lang.String value) {
      updateOperations.set("nick", value);
      return this;
    }

    public ConfigUpdater unsetNick(java.lang.String value) {
      updateOperations.unset("nick");
      return this;
    }

    public ConfigUpdater addNick(java.lang.String value) {
      updateOperations.add("nick", value);
      return this;
    }

    public ConfigUpdater addNick(java.lang.String value, boolean addDups) {
      updateOperations.add("nick", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToNick(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("nick", values, addDups);
      return this;
    }
  
    public ConfigUpdater removeFirstNick() {
      updateOperations.removeFirst("nick");
      return this;
    }
  
    public ConfigUpdater removeLastNick() {
      updateOperations.removeLast("nick");
      return this;
    }
  
    public ConfigUpdater removeFromNick(java.lang.String value) {
      updateOperations.removeAll("nick", value);
      return this;
    }

    public ConfigUpdater removeAllFromNick(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("nick", values);
      return this;
    }
 
    public ConfigUpdater decNick() {
      updateOperations.dec("nick");
      return this;
    }

    public ConfigUpdater incNick() {
      updateOperations.inc("nick");
      return this;
    }

    public ConfigUpdater incNick(Number value) {
      updateOperations.inc("nick", value);
      return this;
    }
    public ConfigUpdater operations(java.util.Set<java.lang.String> value) {
      updateOperations.set("operations", value);
      return this;
    }

    public ConfigUpdater unsetOperations(java.util.Set<java.lang.String> value) {
      updateOperations.unset("operations");
      return this;
    }

    public ConfigUpdater addOperations(java.util.Set<java.lang.String> value) {
      updateOperations.add("operations", value);
      return this;
    }

    public ConfigUpdater addOperations(java.util.Set<java.lang.String> value, boolean addDups) {
      updateOperations.add("operations", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToOperations(java.util.List<java.util.Set<java.lang.String>> values, boolean addDups) {
      updateOperations.addAll("operations", values, addDups);
      return this;
    }
  
    public ConfigUpdater removeFirstOperations() {
      updateOperations.removeFirst("operations");
      return this;
    }
  
    public ConfigUpdater removeLastOperations() {
      updateOperations.removeLast("operations");
      return this;
    }
  
    public ConfigUpdater removeFromOperations(java.util.Set<java.lang.String> value) {
      updateOperations.removeAll("operations", value);
      return this;
    }

    public ConfigUpdater removeAllFromOperations(java.util.List<java.util.Set<java.lang.String>> values) {
      updateOperations.removeAll("operations", values);
      return this;
    }
 
    public ConfigUpdater decOperations() {
      updateOperations.dec("operations");
      return this;
    }

    public ConfigUpdater incOperations() {
      updateOperations.inc("operations");
      return this;
    }

    public ConfigUpdater incOperations(Number value) {
      updateOperations.inc("operations", value);
      return this;
    }
    public ConfigUpdater password(java.lang.String value) {
      updateOperations.set("password", value);
      return this;
    }

    public ConfigUpdater unsetPassword(java.lang.String value) {
      updateOperations.unset("password");
      return this;
    }

    public ConfigUpdater addPassword(java.lang.String value) {
      updateOperations.add("password", value);
      return this;
    }

    public ConfigUpdater addPassword(java.lang.String value, boolean addDups) {
      updateOperations.add("password", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToPassword(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("password", values, addDups);
      return this;
    }
  
    public ConfigUpdater removeFirstPassword() {
      updateOperations.removeFirst("password");
      return this;
    }
  
    public ConfigUpdater removeLastPassword() {
      updateOperations.removeLast("password");
      return this;
    }
  
    public ConfigUpdater removeFromPassword(java.lang.String value) {
      updateOperations.removeAll("password", value);
      return this;
    }

    public ConfigUpdater removeAllFromPassword(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("password", values);
      return this;
    }
 
    public ConfigUpdater decPassword() {
      updateOperations.dec("password");
      return this;
    }

    public ConfigUpdater incPassword() {
      updateOperations.inc("password");
      return this;
    }

    public ConfigUpdater incPassword(Number value) {
      updateOperations.inc("password", value);
      return this;
    }
    public ConfigUpdater port(java.lang.Integer value) {
      updateOperations.set("port", value);
      return this;
    }

    public ConfigUpdater unsetPort(java.lang.Integer value) {
      updateOperations.unset("port");
      return this;
    }

    public ConfigUpdater addPort(java.lang.Integer value) {
      updateOperations.add("port", value);
      return this;
    }

    public ConfigUpdater addPort(java.lang.Integer value, boolean addDups) {
      updateOperations.add("port", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToPort(java.util.List<java.lang.Integer> values, boolean addDups) {
      updateOperations.addAll("port", values, addDups);
      return this;
    }
  
    public ConfigUpdater removeFirstPort() {
      updateOperations.removeFirst("port");
      return this;
    }
  
    public ConfigUpdater removeLastPort() {
      updateOperations.removeLast("port");
      return this;
    }
  
    public ConfigUpdater removeFromPort(java.lang.Integer value) {
      updateOperations.removeAll("port", value);
      return this;
    }

    public ConfigUpdater removeAllFromPort(java.util.List<java.lang.Integer> values) {
      updateOperations.removeAll("port", values);
      return this;
    }
 
    public ConfigUpdater decPort() {
      updateOperations.dec("port");
      return this;
    }

    public ConfigUpdater incPort() {
      updateOperations.inc("port");
      return this;
    }

    public ConfigUpdater incPort(Number value) {
      updateOperations.inc("port", value);
      return this;
    }
    public ConfigUpdater schemaVersion(java.lang.Integer value) {
      updateOperations.set("schemaVersion", value);
      return this;
    }

    public ConfigUpdater unsetSchemaVersion(java.lang.Integer value) {
      updateOperations.unset("schemaVersion");
      return this;
    }

    public ConfigUpdater addSchemaVersion(java.lang.Integer value) {
      updateOperations.add("schemaVersion", value);
      return this;
    }

    public ConfigUpdater addSchemaVersion(java.lang.Integer value, boolean addDups) {
      updateOperations.add("schemaVersion", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToSchemaVersion(java.util.List<java.lang.Integer> values, boolean addDups) {
      updateOperations.addAll("schemaVersion", values, addDups);
      return this;
    }
  
    public ConfigUpdater removeFirstSchemaVersion() {
      updateOperations.removeFirst("schemaVersion");
      return this;
    }
  
    public ConfigUpdater removeLastSchemaVersion() {
      updateOperations.removeLast("schemaVersion");
      return this;
    }
  
    public ConfigUpdater removeFromSchemaVersion(java.lang.Integer value) {
      updateOperations.removeAll("schemaVersion", value);
      return this;
    }

    public ConfigUpdater removeAllFromSchemaVersion(java.util.List<java.lang.Integer> values) {
      updateOperations.removeAll("schemaVersion", values);
      return this;
    }
 
    public ConfigUpdater decSchemaVersion() {
      updateOperations.dec("schemaVersion");
      return this;
    }

    public ConfigUpdater incSchemaVersion() {
      updateOperations.inc("schemaVersion");
      return this;
    }

    public ConfigUpdater incSchemaVersion(Number value) {
      updateOperations.inc("schemaVersion", value);
      return this;
    }
    public ConfigUpdater server(java.lang.String value) {
      updateOperations.set("server", value);
      return this;
    }

    public ConfigUpdater unsetServer(java.lang.String value) {
      updateOperations.unset("server");
      return this;
    }

    public ConfigUpdater addServer(java.lang.String value) {
      updateOperations.add("server", value);
      return this;
    }

    public ConfigUpdater addServer(java.lang.String value, boolean addDups) {
      updateOperations.add("server", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToServer(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("server", values, addDups);
      return this;
    }
  
    public ConfigUpdater removeFirstServer() {
      updateOperations.removeFirst("server");
      return this;
    }
  
    public ConfigUpdater removeLastServer() {
      updateOperations.removeLast("server");
      return this;
    }
  
    public ConfigUpdater removeFromServer(java.lang.String value) {
      updateOperations.removeAll("server", value);
      return this;
    }

    public ConfigUpdater removeAllFromServer(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("server", values);
      return this;
    }
 
    public ConfigUpdater decServer() {
      updateOperations.dec("server");
      return this;
    }

    public ConfigUpdater incServer() {
      updateOperations.inc("server");
      return this;
    }

    public ConfigUpdater incServer(Number value) {
      updateOperations.inc("server", value);
      return this;
    }
    public ConfigUpdater throttleThreshold(java.lang.Integer value) {
      updateOperations.set("throttleThreshold", value);
      return this;
    }

    public ConfigUpdater unsetThrottleThreshold(java.lang.Integer value) {
      updateOperations.unset("throttleThreshold");
      return this;
    }

    public ConfigUpdater addThrottleThreshold(java.lang.Integer value) {
      updateOperations.add("throttleThreshold", value);
      return this;
    }

    public ConfigUpdater addThrottleThreshold(java.lang.Integer value, boolean addDups) {
      updateOperations.add("throttleThreshold", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToThrottleThreshold(java.util.List<java.lang.Integer> values, boolean addDups) {
      updateOperations.addAll("throttleThreshold", values, addDups);
      return this;
    }
  
    public ConfigUpdater removeFirstThrottleThreshold() {
      updateOperations.removeFirst("throttleThreshold");
      return this;
    }
  
    public ConfigUpdater removeLastThrottleThreshold() {
      updateOperations.removeLast("throttleThreshold");
      return this;
    }
  
    public ConfigUpdater removeFromThrottleThreshold(java.lang.Integer value) {
      updateOperations.removeAll("throttleThreshold", value);
      return this;
    }

    public ConfigUpdater removeAllFromThrottleThreshold(java.util.List<java.lang.Integer> values) {
      updateOperations.removeAll("throttleThreshold", values);
      return this;
    }
 
    public ConfigUpdater decThrottleThreshold() {
      updateOperations.dec("throttleThreshold");
      return this;
    }

    public ConfigUpdater incThrottleThreshold() {
      updateOperations.inc("throttleThreshold");
      return this;
    }

    public ConfigUpdater incThrottleThreshold(Number value) {
      updateOperations.inc("throttleThreshold", value);
      return this;
    }
    public ConfigUpdater trigger(java.lang.String value) {
      updateOperations.set("trigger", value);
      return this;
    }

    public ConfigUpdater unsetTrigger(java.lang.String value) {
      updateOperations.unset("trigger");
      return this;
    }

    public ConfigUpdater addTrigger(java.lang.String value) {
      updateOperations.add("trigger", value);
      return this;
    }

    public ConfigUpdater addTrigger(java.lang.String value, boolean addDups) {
      updateOperations.add("trigger", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToTrigger(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("trigger", values, addDups);
      return this;
    }
  
    public ConfigUpdater removeFirstTrigger() {
      updateOperations.removeFirst("trigger");
      return this;
    }
  
    public ConfigUpdater removeLastTrigger() {
      updateOperations.removeLast("trigger");
      return this;
    }
  
    public ConfigUpdater removeFromTrigger(java.lang.String value) {
      updateOperations.removeAll("trigger", value);
      return this;
    }

    public ConfigUpdater removeAllFromTrigger(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("trigger", values);
      return this;
    }
 
    public ConfigUpdater decTrigger() {
      updateOperations.dec("trigger");
      return this;
    }

    public ConfigUpdater incTrigger() {
      updateOperations.inc("trigger");
      return this;
    }

    public ConfigUpdater incTrigger(Number value) {
      updateOperations.inc("trigger", value);
      return this;
    }
    public ConfigUpdater url(java.lang.String value) {
      updateOperations.set("url", value);
      return this;
    }

    public ConfigUpdater unsetUrl(java.lang.String value) {
      updateOperations.unset("url");
      return this;
    }

    public ConfigUpdater addUrl(java.lang.String value) {
      updateOperations.add("url", value);
      return this;
    }

    public ConfigUpdater addUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("url", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToUrl(java.util.List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("url", values, addDups);
      return this;
    }
  
    public ConfigUpdater removeFirstUrl() {
      updateOperations.removeFirst("url");
      return this;
    }
  
    public ConfigUpdater removeLastUrl() {
      updateOperations.removeLast("url");
      return this;
    }
  
    public ConfigUpdater removeFromUrl(java.lang.String value) {
      updateOperations.removeAll("url", value);
      return this;
    }

    public ConfigUpdater removeAllFromUrl(java.util.List<java.lang.String> values) {
      updateOperations.removeAll("url", values);
      return this;
    }
 
    public ConfigUpdater decUrl() {
      updateOperations.dec("url");
      return this;
    }

    public ConfigUpdater incUrl() {
      updateOperations.inc("url");
      return this;
    }

    public ConfigUpdater incUrl(Number value) {
      updateOperations.inc("url", value);
      return this;
    }
  }
}
