package javabot.dao

import com.google.inject.Inject
import javabot.model.AdminEvent
import javabot.model.State
import javabot.model.criteria.AdminEventCriteria
import dev.morphia.Datastore
import dev.morphia.query.Sort

class EventDao @Inject constructor(ds: Datastore)  : BaseDao<AdminEvent>(ds, AdminEvent::class.java) {
    fun findUnprocessed(): AdminEvent? {
        val criteria = AdminEventCriteria(ds)
        criteria.state(State.NEW)
        criteria.query().order(Sort.ascending("requestedOn"))
        return criteria.query().first()
    }
}
