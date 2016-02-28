package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.model.Logs
import javabot.model.Logs.Type
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import java.time.LocalDateTime

class LogsCriteria(ds: Datastore) : BaseCriteria<Logs>(ds, Logs::class.java) {

    fun channel(): TypeSafeFieldEnd<LogsCriteria, Logs, String> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "channel")
    }

    fun channel(value: String): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "channel").equal(value)
    }

    fun id(): TypeSafeFieldEnd<LogsCriteria, Logs, ObjectId> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun message(): TypeSafeFieldEnd<LogsCriteria, Logs, String> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "message")
    }

    fun message(value: String): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "message").equal(value)
    }

    fun nick(): TypeSafeFieldEnd<LogsCriteria, Logs, String> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "nick")
    }

    fun nick(value: String): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "nick").equal(value)
    }

    fun type(): TypeSafeFieldEnd<LogsCriteria, Logs, Type> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, javabot.model.Logs.Type>(
              this, query, "type")
    }

    fun type(value: Type): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, javabot.model.Logs.Type>(
              this, query, "type").equal(value)
    }

    fun updated(): TypeSafeFieldEnd<LogsCriteria, Logs, LocalDateTime> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, java.time.LocalDateTime>(
              this, query, "updated")
    }

    fun updated(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, java.time.LocalDateTime>(
              this, query, "updated").equal(value)
    }

    fun upperNick(): TypeSafeFieldEnd<LogsCriteria, Logs, String> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "upperNick")
    }

    fun upperNick(value: String): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "upperNick").equal(value)
    }

    fun getUpdater(): LogsUpdater {
        return LogsUpdater()
    }

    inner class LogsUpdater {
        var updateOperations = ds.createUpdateOperations(Logs::class.java)

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

        fun channel(value: String): LogsUpdater {
            updateOperations.set("channel", value)
            return this
        }

        fun unsetChannel(): LogsUpdater {
            updateOperations.unset("channel")
            return this
        }

        fun message(value: String): LogsUpdater {
            updateOperations.set("message", value)
            return this
        }

        fun unsetMessage(): LogsUpdater {
            updateOperations.unset("message")
            return this
        }

        fun nick(value: String): LogsUpdater {
            updateOperations.set("nick", value)
            return this
        }

        fun unsetNick(): LogsUpdater {
            updateOperations.unset("nick")
            return this
        }

        fun type(value: Type): LogsUpdater {
            updateOperations.set("type", value)
            return this
        }

        fun unsetType(): LogsUpdater {
            updateOperations.unset("type")
            return this
        }

        fun updated(value: LocalDateTime): LogsUpdater {
            updateOperations.set("updated", value)
            return this
        }

        fun unsetUpdated(): LogsUpdater {
            updateOperations.unset("updated")
            return this
        }

        fun upperNick(value: String): LogsUpdater {
            updateOperations.set("upperNick", value)
            return this
        }

        fun unsetUpperNick(): LogsUpdater {
            updateOperations.unset("upperNick")
            return this
        }
    }
}
