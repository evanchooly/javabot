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

class ApiEventCriteria(ds: Datastore) : BaseCriteria<ApiEvent>(ds, ApiEvent::class.java) {

    fun adminDao(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, AdminDao> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.dao.AdminDao>(
              this, query, "adminDao")
    }

    fun adminDao(value: AdminDao): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.dao.AdminDao>(
              this, query, "adminDao").equal(value)
    }

    fun apiDao(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, ApiDao> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.dao.ApiDao>(
              this, query, "apiDao")
    }

    fun apiDao(value: ApiDao): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.dao.ApiDao>(
              this, query, "apiDao").equal(value)
    }

    fun apiId(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, ObjectId> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId>(
              this, query, "apiId")
    }

    fun apiId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId>(
              this, query, "apiId").equal(value)
    }

    fun baseUrl(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "baseUrl")
    }

    fun baseUrl(value: String): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "baseUrl").equal(value)
    }

    fun downloadUrl(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "downloadUrl")
    }

    fun downloadUrl(value: String): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "downloadUrl").equal(value)
    }

    fun ircBot(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, Provider<Any>> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, com.google.inject.Provider<Any>>(
              this, query, "ircBot")
    }

    fun name(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "name")
    }

    fun name(value: String): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "name").equal(value)
    }

    fun parser(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, JavadocParser> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.javadoc.JavadocParser>(
              this, query, "parser")
    }

    fun parser(value: JavadocParser): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.javadoc.JavadocParser>(
              this, query, "parser").equal(value)
    }

    fun bot(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, Javabot> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.Javabot>(
              this, query, "bot")
    }

    fun bot(value: Javabot): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.Javabot>(
              this, query, "bot").equal(value)
    }

    fun completed(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, LocalDateTime> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.time.LocalDateTime>(
              this, query, "completed")
    }

    fun completed(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.time.LocalDateTime>(
              this, query, "completed").equal(value)
    }

    fun id(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, ObjectId> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun requestedBy(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "requestedBy")
    }

    fun requestedBy(value: String): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, String>(
              this, query, "requestedBy").equal(value)
    }

    fun requestedOn(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, LocalDateTime> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.time.LocalDateTime>(
              this, query, "requestedOn")
    }

    fun requestedOn(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, java.time.LocalDateTime>(
              this, query, "requestedOn").equal(value)
    }

    fun state(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, State> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.AdminEvent.State>(
              this, query, "state")
    }

    fun state(value: State): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.AdminEvent.State>(
              this, query, "state").equal(value)
    }

    fun type(): TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, EventType> {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.EventType>(
              this, query, "type")
    }

    fun type(value: EventType): Criteria {
        return TypeSafeFieldEnd<ApiEventCriteria, ApiEvent, javabot.model.EventType>(
              this, query, "type").equal(value)
    }

    fun getUpdater(): ApiEventUpdater {
        return ApiEventUpdater()
    }

    inner class ApiEventUpdater {
        var updateOperations = ds.createUpdateOperations(ApiEvent::class.java)

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

        fun adminDao(value: AdminDao): ApiEventUpdater {
            updateOperations.set("adminDao", value)
            return this
        }

        fun unsetAdminDao(): ApiEventUpdater {
            updateOperations.unset("adminDao")
            return this
        }

        fun apiDao(value: ApiDao): ApiEventUpdater {
            updateOperations.set("apiDao", value)
            return this
        }

        fun unsetApiDao(): ApiEventUpdater {
            updateOperations.unset("apiDao")
            return this
        }

        fun apiId(value: ObjectId): ApiEventUpdater {
            updateOperations.set("apiId", value)
            return this
        }

        fun unsetApiId(): ApiEventUpdater {
            updateOperations.unset("apiId")
            return this
        }

        fun baseUrl(value: String): ApiEventUpdater {
            updateOperations.set("baseUrl", value)
            return this
        }

        fun unsetBaseUrl(): ApiEventUpdater {
            updateOperations.unset("baseUrl")
            return this
        }

        fun downloadUrl(value: String): ApiEventUpdater {
            updateOperations.set("downloadUrl", value)
            return this
        }

        fun unsetDownloadUrl(): ApiEventUpdater {
            updateOperations.unset("downloadUrl")
            return this
        }

        fun ircBot(value: Provider<PircBotX>): ApiEventUpdater {
            updateOperations.set("ircBot", value)
            return this
        }

        fun unsetIrcBot(): ApiEventUpdater {
            updateOperations.unset("ircBot")
            return this
        }

        fun name(value: String): ApiEventUpdater {
            updateOperations.set("name", value)
            return this
        }

        fun unsetName(): ApiEventUpdater {
            updateOperations.unset("name")
            return this
        }

        fun parser(value: JavadocParser): ApiEventUpdater {
            updateOperations.set("parser", value)
            return this
        }

        fun unsetParser(): ApiEventUpdater {
            updateOperations.unset("parser")
            return this
        }

        fun bot(value: Javabot): ApiEventUpdater {
            updateOperations.set("bot", value)
            return this
        }

        fun unsetBot(): ApiEventUpdater {
            updateOperations.unset("bot")
            return this
        }

        fun completed(value: LocalDateTime): ApiEventUpdater {
            updateOperations.set("completed", value)
            return this
        }

        fun unsetCompleted(): ApiEventUpdater {
            updateOperations.unset("completed")
            return this
        }

        fun requestedBy(value: String): ApiEventUpdater {
            updateOperations.set("requestedBy", value)
            return this
        }

        fun unsetRequestedBy(): ApiEventUpdater {
            updateOperations.unset("requestedBy")
            return this
        }

        fun requestedOn(value: LocalDateTime): ApiEventUpdater {
            updateOperations.set("requestedOn", value)
            return this
        }

        fun unsetRequestedOn(): ApiEventUpdater {
            updateOperations.unset("requestedOn")
            return this
        }

        fun state(value: State): ApiEventUpdater {
            updateOperations.set("state", value)
            return this
        }

        fun unsetState(): ApiEventUpdater {
            updateOperations.unset("state")
            return this
        }

        fun type(value: EventType): ApiEventUpdater {
            updateOperations.set("type", value)
            return this
        }

        fun unsetType(): ApiEventUpdater {
            updateOperations.unset("type")
            return this
        }
    }
}
