package javabot.dao;

import java.util.List;

import javabot.model.AdminEvent;
import javabot.model.criteria.AdminEventCriteria;

public class EventDao extends BaseDao<AdminEvent> {
  protected EventDao() {
    super(AdminEvent.class);
  }

  public List<AdminEvent> findUnprocessed() {
    AdminEventCriteria criteria = new AdminEventCriteria(ds);
    criteria.processed().equal(Boolean.FALSE);
    criteria.query().order("-requestedOn");
    List<AdminEvent> adminEvents = criteria.query().asList();
    return adminEvents;
  }
}