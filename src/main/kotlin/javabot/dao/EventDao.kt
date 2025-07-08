package javabot.dao

import com.google.inject.Inject
import dev.morphia.Datastore
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import dev.morphia.query.filters.Filters.eq
import javabot.model.AdminEvent
import javabot.model.State.NEW

class EventDao @Inject constructor(ds: Datastore) :
    BaseDao<AdminEvent>(ds, AdminEvent::class.java) {
    fun findUnprocessed(): AdminEvent? {
        return ds.find(AdminEvent::class.java)
            .filter(eq("state", NEW))
            .first(FindOptions().sort(Sort.ascending("requestedOn")))
    }
}
