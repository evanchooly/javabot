package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.google.inject.Provider
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.Javabot
import javabot.dao.AdminDao
import javabot.dao.ApiDao
import javabot.javadoc.JavadocParser
import javabot.model.AdminEvent.State
import javabot.model.ApiEvent
import javabot.model.EventType
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import org.pircbotx.PircBotX
import java.time.LocalDateTime

public class ApiEventCriteria(ds: Datastore) : BaseCriteria<ApiEvent>(ds, ApiEvent::class.java) {

    public fun adminDao(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, AdminDao> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.dao.AdminDao>(
              this, query, "adminDao")
    }

    public fun adminDao(value: AdminDao): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.dao.AdminDao>(
              this, query, "adminDao").equal(value)
    }

    public fun apiDao(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, ApiDao> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.dao.ApiDao>(
              this, query, "apiDao")
    }

    public fun apiDao(value: ApiDao): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.dao.ApiDao>(
              this, query, "apiDao").equal(value)
    }

    public fun apiId(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, ObjectId> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId>(
              this, query, "apiId")
    }

    public fun apiId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId>(
              this, query, "apiId").equal(value)
    }

    public fun baseUrl(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "baseUrl")
    }

    public fun baseUrl(value: String): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "baseUrl").equal(value)
    }

    public fun downloadUrl(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "downloadUrl")
    }

    public fun downloadUrl(value: String): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "downloadUrl").equal(value)
    }

    public fun ircBot(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, Provider<Any>> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, com.google.inject.Provider<Any>>(
              this, query, "ircBot")
    }

    public fun name(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "name")
    }

    public fun name(value: String): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "name").equal(value)
    }

    public fun parser(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, JavadocParser> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.javadoc.JavadocParser>(
              this, query, "parser")
    }

    public fun parser(value: JavadocParser): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.javadoc.JavadocParser>(
              this, query, "parser").equal(value)
    }

    public fun bot(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, Javabot> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.Javabot>(
              this, query, "bot")
    }

    public fun bot(value: Javabot): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.Javabot>(
              this, query, "bot").equal(value)
    }

    public fun completed(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, LocalDateTime> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.time.LocalDateTime>(
              this, query, "completed")
    }

    public fun completed(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.time.LocalDateTime>(
              this, query, "completed").equal(value)
    }

    public fun id(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, ObjectId> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun requestedBy(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "requestedBy")
    }

    public fun requestedBy(value: String): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "requestedBy").equal(value)
    }

    public fun requestedOn(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, LocalDateTime> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.time.LocalDateTime>(
              this, query, "requestedOn")
    }

    public fun requestedOn(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.time.LocalDateTime>(
              this, query, "requestedOn").equal(value)
    }

    public fun state(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, State> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.AdminEvent.State>(
              this, query, "state")
    }

    public fun state(value: State): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.AdminEvent.State>(
              this, query, "state").equal(value)
    }

    public fun type(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, EventType> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.EventType>(
              this, query, "type")
    }

    public fun type(value: EventType): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.EventType>(
              this, query, "type").equal(value)
    }

    public fun getUpdater(): ApiEventUpdater {
        return ApiEventUpdater()
    }

    public inner class ApiEventUpdater {
        var updateOperations = ds.createUpdateOperations(ApiEvent::class.java)

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

        public fun adminDao(value: AdminDao): ApiEventUpdater {
            updateOperations.set("adminDao", value)
            return this
        }

        public fun unsetAdminDao(): ApiEventUpdater {
            updateOperations.unset("adminDao")
            return this
        }

        public fun apiDao(value: ApiDao): ApiEventUpdater {
            updateOperations.set("apiDao", value)
            return this
        }

        public fun unsetApiDao(): ApiEventUpdater {
            updateOperations.unset("apiDao")
            return this
        }

        public fun apiId(value: ObjectId): ApiEventUpdater {
            updateOperations.set("apiId", value)
            return this
        }

        public fun unsetApiId(): ApiEventUpdater {
            updateOperations.unset("apiId")
            return this
        }

        public fun baseUrl(value: String): ApiEventUpdater {
            updateOperations.set("baseUrl", value)
            return this
        }

        public fun unsetBaseUrl(): ApiEventUpdater {
            updateOperations.unset("baseUrl")
            return this
        }

        public fun downloadUrl(value: String): ApiEventUpdater {
            updateOperations.set("downloadUrl", value)
            return this
        }

        public fun unsetDownloadUrl(): ApiEventUpdater {
            updateOperations.unset("downloadUrl")
            return this
        }

        public fun ircBot(value: Provider<PircBotX>): ApiEventUpdater {
            updateOperations.set("ircBot", value)
            return this
        }

        public fun unsetIrcBot(): ApiEventUpdater {
            updateOperations.unset("ircBot")
            return this
        }

        public fun name(value: String): ApiEventUpdater {
            updateOperations.set("name", value)
            return this
        }

        public fun unsetName(): ApiEventUpdater {
            updateOperations.unset("name")
            return this
        }

        public fun parser(value: JavadocParser): ApiEventUpdater {
            updateOperations.set("parser", value)
            return this
        }

        public fun unsetParser(): ApiEventUpdater {
            updateOperations.unset("parser")
            return this
        }

        public fun bot(value: Javabot): ApiEventUpdater {
            updateOperations.set("bot", value)
            return this
        }

        public fun unsetBot(): ApiEventUpdater {
            updateOperations.unset("bot")
            return this
        }

        public fun completed(value: LocalDateTime): ApiEventUpdater {
            updateOperations.set("completed", value)
            return this
        }

        public fun unsetCompleted(): ApiEventUpdater {
            updateOperations.unset("completed")
            return this
        }

        public fun requestedBy(value: String): ApiEventUpdater {
            updateOperations.set("requestedBy", value)
            return this
        }

        public fun unsetRequestedBy(): ApiEventUpdater {
            updateOperations.unset("requestedBy")
            return this
        }

        public fun requestedOn(value: LocalDateTime): ApiEventUpdater {
            updateOperations.set("requestedOn", value)
            return this
        }

        public fun unsetRequestedOn(): ApiEventUpdater {
            updateOperations.unset("requestedOn")
            return this
        }

        public fun state(value: State): ApiEventUpdater {
            updateOperations.set("state", value)
            return this
        }

        public fun unsetState(): ApiEventUpdater {
            updateOperations.unset("state")
            return this
        }

        public fun type(value: EventType): ApiEventUpdater {
            updateOperations.set("type", value)
            return this
        }

        public fun unsetType(): ApiEventUpdater {
            updateOperations.unset("type")
            return this
        }
    }
}
