package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.Javabot
import javabot.model.AdminEvent.State
import javabot.model.EventType
import javabot.model.OperationEvent
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import java.time.LocalDateTime

class OperationEventCriteria(ds: Datastore) : BaseCriteria<OperationEvent>(ds, OperationEvent::class.java) {

    fun operation(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, String> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, String>(
              this, query, "operation")
    }

    fun operation(value: String): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, String>(
              this, query, "operation").equal(value)
    }

    fun bot(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, Javabot> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.Javabot>(
              this, query, "bot")
    }

    fun bot(value: Javabot): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.Javabot>(
              this, query, "bot").equal(value)
    }

    fun completed(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, LocalDateTime> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.time.LocalDateTime>(
              this, query, "completed")
    }

    fun completed(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.time.LocalDateTime>(
              this, query, "completed").equal(value)
    }

    fun id(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, ObjectId> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun requestedBy(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, String> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, String>(
              this, query, "requestedBy")
    }

    fun requestedBy(value: String): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, String>(
              this, query, "requestedBy").equal(value)
    }

    fun requestedOn(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, LocalDateTime> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.time.LocalDateTime>(
              this, query, "requestedOn")
    }

    fun requestedOn(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.time.LocalDateTime>(
              this, query, "requestedOn").equal(value)
    }

    fun state(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, State> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.AdminEvent.State>(
              this, query, "state")
    }

    fun state(value: State): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.AdminEvent.State>(
              this, query, "state").equal(value)
    }

    fun type(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, EventType> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.EventType>(
              this, query, "type")
    }

    fun type(value: EventType): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.EventType>(
              this, query, "type").equal(value)
    }

    fun getUpdater(): OperationEventUpdater {
        return OperationEventUpdater()
    }

    inner class OperationEventUpdater {
        var updateOperations = ds.createUpdateOperations(OperationEvent::class.java)

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

        fun operation(value: String): OperationEventUpdater {
            updateOperations.set("operation", value)
            return this
        }

        fun unsetOperation(): OperationEventUpdater {
            updateOperations.unset("operation")
            return this
        }

        fun bot(value: Javabot): OperationEventUpdater {
            updateOperations.set("bot", value)
            return this
        }

        fun unsetBot(): OperationEventUpdater {
            updateOperations.unset("bot")
            return this
        }

        fun completed(value: LocalDateTime): OperationEventUpdater {
            updateOperations.set("completed", value)
            return this
        }

        fun unsetCompleted(): OperationEventUpdater {
            updateOperations.unset("completed")
            return this
        }

        fun requestedBy(value: String): OperationEventUpdater {
            updateOperations.set("requestedBy", value)
            return this
        }

        fun unsetRequestedBy(): OperationEventUpdater {
            updateOperations.unset("requestedBy")
            return this
        }

        fun requestedOn(value: LocalDateTime): OperationEventUpdater {
            updateOperations.set("requestedOn", value)
            return this
        }

        fun unsetRequestedOn(): OperationEventUpdater {
            updateOperations.unset("requestedOn")
            return this
        }

        fun state(value: State): OperationEventUpdater {
            updateOperations.set("state", value)
            return this
        }

        fun unsetState(): OperationEventUpdater {
            updateOperations.unset("state")
            return this
        }

        fun type(value: EventType): OperationEventUpdater {
            updateOperations.set("type", value)
            return this
        }

        fun unsetType(): OperationEventUpdater {
            updateOperations.unset("type")
            return this
        }
    }
}
