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
import org.pircbotx.PircBotX
import java.time.LocalDateTime
import javax.inject.Provider

public class ChannelEventCriteria(ds: Datastore) : BaseCriteria<ChannelEvent>(ds, ChannelEvent::class.java) {

    public fun channel(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String>(
              this, query, "channel")
    }

    public fun channel(value: String): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String>(
              this, query, "channel").equal(value)
    }

    public fun channelDao(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, ChannelDao> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.dao.ChannelDao>(
              this, query, "channelDao")
    }

    public fun channelDao(value: ChannelDao): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.dao.ChannelDao>(
              this, query, "channelDao").equal(value)
    }

    public fun key(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String>(
              this, query, "key")
    }

    public fun key(value: String): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String>(
              this, query, "key").equal(value)
    }

    public fun logged(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, Boolean> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, Boolean>(
              this, query, "logged")
    }

    public fun logged(value: Boolean?): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, Boolean>(
              this, query, "logged").equal(value)
    }

    public fun bot(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, Javabot> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.Javabot>(
              this, query, "bot")
    }

    public fun bot(value: Javabot): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.Javabot>(
              this, query, "bot").equal(value)
    }

    public fun completed(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, LocalDateTime> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime>(
              this, query, "completed")
    }

    public fun completed(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime>(
              this, query, "completed").equal(value)
    }

    public fun id(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, ObjectId> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun requestedBy(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String>(
              this, query, "requestedBy")
    }

    public fun requestedBy(value: String): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, String>(
              this, query, "requestedBy").equal(value)
    }

    public fun requestedOn(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, LocalDateTime> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime>(
              this, query, "requestedOn")
    }

    public fun requestedOn(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, java.time.LocalDateTime>(
              this, query, "requestedOn").equal(value)
    }

    public fun state(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, State> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.AdminEvent.State>(
              this, query, "state")
    }

    public fun state(value: State): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.AdminEvent.State>(
              this, query, "state").equal(value)
    }

    public fun type(): TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, EventType> {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.EventType>(
              this, query, "type")
    }

    public fun type(value: EventType): Criteria {
        return TypeSafeFieldEnd<ChannelEventCriteria, ChannelEvent, javabot.model.EventType>(
              this, query, "type").equal(value)
    }

    public fun getUpdater(): ChannelEventUpdater {
        return ChannelEventUpdater()
    }

    public inner class ChannelEventUpdater {
        var updateOperations = ds.createUpdateOperations(ChannelEvent::class.java)

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

        public fun channel(value: String): ChannelEventUpdater {
            updateOperations.set("channel", value)
            return this
        }

        public fun unsetChannel(): ChannelEventUpdater {
            updateOperations.unset("channel")
            return this
        }

        public fun channelDao(value: ChannelDao): ChannelEventUpdater {
            updateOperations.set("channelDao", value)
            return this
        }

        public fun unsetChannelDao(): ChannelEventUpdater {
            updateOperations.unset("channelDao")
            return this
        }

        public fun ircBot(value: Provider<PircBotX>): ChannelEventUpdater {
            updateOperations.set("ircBot", value)
            return this
        }

        public fun unsetIrcBot(): ChannelEventUpdater {
            updateOperations.unset("ircBot")
            return this
        }

        public fun key(value: String): ChannelEventUpdater {
            updateOperations.set("key", value)
            return this
        }

        public fun unsetKey(): ChannelEventUpdater {
            updateOperations.unset("key")
            return this
        }

        public fun logged(value: Boolean?): ChannelEventUpdater {
            updateOperations.set("logged", value)
            return this
        }

        public fun unsetLogged(): ChannelEventUpdater {
            updateOperations.unset("logged")
            return this
        }

        public fun bot(value: Javabot): ChannelEventUpdater {
            updateOperations.set("bot", value)
            return this
        }

        public fun unsetBot(): ChannelEventUpdater {
            updateOperations.unset("bot")
            return this
        }

        public fun completed(value: LocalDateTime): ChannelEventUpdater {
            updateOperations.set("completed", value)
            return this
        }

        public fun unsetCompleted(): ChannelEventUpdater {
            updateOperations.unset("completed")
            return this
        }

        public fun requestedBy(value: String): ChannelEventUpdater {
            updateOperations.set("requestedBy", value)
            return this
        }

        public fun unsetRequestedBy(): ChannelEventUpdater {
            updateOperations.unset("requestedBy")
            return this
        }

        public fun requestedOn(value: LocalDateTime): ChannelEventUpdater {
            updateOperations.set("requestedOn", value)
            return this
        }

        public fun unsetRequestedOn(): ChannelEventUpdater {
            updateOperations.unset("requestedOn")
            return this
        }

        public fun state(value: State): ChannelEventUpdater {
            updateOperations.set("state", value)
            return this
        }

        public fun unsetState(): ChannelEventUpdater {
            updateOperations.unset("state")
            return this
        }

        public fun type(value: EventType): ChannelEventUpdater {
            updateOperations.set("type", value)
            return this
        }

        public fun unsetType(): ChannelEventUpdater {
            updateOperations.unset("type")
            return this
        }
    }
}
