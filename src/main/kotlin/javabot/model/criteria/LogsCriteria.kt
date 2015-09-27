package javabot.model.criteria

import com.antwerkz.critter.criteria.BaseCriteria
import javabot.model.Logs
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.FieldEndImpl
import org.mongodb.morphia.query.QueryImpl
import com.antwerkz.critter.TypeSafeFieldEnd
import org.bson.types.ObjectId
import javabot.model.Logs.Type
import java.time.LocalDateTime
import org.mongodb.morphia.query.UpdateOperations
import org.mongodb.morphia.query.UpdateResults
import com.mongodb.WriteConcern
import com.mongodb.WriteResult

public class LogsCriteria(ds: Datastore) : BaseCriteria<Logs>(ds, Logs::class.java) {

    public fun channel(): TypeSafeFieldEnd<LogsCriteria, Logs, String> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "channel")
    }

    public fun channel(value: String): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "channel").equal(value)
    }

    public fun id(): TypeSafeFieldEnd<LogsCriteria, Logs, ObjectId> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun message(): TypeSafeFieldEnd<LogsCriteria, Logs, String> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "message")
    }

    public fun message(value: String): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "message").equal(value)
    }

    public fun nick(): TypeSafeFieldEnd<LogsCriteria, Logs, String> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "nick")
    }

    public fun nick(value: String): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "nick").equal(value)
    }

    public fun type(): TypeSafeFieldEnd<LogsCriteria, Logs, Type> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, javabot.model.Logs.Type>(
              this, query, "type")
    }

    public fun type(value: Type): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, javabot.model.Logs.Type>(
              this, query, "type").equal(value)
    }

    public fun updated(): TypeSafeFieldEnd<LogsCriteria, Logs, LocalDateTime> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, java.time.LocalDateTime>(
              this, query, "updated")
    }

    public fun updated(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, java.time.LocalDateTime>(
              this, query, "updated").equal(value)
    }

    public fun upperNick(): TypeSafeFieldEnd<LogsCriteria, Logs, String> {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "upperNick")
    }

    public fun upperNick(value: String): Criteria {
        return TypeSafeFieldEnd<LogsCriteria, Logs, String>(this,
              query, "upperNick").equal(value)
    }

    public fun getUpdater(): LogsUpdater {
        return LogsUpdater()
    }

    public inner class LogsUpdater {
        var updateOperations = ds.createUpdateOperations(Logs::class.java)

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

        public fun channel(value: String): LogsUpdater {
            updateOperations.set("channel", value)
            return this
        }

        public fun unsetChannel(): LogsUpdater {
            updateOperations.unset("channel")
            return this
        }

        public fun message(value: String): LogsUpdater {
            updateOperations.set("message", value)
            return this
        }

        public fun unsetMessage(): LogsUpdater {
            updateOperations.unset("message")
            return this
        }

        public fun nick(value: String): LogsUpdater {
            updateOperations.set("nick", value)
            return this
        }

        public fun unsetNick(): LogsUpdater {
            updateOperations.unset("nick")
            return this
        }

        public fun type(value: Type): LogsUpdater {
            updateOperations.set("type", value)
            return this
        }

        public fun unsetType(): LogsUpdater {
            updateOperations.unset("type")
            return this
        }

        public fun updated(value: LocalDateTime): LogsUpdater {
            updateOperations.set("updated", value)
            return this
        }

        public fun unsetUpdated(): LogsUpdater {
            updateOperations.unset("updated")
            return this
        }

        public fun upperNick(value: String): LogsUpdater {
            updateOperations.set("upperNick", value)
            return this
        }

        public fun unsetUpperNick(): LogsUpdater {
            updateOperations.unset("upperNick")
            return this
        }
    }
}
