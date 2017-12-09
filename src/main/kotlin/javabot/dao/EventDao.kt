package javabot.dao

import com.google.inject.Inject
import javabot.model.AdminEvent
import javabot.model.State
import javabot.model.criteria.AdminEventCriteria
import org.mongodb.morphia.Datastore

class EventDao @Inject constructor(ds: Datastore)  : BaseDao<AdminEvent>(ds, AdminEvent::class.java) {
    fun findUnprocessed(): AdminEvent? {
        val criteria = AdminEventCriteria(ds)
        criteria.state(State.NEW)
        criteria.query().order("requestedOn")
        return criteria.query().get()
    }
}