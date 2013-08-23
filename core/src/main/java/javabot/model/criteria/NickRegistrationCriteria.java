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


public class NickRegistrationCriteria {
  private Query<javabot.model.NickRegistration> query;
  private Datastore ds;

  public Query<javabot.model.NickRegistration> query() {
    return query;
  }

  public NickRegistrationCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.NickRegistration.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, java.lang.String> host() {
    return new TypeSafeFieldEnd<>(query, query.criteria("host"));
  }

  public NickRegistrationCriteria host(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("host")).equal(value);
    return this;
  }

  public NickRegistrationCriteria orderByHost() {
    return orderByHost(true);
  }

  public NickRegistrationCriteria orderByHost(boolean ascending) {
    query.order((!ascending ? "-" : "") + "host");
    return this;
  }

  public NickRegistrationCriteria distinctHost() {
    ((QueryImpl) query).getCollection().distinct("host");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public NickRegistrationCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public NickRegistrationCriteria orderById() {
    return orderById(true);
  }

  public NickRegistrationCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public NickRegistrationCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, java.lang.String> nick() {
    return new TypeSafeFieldEnd<>(query, query.criteria("nick"));
  }

  public NickRegistrationCriteria nick(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("nick")).equal(value);
    return this;
  }

  public NickRegistrationCriteria orderByNick() {
    return orderByNick(true);
  }

  public NickRegistrationCriteria orderByNick(boolean ascending) {
    query.order((!ascending ? "-" : "") + "nick");
    return this;
  }

  public NickRegistrationCriteria distinctNick() {
    ((QueryImpl) query).getCollection().distinct("nick");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, java.lang.String> twitterName() {
    return new TypeSafeFieldEnd<>(query, query.criteria("twitterName"));
  }

  public NickRegistrationCriteria twitterName(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("twitterName")).equal(value);
    return this;
  }

  public NickRegistrationCriteria orderByTwitterName() {
    return orderByTwitterName(true);
  }

  public NickRegistrationCriteria orderByTwitterName(boolean ascending) {
    query.order((!ascending ? "-" : "") + "twitterName");
    return this;
  }

  public NickRegistrationCriteria distinctTwitterName() {
    ((QueryImpl) query).getCollection().distinct("twitterName");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.NickRegistration, java.lang.String> url() {
    return new TypeSafeFieldEnd<>(query, query.criteria("url"));
  }

  public NickRegistrationCriteria url(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("url")).equal(value);
    return this;
  }

  public NickRegistrationCriteria orderByUrl() {
    return orderByUrl(true);
  }

  public NickRegistrationCriteria orderByUrl(boolean ascending) {
    query.order((!ascending ? "-" : "") + "url");
    return this;
  }

  public NickRegistrationCriteria distinctUrl() {
    ((QueryImpl) query).getCollection().distinct("url");
    return this;
  }

  public NickRegistrationUpdater getUpdater() {
    return new NickRegistrationUpdater();
  }

  public class NickRegistrationUpdater {
    UpdateOperations<javabot.model.NickRegistration> updateOperations;

    public NickRegistrationUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.NickRegistration.class);
    }

    public UpdateResults<javabot.model.NickRegistration> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.NickRegistration> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.NickRegistration> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.NickRegistration> upsert(WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public NickRegistrationUpdater host(java.lang.String value) {
      updateOperations.set("host", value);
      return this;
    }

    public NickRegistrationUpdater unsetHost(java.lang.String value) {
      updateOperations.unset("host");
      return this;
    }

    public NickRegistrationUpdater addHost(java.lang.String value) {
      updateOperations.add("host", value);
      return this;
    }

    public NickRegistrationUpdater addHost(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("host", value, addDups);
      return this;
    }

    public NickRegistrationUpdater addAllToHost(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("host", values, addDups);
      return this;
    }
  
    public NickRegistrationUpdater removeFirstHost() {
      updateOperations.removeFirst("host");
      return this;
    }
  
    public NickRegistrationUpdater removeLastHost() {
      updateOperations.removeLast("host");
      return this;
    }
  
    public NickRegistrationUpdater removeFromHost(java.lang.String value) {
      updateOperations.removeAll("host", value);
      return this;
    }

    public NickRegistrationUpdater removeAllFromHost(List<java.lang.String> values) {
      updateOperations.removeAll("host", values);
      return this;
    }
 
    public NickRegistrationUpdater decHost() {
      updateOperations.dec("host");
      return this;
    }

    public NickRegistrationUpdater incHost() {
      updateOperations.inc("host");
      return this;
    }

    public NickRegistrationUpdater incHost(Number value) {
      updateOperations.inc("host", value);
      return this;
    }
    public NickRegistrationUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public NickRegistrationUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public NickRegistrationUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public NickRegistrationUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public NickRegistrationUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public NickRegistrationUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public NickRegistrationUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public NickRegistrationUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public NickRegistrationUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public NickRegistrationUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public NickRegistrationUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public NickRegistrationUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public NickRegistrationUpdater nick(java.lang.String value) {
      updateOperations.set("nick", value);
      return this;
    }

    public NickRegistrationUpdater unsetNick(java.lang.String value) {
      updateOperations.unset("nick");
      return this;
    }

    public NickRegistrationUpdater addNick(java.lang.String value) {
      updateOperations.add("nick", value);
      return this;
    }

    public NickRegistrationUpdater addNick(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("nick", value, addDups);
      return this;
    }

    public NickRegistrationUpdater addAllToNick(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("nick", values, addDups);
      return this;
    }
  
    public NickRegistrationUpdater removeFirstNick() {
      updateOperations.removeFirst("nick");
      return this;
    }
  
    public NickRegistrationUpdater removeLastNick() {
      updateOperations.removeLast("nick");
      return this;
    }
  
    public NickRegistrationUpdater removeFromNick(java.lang.String value) {
      updateOperations.removeAll("nick", value);
      return this;
    }

    public NickRegistrationUpdater removeAllFromNick(List<java.lang.String> values) {
      updateOperations.removeAll("nick", values);
      return this;
    }
 
    public NickRegistrationUpdater decNick() {
      updateOperations.dec("nick");
      return this;
    }

    public NickRegistrationUpdater incNick() {
      updateOperations.inc("nick");
      return this;
    }

    public NickRegistrationUpdater incNick(Number value) {
      updateOperations.inc("nick", value);
      return this;
    }
    public NickRegistrationUpdater twitterName(java.lang.String value) {
      updateOperations.set("twitterName", value);
      return this;
    }

    public NickRegistrationUpdater unsetTwitterName(java.lang.String value) {
      updateOperations.unset("twitterName");
      return this;
    }

    public NickRegistrationUpdater addTwitterName(java.lang.String value) {
      updateOperations.add("twitterName", value);
      return this;
    }

    public NickRegistrationUpdater addTwitterName(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("twitterName", value, addDups);
      return this;
    }

    public NickRegistrationUpdater addAllToTwitterName(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("twitterName", values, addDups);
      return this;
    }
  
    public NickRegistrationUpdater removeFirstTwitterName() {
      updateOperations.removeFirst("twitterName");
      return this;
    }
  
    public NickRegistrationUpdater removeLastTwitterName() {
      updateOperations.removeLast("twitterName");
      return this;
    }
  
    public NickRegistrationUpdater removeFromTwitterName(java.lang.String value) {
      updateOperations.removeAll("twitterName", value);
      return this;
    }

    public NickRegistrationUpdater removeAllFromTwitterName(List<java.lang.String> values) {
      updateOperations.removeAll("twitterName", values);
      return this;
    }
 
    public NickRegistrationUpdater decTwitterName() {
      updateOperations.dec("twitterName");
      return this;
    }

    public NickRegistrationUpdater incTwitterName() {
      updateOperations.inc("twitterName");
      return this;
    }

    public NickRegistrationUpdater incTwitterName(Number value) {
      updateOperations.inc("twitterName", value);
      return this;
    }
    public NickRegistrationUpdater url(java.lang.String value) {
      updateOperations.set("url", value);
      return this;
    }

    public NickRegistrationUpdater unsetUrl(java.lang.String value) {
      updateOperations.unset("url");
      return this;
    }

    public NickRegistrationUpdater addUrl(java.lang.String value) {
      updateOperations.add("url", value);
      return this;
    }

    public NickRegistrationUpdater addUrl(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("url", value, addDups);
      return this;
    }

    public NickRegistrationUpdater addAllToUrl(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("url", values, addDups);
      return this;
    }
  
    public NickRegistrationUpdater removeFirstUrl() {
      updateOperations.removeFirst("url");
      return this;
    }
  
    public NickRegistrationUpdater removeLastUrl() {
      updateOperations.removeLast("url");
      return this;
    }
  
    public NickRegistrationUpdater removeFromUrl(java.lang.String value) {
      updateOperations.removeAll("url", value);
      return this;
    }

    public NickRegistrationUpdater removeAllFromUrl(List<java.lang.String> values) {
      updateOperations.removeAll("url", values);
      return this;
    }
 
    public NickRegistrationUpdater decUrl() {
      updateOperations.dec("url");
      return this;
    }

    public NickRegistrationUpdater incUrl() {
      updateOperations.inc("url");
      return this;
    }

    public NickRegistrationUpdater incUrl(Number value) {
      updateOperations.inc("url", value);
      return this;
    }
  }
}
