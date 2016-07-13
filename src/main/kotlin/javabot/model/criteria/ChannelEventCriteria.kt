package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.Javabot
import javabot.dao.ChannelDao
import javabot.model.AdminEvent.State
import javabot.model.ChannelEvent
import javabot.model.EventType
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import java.time.LocalDateTime

class ChannelEventCriteria(ds: Datastore) : BaseCriteria<ChannelEvent>(ds, ChannelEvent::class.java) {

    fun channel(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String>(
              this, query, "channel")
    }

    fun channel(value: String): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String>(
              this, query, "channel").equal(value)
    }

    fun channelDao(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, ChannelDao> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.dao.ChannelDao>(
              this, query, "channelDao")
    }

    fun channelDao(value: ChannelDao): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.dao.ChannelDao>(
              this, query, "channelDao").equal(value)
    }

    fun key(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String>(
              this, query, "key")
    }

    fun key(value: String): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String>(
              this, query, "key").equal(value)
    }

    fun logged(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, Boolean> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, Boolean>(
              this, query, "logged")
    }

    fun logged(value: Boolean?): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, Boolean>(
              this, query, "logged").equal(value)
    }

    fun bot(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, Javabot> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.Javabot>(
              this, query, "bot")
    }

    fun bot(value: Javabot): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.Javabot>(
              this, query, "bot").equal(value)
    }

    fun completed(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, LocalDateTime> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime>(
              this, query, "completed")
    }

    fun completed(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime>(
              this, query, "completed").equal(value)
    }

    fun id(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, ObjectId> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun requestedBy(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String>(
              this, query, "requestedBy")
    }

    fun requestedBy(value: String): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String>(
              this, query, "requestedBy").equal(value)
    }

    fun requestedOn(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, LocalDateTime> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime>(
              this, query, "requestedOn")
    }

    fun requestedOn(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime>(
              this, query, "requestedOn").equal(value)
    }

    fun state(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, State> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.AdminEvent.State>(
              this, query, "state")
    }

    fun state(value: State): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.AdminEvent.State>(
              this, query, "state").equal(value)
    }

    fun type(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, EventType> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.EventType>(
              this, query, "type")
    }

    fun type(value: EventType): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.EventType>(
              this, query, "type").equal(value)
    }

    fun getUpdater(): ChannelEventUpdater {
        return ChannelEventUpdater()
    }

    inner class ChannelEventUpdater {
        var updateOperations = ds.createUpdateOperations(ChannelEvent::class.java)

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

        fun channel(value: String): ChannelEventUpdater {
            updateOperations.set("channel", value)
            return this
        }

        fun unsetChannel(): ChannelEventUpdater {
            updateOperations.unset("channel")
            return this
        }

        fun channelDao(value: ChannelDao): ChannelEventUpdater {
            updateOperations.set("channelDao", value)
            return this
        }

        fun unsetChannelDao(): ChannelEventUpdater {
            updateOperations.unset("channelDao")
            return this
        }

        fun unsetIrcBot(): ChannelEventUpdater {
            updateOperations.unset("ircBot")
            return this
        }

        fun key(value: String): ChannelEventUpdater {
            updateOperations.set("key", value)
            return this
        }

        fun unsetKey(): ChannelEventUpdater {
            updateOperations.unset("key")
            return this
        }

        fun logged(value: Boolean?): ChannelEventUpdater {
            updateOperations.set("logged", value)
            return this
        }

        fun unsetLogged(): ChannelEventUpdater {
            updateOperations.unset("logged")
            return this
        }

        fun bot(value: Javabot): ChannelEventUpdater {
            updateOperations.set("bot", value)
            return this
        }

        fun unsetBot(): ChannelEventUpdater {
            updateOperations.unset("bot")
            return this
        }

        fun completed(value: LocalDateTime): ChannelEventUpdater {
            updateOperations.set("completed", value)
            return this
        }

        fun unsetCompleted(): ChannelEventUpdater {
            updateOperations.unset("completed")
            return this
        }

        fun requestedBy(value: String): ChannelEventUpdater {
            updateOperations.set("requestedBy", value)
            return this
        }

        fun unsetRequestedBy(): ChannelEventUpdater {
            updateOperations.unset("requestedBy")
            return this
        }

        fun requestedOn(value: LocalDateTime): ChannelEventUpdater {
            updateOperations.set("requestedOn", value)
            return this
        }

        fun unsetRequestedOn(): ChannelEventUpdater {
            updateOperations.unset("requestedOn")
            return this
        }

        fun state(value: State): ChannelEventUpdater {
            updateOperations.set("state", value)
            return this
        }

        fun unsetState(): ChannelEventUpdater {
            updateOperations.unset("state")
            return this
        }

        fun type(value: EventType): ChannelEventUpdater {
            updateOperations.set("type", value)
            return this
        }

        fun unsetType(): ChannelEventUpdater {
            updateOperations.unset("type")
            return this
        }
    }
}
