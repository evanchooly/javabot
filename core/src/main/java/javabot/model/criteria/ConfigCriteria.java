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


public class ConfigCriteria {
  private Query<javabot.model.Config> query;
  private Datastore ds;

  public Query<javabot.model.Config> query() {
    return query;
  }

  public ConfigCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Config.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.Integer> historyLength() {
    return new TypeSafeFieldEnd<>(query, query.criteria("historyLength"));
  }

  public ConfigCriteria historyLength(java.lang.Integer value) {
    new TypeSafeFieldEnd<>(query, query.criteria("historyLength")).equal(value);
    return this;
  }

  public ConfigCriteria orderByHistoryLength() {
    return orderByHistoryLength(true);
  }

  public ConfigCriteria orderByHistoryLength(boolean ascending) {
    query.order((!ascending ? "-" : "") + "historyLength");
    return this;
  }

  public ConfigCriteria distinctHistoryLength() {
    ((QueryImpl) query).getCollection().distinct("historyLength");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ConfigCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public ConfigCriteria orderById() {
    return orderById(true);
  }

  public ConfigCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public ConfigCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> nick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("nick"));
  }

  public ConfigCriteria nick(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("nick")).equal(value);
    return this;
  }

  public ConfigCriteria orderByNick() {
    return orderByNick(true);
  }

  public ConfigCriteria orderByNick(boolean ascending) {
    query.order((!ascending ? "-" : "") + "nick");
    return this;
  }

  public ConfigCriteria distinctNick() {
    ((QueryImpl) query).getCollection().distinct("nick");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.util.List<java.lang.String>> operations() {
    return new TypeSafeFieldEnd<>(query, query.criteria("operations"));
  }

  public ConfigCriteria operations(java.util.List<java.lang.String> value) {
    new TypeSafeFieldEnd<>(query, query.criteria("operations")).equal(value);
    return this;
  }

  public ConfigCriteria orderByOperations() {
    return orderByOperations(true);
  }

  public ConfigCriteria orderByOperations(boolean ascending) {
    query.order((!ascending ? "-" : "") + "operations");
    return this;
  }

  public ConfigCriteria distinctOperations() {
    ((QueryImpl) query).getCollection().distinct("operations");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> password() {
    return new TypeSafeFieldEnd<>(query, query.criteria("password"));
  }

  public ConfigCriteria password(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("password")).equal(value);
    return this;
  }

  public ConfigCriteria orderByPassword() {
    return orderByPassword(true);
  }

  public ConfigCriteria orderByPassword(boolean ascending) {
    query.order((!ascending ? "-" : "") + "password");
    return this;
  }

  public ConfigCriteria distinctPassword() {
    ((QueryImpl) query).getCollection().distinct("password");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.Integer> port() {
    return new TypeSafeFieldEnd<>(query, query.criteria("port"));
  }

  public ConfigCriteria port(java.lang.Integer value) {
    new TypeSafeFieldEnd<>(query, query.criteria("port")).equal(value);
    return this;
  }

  public ConfigCriteria orderByPort() {
    return orderByPort(true);
  }

  public ConfigCriteria orderByPort(boolean ascending) {
    query.order((!ascending ? "-" : "") + "port");
    return this;
  }

  public ConfigCriteria distinctPort() {
    ((QueryImpl) query).getCollection().distinct("port");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.Integer> schemaVersion() {
    return new TypeSafeFieldEnd<>(query, query.criteria("schemaVersion"));
  }

  public ConfigCriteria schemaVersion(java.lang.Integer value) {
    new TypeSafeFieldEnd<>(query, query.criteria("schemaVersion")).equal(value);
    return this;
  }

  public ConfigCriteria orderBySchemaVersion() {
    return orderBySchemaVersion(true);
  }

  public ConfigCriteria orderBySchemaVersion(boolean ascending) {
    query.order((!ascending ? "-" : "") + "schemaVersion");
    return this;
  }

  public ConfigCriteria distinctSchemaVersion() {
    ((QueryImpl) query).getCollection().distinct("schemaVersion");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> server() {
    return new TypeSafeFieldEnd<>(query, query.criteria("server"));
  }

  public ConfigCriteria server(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("server")).equal(value);
    return this;
  }

  public ConfigCriteria orderByServer() {
    return orderByServer(true);
  }

  public ConfigCriteria orderByServer(boolean ascending) {
    query.order((!ascending ? "-" : "") + "server");
    return this;
  }

  public ConfigCriteria distinctServer() {
    ((QueryImpl) query).getCollection().distinct("server");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> trigger() {
    return new TypeSafeFieldEnd<>(query, query.criteria("trigger"));
  }

  public ConfigCriteria trigger(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("trigger")).equal(value);
    return this;
  }

  public ConfigCriteria orderByTrigger() {
    return orderByTrigger(true);
  }

  public ConfigCriteria orderByTrigger(boolean ascending) {
    query.order((!ascending ? "-" : "") + "trigger");
    return this;
  }

  public ConfigCriteria distinctTrigger() {
    ((QueryImpl) query).getCollection().distinct("trigger");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Config, java.lang.String> url() {
    return new TypeSafeFieldEnd<>(query, query.criteria("url"));
  }

  public ConfigCriteria url(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("url")).equal(value);
    return this;
  }

  public ConfigCriteria orderByUrl() {
    return orderByUrl(true);
  }

  public ConfigCriteria orderByUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "url");
    return this;
  }

  public ConfigCriteria distinctUrl() {
    ((QueryImpl) query).getCollection().distinct("url");
    return this;
  }

  public ConfigUpdater getUpdater() {
    return new ConfigUpdater();
  }

  public class ConfigUpdater {
    UpdateOperations<javabot.model.Config> updateOperations;

    public ConfigUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.Config.class);
    }

    public UpdateResults<javabot.model.Config> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.Config> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.Config> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.Config> upsert(WriteConcern wc) {
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

    public ConfigUpdater addHistoryLength(String fieldExpr, java.lang.Integer value, boolean addDups) {
      updateOperations.add("historyLength", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToHistoryLength(List<java.lang.Integer> values, boolean addDups) {
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

    public ConfigUpdater removeAllFromHistoryLength(List<java.lang.Integer> values) {
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

    public ConfigUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public ConfigUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
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

    public ConfigUpdater addNick(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("nick", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToNick(List<java.lang.String> values, boolean addDups) {
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

    public ConfigUpdater removeAllFromNick(List<java.lang.String> values) {
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
    public ConfigUpdater operations(java.util.List<java.lang.String> value) {
      updateOperations.set("operations", value);
      return this;
    }

    public ConfigUpdater unsetOperations(java.util.List<java.lang.String> value) {
      updateOperations.unset("operations");
      return this;
    }

    public ConfigUpdater addOperations(java.util.List<java.lang.String> value) {
      updateOperations.add("operations", value);
      return this;
    }

    public ConfigUpdater addOperations(String fieldExpr, java.util.List<java.lang.String> value, boolean addDups) {
      updateOperations.add("operations", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToOperations(List<java.util.List<java.lang.String>> values, boolean addDups) {
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
  
    public ConfigUpdater removeFromOperations(java.util.List<java.lang.String> value) {
      updateOperations.removeAll("operations", value);
      return this;
    }

    public ConfigUpdater removeAllFromOperations(List<java.util.List<java.lang.String>> values) {
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

    public ConfigUpdater addPassword(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("password", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToPassword(List<java.lang.String> values, boolean addDups) {
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

    public ConfigUpdater removeAllFromPassword(List<java.lang.String> values) {
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

    public ConfigUpdater addPort(String fieldExpr, java.lang.Integer value, boolean addDups) {
      updateOperations.add("port", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToPort(List<java.lang.Integer> values, boolean addDups) {
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

    public ConfigUpdater removeAllFromPort(List<java.lang.Integer> values) {
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

    public ConfigUpdater addSchemaVersion(String fieldExpr, java.lang.Integer value, boolean addDups) {
      updateOperations.add("schemaVersion", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToSchemaVersion(List<java.lang.Integer> values, boolean addDups) {
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

    public ConfigUpdater removeAllFromSchemaVersion(List<java.lang.Integer> values) {
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

    public ConfigUpdater addServer(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("server", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToServer(List<java.lang.String> values, boolean addDups) {
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

    public ConfigUpdater removeAllFromServer(List<java.lang.String> values) {
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

    public ConfigUpdater addTrigger(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("trigger", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToTrigger(List<java.lang.String> values, boolean addDups) {
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

    public ConfigUpdater removeAllFromTrigger(List<java.lang.String> values) {
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

    public ConfigUpdater addUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("url", value, addDups);
      return this;
    }

    public ConfigUpdater addAllToUrl(List<java.lang.String> values, boolean addDups) {
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

    public ConfigUpdater removeAllFromUrl(List<java.lang.String> values) {
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
