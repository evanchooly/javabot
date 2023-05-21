package javabot.dao

import com.google.inject.Inject
import dev.morphia.Datastore
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import javabot.model.AdminEvent
import javabot.model.State.NEW
import javabot.model.criteria.AdminEventCriteria.Companion.requestedOn
import javabot.model.criteria.AdminEventCriteria.Companion.state

class EventDao @Inject constructor(ds: Datastore) :
    BaseDao<AdminEvent>(ds, AdminEvent::class.java) {
    fun findUnprocessed(): AdminEvent? {
        return ds.find(AdminEvent::class.java)
            .filter(state().eq(NEW))
            .first(FindOptions().sort(Sort.ascending(requestedOn)))
    }
}
