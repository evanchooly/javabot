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

public class AdminEventCriteria(ds: Datastore) : BaseCriteria<AdminEvent>(ds, AdminEvent::class.java) {

    public fun bot(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, Javabot> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.Javabot>(
              this, query, "bot")
    }

    public fun bot(value: Javabot): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.Javabot>(
              this, query, "bot").equal(value)
    }

    public fun completed(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, LocalDateTime> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, java.time.LocalDateTime>(
              this, query, "completed")
    }

    public fun completed(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, java.time.LocalDateTime>(
              this, query, "completed").equal(value)
    }

    public fun id(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, ObjectId> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun requestedBy(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, String> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, String>(
              this, query, "requestedBy")
    }

    public fun requestedBy(value: String): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, String>(
              this, query, "requestedBy").equal(value)
    }

    public fun requestedOn(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, LocalDateTime> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, java.time.LocalDateTime>(
              this, query, "requestedOn")
    }

    public fun requestedOn(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, java.time.LocalDateTime>(
              this, query, "requestedOn").equal(value)
    }

    public fun state(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, State> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.AdminEvent.State>(
              this, query, "state")
    }

    public fun state(value: State): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.AdminEvent.State>(
              this, query, "state").equal(value)
    }

    public fun type(): TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, EventType> {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.EventType>(
              this, query, "type")
    }

    public fun type(value: EventType): Criteria {
        return TypeSafeFieldEnd<AdminEventCriteria, AdminEvent, javabot.model.EventType>(
              this, query, "type").equal(value)
    }

    public fun getUpdater(): AdminEventUpdater {
        return AdminEventUpdater()
    }

    public inner class AdminEventUpdater {
        var updateOperations = ds.createUpdateOperations(AdminEvent::class.java)

        public fun updateAll(): UpdateResults {
            return ds.update(query(), updateOperations, false)
        }

        public fun updateFirst(): UpdateResults {
            return ds.updateFirst(query(), updateOperations, false)
        }

        public fun updateAll(wc: WriteConcern): UpdateResults {
            return ds.update(query(), updateOperations, false, wc)
        }

        public fun updateFirst(wc: WriteConcern): UpdateResults {
            return ds.updateFirst(query(), updateOperations, false, wc)
        }

        public fun upsert(): UpdateResults {
            return ds.update(query(), updateOperations, true)
        }

        public fun upsert(wc: WriteConcern): UpdateResults {
            return ds.update(query(), updateOperations, true, wc)
        }

        public fun remove(): WriteResult {
            return ds.delete(query())
        }

        public fun remove(wc: WriteConcern): WriteResult {
            return ds.delete(query(), wc)
        }

        public fun bot(value: Javabot): AdminEventUpdater {
            updateOperations.set("bot", value)
            return this
        }

        public fun unsetBot(): AdminEventUpdater {
            updateOperations.unset("bot")
            return this
        }

        public fun completed(value: LocalDateTime): AdminEventUpdater {
            updateOperations.set("completed", value)
            return this
        }

        public fun unsetCompleted(): AdminEventUpdater {
            updateOperations.unset("completed")
            return this
        }

        public fun requestedBy(value: String): AdminEventUpdater {
            updateOperations.set("requestedBy", value)
            return this
        }

        public fun unsetRequestedBy(): AdminEventUpdater {
            updateOperations.unset("requestedBy")
            return this
        }

        public fun requestedOn(value: LocalDateTime): AdminEventUpdater {
            updateOperations.set("requestedOn", value)
            return this
        }

        public fun unsetRequestedOn(): AdminEventUpdater {
            updateOperations.unset("requestedOn")
            return this
        }

        public fun state(value: State): AdminEventUpdater {
            updateOperations.set("state", value)
            return this
        }

        public fun unsetState(): AdminEventUpdater {
            updateOperations.unset("state")
            return this
        }

        public fun type(value: EventType): AdminEventUpdater {
            updateOperations.set("type", value)
            return this
        }

        public fun unsetType(): AdminEventUpdater {
            updateOperations.unset("type")
            return this
        }
    }
}
