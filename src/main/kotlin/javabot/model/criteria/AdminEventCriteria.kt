package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.Javabot
import javabot.model.AdminEvent
import javabot.model.AdminEvent.State
import javabot.model.EventType
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import java.time.LocalDateTime

class AdminEventCriteria(ds: Datastore) : BaseCriteria<AdminEvent>(ds, AdminEvent::class.java) {

    fun bot(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, Javabot> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.Javabot>(
              this, query, "bot")
    }

    fun bot(value: Javabot): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.Javabot>(
              this, query, "bot").equal(value)
    }

    fun completed(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, LocalDateTime> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, java.time.LocalDateTime>(
              this, query, "completed")
    }

    fun completed(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, java.time.LocalDateTime>(
              this, query, "completed").equal(value)
    }

    fun id(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, ObjectId> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun requestedBy(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, String> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, String>(
              this, query, "requestedBy")
    }

    fun requestedBy(value: String): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, String>(
              this, query, "requestedBy").equal(value)
    }

    fun requestedOn(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, LocalDateTime> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, java.time.LocalDateTime>(
              this, query, "requestedOn")
    }

    fun requestedOn(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, java.time.LocalDateTime>(
              this, query, "requestedOn").equal(value)
    }

    fun state(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, State> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.AdminEvent.State>(
              this, query, "state")
    }

    fun state(value: State): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.AdminEvent.State>(
              this, query, "state").equal(value)
    }

    fun type(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, EventType> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.EventType>(
              this, query, "type")
    }

    fun type(value: EventType): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.EventType>(
              this, query, "type").equal(value)
    }

    fun getUpdater(): AdminEventUpdater {
        return AdminEventUpdater()
    }

    inner class AdminEventUpdater {
        var updateOperations = ds.createUpdateOperations(AdminEvent::class.java)

        fun updateAll(): UpdateResults {
            return ds.update(query(), updateOperations, false)
        }

        fun updateFirst(): UpdateResults {
            return ds.updateFirst(query(), updateOperations, false)
        }

        fun updateAll(wc: WriteConcern): UpdateResults {
            return ds.update(query(), updateOperations, false, wc)
        }

        fun updateFirst(wc: WriteConcern): UpdateResults {
            return ds.updateFirst(query(), updateOperations, false, wc)
        }

        fun upsert(): UpdateResults {
            return ds.update(query(), updateOperations, true)
        }

        fun upsert(wc: WriteConcern): UpdateResults {
            return ds.update(query(), updateOperations, true, wc)
        }

        fun remove(): WriteResult {
            return ds.delete(query())
        }

        fun remove(wc: WriteConcern): WriteResult {
            return ds.delete(query(), wc)
        }

        fun bot(value: Javabot): AdminEventUpdater {
            updateOperations.set("bot", value)
            return this
        }

        fun unsetBot(): AdminEventUpdater {
            updateOperations.unset("bot")
            return this
        }

        fun completed(value: LocalDateTime): AdminEventUpdater {
            updateOperations.set("completed", value)
            return this
        }

        fun unsetCompleted(): AdminEventUpdater {
            updateOperations.unset("completed")
            return this
        }

        fun requestedBy(value: String): AdminEventUpdater {
            updateOperations.set("requestedBy", value)
            return this
        }

        fun unsetRequestedBy(): AdminEventUpdater {
            updateOperations.unset("requestedBy")
            return this
        }

        fun requestedOn(value: LocalDateTime): AdminEventUpdater {
            updateOperations.set("requestedOn", value)
            return this
        }

        fun unsetRequestedOn(): AdminEventUpdater {
            updateOperations.unset("requestedOn")
            return this
        }

        fun state(value: State): AdminEventUpdater {
            updateOperations.set("state", value)
            return this
        }

        fun unsetState(): AdminEventUpdater {
            updateOperations.unset("state")
            return this
        }

        fun type(value: EventType): AdminEventUpdater {
            updateOperations.set("type", value)
            return this
        }

        fun unsetType(): AdminEventUpdater {
            updateOperations.unset("type")
            return this
        }
    }
}
