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


public class ChangeCriteria {
  private Query<javabot.model.Change> query;
  private Datastore ds;

  public Query<javabot.model.Change> query() {
    return query;
  }

  public ChangeCriteria(Datastore ds) {
    this.ds = ds;
    query = ds.find(javabot.model.Change.class);
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

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Change, org.joda.time.DateTime> changeDate() {
    return new TypeSafeFieldEnd<>(query, query.criteria("changeDate"));
  }

  public ChangeCriteria changeDate(org.joda.time.DateTime value) {
    new TypeSafeFieldEnd<>(query, query.criteria("changeDate")).equal(value);
    return this;
  }

  public ChangeCriteria orderByChangeDate() {
    return orderByChangeDate(true);
  }

  public ChangeCriteria orderByChangeDate(boolean ascending) {
    query.order((!ascending ? "-" : "") + "changeDate");
    return this;
  }

  public ChangeCriteria distinctChangeDate() {
    ((QueryImpl) query).getCollection().distinct("changeDate");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Change, org.bson.types.ObjectId> id() {
    return new TypeSafeFieldEnd<>(query, query.criteria("id"));
  }

  public ChangeCriteria id(org.bson.types.ObjectId value) {
    new TypeSafeFieldEnd<>(query, query.criteria("id")).equal(value);
    return this;
  }

  public ChangeCriteria orderById() {
    return orderById(true);
  }

  public ChangeCriteria orderById(boolean ascending) {
    query.order((!ascending ? "-" : "") + "id");
    return this;
  }

  public ChangeCriteria distinctId() {
    ((QueryImpl) query).getCollection().distinct("id");
    return this;
  }

  public TypeSafeFieldEnd<? extends CriteriaContainer, javabot.model.Change, java.lang.String> message() {
    return new TypeSafeFieldEnd<>(query, query.criteria("message"));
  }

  public ChangeCriteria message(java.lang.String value) {
    new TypeSafeFieldEnd<>(query, query.criteria("message")).equal(value);
    return this;
  }

  public ChangeCriteria orderByMessage() {
    return orderByMessage(true);
  }

  public ChangeCriteria orderByMessage(boolean ascending) {
    query.order((!ascending ? "-" : "") + "message");
    return this;
  }

  public ChangeCriteria distinctMessage() {
    ((QueryImpl) query).getCollection().distinct("message");
    return this;
  }

  public ChangeUpdater getUpdater() {
    return new ChangeUpdater();
  }

  public class ChangeUpdater {
    UpdateOperations<javabot.model.Change> updateOperations;

    public ChangeUpdater() {
      updateOperations = ds.createUpdateOperations(javabot.model.Change.class);
    }

    public UpdateResults<javabot.model.Change> update() {
      return ds.update(query(), updateOperations, false);
    }

    public UpdateResults<javabot.model.Change> update(WriteConcern wc) {
      return ds.update(query(), updateOperations, false, wc);
    }

    public UpdateResults<javabot.model.Change> upsert() {
      return ds.update(query(), updateOperations, true);
    }

    public UpdateResults<javabot.model.Change> upsert(WriteConcern wc) {
      return ds.update(query(), updateOperations, true, wc);
    }

    public ChangeUpdater changeDate(org.joda.time.DateTime value) {
      updateOperations.set("changeDate", value);
      return this;
    }

    public ChangeUpdater unsetChangeDate(org.joda.time.DateTime value) {
      updateOperations.unset("changeDate");
      return this;
    }

    public ChangeUpdater addChangeDate(org.joda.time.DateTime value) {
      updateOperations.add("changeDate", value);
      return this;
    }

    public ChangeUpdater addChangeDate(String fieldExpr, org.joda.time.DateTime value, boolean addDups) {
      updateOperations.add("changeDate", value, addDups);
      return this;
    }

    public ChangeUpdater addAllToChangeDate(List<org.joda.time.DateTime> values, boolean addDups) {
      updateOperations.addAll("changeDate", values, addDups);
      return this;
    }
  
    public ChangeUpdater removeFirstChangeDate() {
      updateOperations.removeFirst("changeDate");
      return this;
    }
  
    public ChangeUpdater removeLastChangeDate() {
      updateOperations.removeLast("changeDate");
      return this;
    }
  
    public ChangeUpdater removeFromChangeDate(org.joda.time.DateTime value) {
      updateOperations.removeAll("changeDate", value);
      return this;
    }

    public ChangeUpdater removeAllFromChangeDate(List<org.joda.time.DateTime> values) {
      updateOperations.removeAll("changeDate", values);
      return this;
    }
 
    public ChangeUpdater decChangeDate() {
      updateOperations.dec("changeDate");
      return this;
    }

    public ChangeUpdater incChangeDate() {
      updateOperations.inc("changeDate");
      return this;
    }

    public ChangeUpdater incChangeDate(Number value) {
      updateOperations.inc("changeDate", value);
      return this;
    }
    public ChangeUpdater id(org.bson.types.ObjectId value) {
      updateOperations.set("id", value);
      return this;
    }

    public ChangeUpdater unsetId(org.bson.types.ObjectId value) {
      updateOperations.unset("id");
      return this;
    }

    public ChangeUpdater addId(org.bson.types.ObjectId value) {
      updateOperations.add("id", value);
      return this;
    }

    public ChangeUpdater addId(String fieldExpr, org.bson.types.ObjectId value, boolean addDups) {
      updateOperations.add("id", value, addDups);
      return this;
    }

    public ChangeUpdater addAllToId(List<org.bson.types.ObjectId> values, boolean addDups) {
      updateOperations.addAll("id", values, addDups);
      return this;
    }
  
    public ChangeUpdater removeFirstId() {
      updateOperations.removeFirst("id");
      return this;
    }
  
    public ChangeUpdater removeLastId() {
      updateOperations.removeLast("id");
      return this;
    }
  
    public ChangeUpdater removeFromId(org.bson.types.ObjectId value) {
      updateOperations.removeAll("id", value);
      return this;
    }

    public ChangeUpdater removeAllFromId(List<org.bson.types.ObjectId> values) {
      updateOperations.removeAll("id", values);
      return this;
    }
 
    public ChangeUpdater decId() {
      updateOperations.dec("id");
      return this;
    }

    public ChangeUpdater incId() {
      updateOperations.inc("id");
      return this;
    }

    public ChangeUpdater incId(Number value) {
      updateOperations.inc("id", value);
      return this;
    }
    public ChangeUpdater message(java.lang.String value) {
      updateOperations.set("message", value);
      return this;
    }

    public ChangeUpdater unsetMessage(java.lang.String value) {
      updateOperations.unset("message");
      return this;
    }

    public ChangeUpdater addMessage(java.lang.String value) {
      updateOperations.add("message", value);
      return this;
    }

    public ChangeUpdater addMessage(String fieldExpr, java.lang.String value, boolean addDups) {
      updateOperations.add("message", value, addDups);
      return this;
    }

    public ChangeUpdater addAllToMessage(List<java.lang.String> values, boolean addDups) {
      updateOperations.addAll("message", values, addDups);
      return this;
    }
  
    public ChangeUpdater removeFirstMessage() {
      updateOperations.removeFirst("message");
      return this;
    }
  
    public ChangeUpdater removeLastMessage() {
      updateOperations.removeLast("message");
      return this;
    }
  
    public ChangeUpdater removeFromMessage(java.lang.String value) {
      updateOperations.removeAll("message", value);
      return this;
    }

    public ChangeUpdater removeAllFromMessage(List<java.lang.String> values) {
      updateOperations.removeAll("message", values);
      return this;
    }
 
    public ChangeUpdater decMessage() {
      updateOperations.dec("message");
      return this;
    }

    public ChangeUpdater incMessage() {
      updateOperations.inc("message");
      return this;
    }

    public ChangeUpdater incMessage(Number value) {
      updateOperations.inc("message", value);
      return this;
    }
  }
}
