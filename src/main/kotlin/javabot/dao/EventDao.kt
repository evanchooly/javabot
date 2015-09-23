package javabot.dao

import javabot.model.AdminEvent
import javabot.model.AdminEvent.State
import javabot.model.criteria.AdminEventCriteria

public class EventDao protected constructor() : BaseDao<AdminEvent>(AdminEvent::class.java) {

    public fun findUnprocessed(): AdminEvent {
        val criteria = AdminEventCriteria(ds)
        criteria.state(State.NEW)
        criteria.query().order("requestedOn")
        return criteria.query().get()
    }
}