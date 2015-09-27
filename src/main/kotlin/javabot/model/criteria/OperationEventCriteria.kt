package javabot.model.criteria

import com.antwerkz.critter.criteria.BaseCriteria
import javabot.model.OperationEvent
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.FieldEndImpl
import org.mongodb.morphia.query.QueryImpl
import com.antwerkz.critter.TypeSafeFieldEnd
import javabot.Javabot
import java.time.LocalDateTime
import org.bson.types.ObjectId
import javabot.model.AdminEvent.State
import javabot.model.EventType
import org.mongodb.morphia.query.UpdateOperations
import org.mongodb.morphia.query.UpdateResults
import com.mongodb.WriteConcern
import com.mongodb.WriteResult

public class OperationEventCriteria(ds: Datastore) : BaseCriteria<OperationEvent>(ds, OperationEvent::class.java) {

    public fun operation(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, String> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, String>(
              this, query, "operation")
    }

    public fun operation(value: String): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, String>(
              this, query, "operation").equal(value)
    }

    public fun bot(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, Javabot> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.Javabot>(
              this, query, "bot")
    }

    public fun bot(value: Javabot): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.Javabot>(
              this, query, "bot").equal(value)
    }

    public fun completed(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, LocalDateTime> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.time.LocalDateTime>(
              this, query, "completed")
    }

    public fun completed(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.time.LocalDateTime>(
              this, query, "completed").equal(value)
    }

    public fun id(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, ObjectId> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun requestedBy(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, String> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, String>(
              this, query, "requestedBy")
    }

    public fun requestedBy(value: String): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, String>(
              this, query, "requestedBy").equal(value)
    }

    public fun requestedOn(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, LocalDateTime> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.time.LocalDateTime>(
              this, query, "requestedOn")
    }

    public fun requestedOn(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, java.time.LocalDateTime>(
              this, query, "requestedOn").equal(value)
    }

    public fun state(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, State> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.AdminEvent.State>(
              this, query, "state")
    }

    public fun state(value: State): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.AdminEvent.State>(
              this, query, "state").equal(value)
    }

    public fun type(): TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, EventType> {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.EventType>(
              this, query, "type")
    }

    public fun type(value: EventType): Criteria {
        return TypeSafeFieldEnd<OperationEventCriteria, OperationEvent, javabot.model.EventType>(
              this, query, "type").equal(value)
    }

    public fun getUpdater(): OperationEventUpdater {
        return OperationEventUpdater()
    }

    public inner class OperationEventUpdater {
        var updateOperations = ds.createUpdateOperations(OperationEvent::class.java)

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

        public fun operation(value: String): OperationEventUpdater {
            updateOperations.set("operation", value)
            return this
        }

        public fun unsetOperation(): OperationEventUpdater {
            updateOperations.unset("operation")
            return this
        }

        public fun bot(value: Javabot): OperationEventUpdater {
            updateOperations.set("bot", value)
            return this
        }

        public fun unsetBot(): OperationEventUpdater {
            updateOperations.unset("bot")
            return this
        }

        public fun completed(value: LocalDateTime): OperationEventUpdater {
            updateOperations.set("completed", value)
            return this
        }

        public fun unsetCompleted(): OperationEventUpdater {
            updateOperations.unset("completed")
            return this
        }

        public fun requestedBy(value: String): OperationEventUpdater {
            updateOperations.set("requestedBy", value)
            return this
        }

        public fun unsetRequestedBy(): OperationEventUpdater {
            updateOperations.unset("requestedBy")
            return this
        }

        public fun requestedOn(value: LocalDateTime): OperationEventUpdater {
            updateOperations.set("requestedOn", value)
            return this
        }

        public fun unsetRequestedOn(): OperationEventUpdater {
            updateOperations.unset("requestedOn")
            return this
        }

        public fun state(value: State): OperationEventUpdater {
            updateOperations.set("state", value)
            return this
        }

        public fun unsetState(): OperationEventUpdater {
            updateOperations.unset("state")
            return this
        }

        public fun type(value: EventType): OperationEventUpdater {
            updateOperations.set("type", value)
            return this
        }

        public fun unsetType(): OperationEventUpdater {
            updateOperations.unset("type")
            return this
        }
    }
}
