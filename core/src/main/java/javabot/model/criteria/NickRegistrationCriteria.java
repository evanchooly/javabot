package javabot.model.criteria;

import javabot.model.NickRegistration;

public class NickRegistrationCriteria extends com.antwerkz.critter.criteria.BaseCriteria<NickRegistration> {
  private String prefix = "";

  public NickRegistrationCriteria(org.mongodb.morphia.Datastore ds) {
    super(ds, NickRegistration.class);
  }


  public com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, java.lang.String> host() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, java.lang.String>(this, query, prefix + "host");
  }

  public org.mongodb.morphia.query.Criteria host(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, java.lang.String>(this, query, prefix + "host").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, org.bson.types.ObjectId> id() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, org.bson.types.ObjectId>(this, query, prefix + "id");
  }

  public org.mongodb.morphia.query.Criteria id(org.bson.types.ObjectId value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, org.bson.types.ObjectId>(this, query, prefix + "id").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, java.lang.String> nick() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, java.lang.String>(this, query, prefix + "nick");
  }

  public org.mongodb.morphia.query.Criteria nick(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, java.lang.String>(this, query, prefix + "nick").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, java.lang.String> twitterName() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, java.lang.String>(this, query, prefix + "twitterName");
  }

  public org.mongodb.morphia.query.Criteria twitterName(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, java.lang.String>(this, query, prefix + "twitterName").equal(value);
  }

  public com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, java.lang.String> url() {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, java.lang.String>(this, query, prefix + "url");
  }

  public org.mongodb.morphia.query.Criteria url(java.lang.String value) {
    return new com.antwerkz.critter.TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, java.lang.String>(this, query, prefix + "url").equal(value);
  }


  public NickRegistrationUpdater getUpdater() {
    return new NickRegistrationUpdater();
  }

  public class NickRegistrationUpdater {
    org.mongodb.morphia.query.UpdateOperations<NickRegistration> updateOperations;

    public NickRegistrationUpdater() {
      updateOperations = ds.createUpdateOperations(NickRegistration.class);
    }

    public org.mongodb.morphia.query.UpdateResults<NickRegistration> update() {
      return ds.update(query(), updateOperations, false);
    }

    public org.mongodb.morphia.query.UpdateResults<NickRegistration> update(com.mongodb.WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public org.mongodb.morphia.query.UpdateResults<NickRegistration> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public org.mongodb.morphia.query.UpdateResults<NickRegistration> upsert(com.mongodb.WriteConcern wc) {
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

    public NickRegistrationUpdater addHost(java.lang.String value, boolean addDups) {
      updateOperations.add("host", value, addDups);
      return this;
    }

    public NickRegistrationUpdater addAllToHost(java.util.List<java.lang.String> values, boolean addDups) {
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

    public NickRegistrationUpdater removeAllFromHost(java.util.List<java.lang.String> values) {
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

    public NickRegistrationUpdater addId(org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public NickRegistrationUpdater addAllToId(java.util.List<org.bson.types.ObjectId> values, boolean addDups) {
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

    public NickRegistrationUpdater removeAllFromId(java.util.List<org.bson.types.ObjectId> values) {
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

    public NickRegistrationUpdater addNick(java.lang.String value, boolean addDups) {
      updateOperations.add("nick", value, addDups);
      return this;
    }

    public NickRegistrationUpdater addAllToNick(java.util.List<java.lang.String> values, boolean addDups) {
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

    public NickRegistrationUpdater removeAllFromNick(java.util.List<java.lang.String> values) {
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

    public NickRegistrationUpdater addTwitterName(java.lang.String value, boolean addDups) {
      updateOperations.add("twitterName", value, addDups);
      return this;
    }

    public NickRegistrationUpdater addAllToTwitterName(java.util.List<java.lang.String> values, boolean addDups) {
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

    public NickRegistrationUpdater removeAllFromTwitterName(java.util.List<java.lang.String> values) {
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

    public NickRegistrationUpdater addUrl(java.lang.String value, boolean addDups) {
      updateOperations.add("url", value, addDups);
      return this;
    }

    public NickRegistrationUpdater addAllToUrl(java.util.List<java.lang.String> values, boolean addDups) {
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

    public NickRegistrationUpdater removeAllFromUrl(java.util.List<java.lang.String> values) {
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
