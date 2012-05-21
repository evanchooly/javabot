package javabot.dao;

import javabot.model.AdminEvent;

import java.util.List;

public interface EventDao extends BaseDao {
    String FIND_ALL = "Event.findAll";

    List<AdminEvent> findUnprocessed();
}
