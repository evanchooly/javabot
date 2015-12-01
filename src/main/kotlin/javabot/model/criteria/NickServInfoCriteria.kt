package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.model.NickServInfo
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import java.time.LocalDateTime

public class NickServInfoCriteria(ds: Datastore) : BaseCriteria<NickServInfo>(ds, NickServInfo::class.java) {

    public fun account(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String>(
              this, query, "account")
    }

    public fun account(value: String): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String>(
              this, query, "account").equal(value)
    }

    public fun created(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, LocalDateTime> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "created")
    }

    public fun created(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "created").equal(value)
    }

    public fun extraneous(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, Map<Any, Any>> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, Map<Any, Any>>(
              this, query, "extraneous")
    }

    public fun id(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, ObjectId> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun lastAddress(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String>(
              this, query, "lastAddress")
    }

    public fun lastAddress(value: String): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String>(
              this, query, "lastAddress").equal(value)
    }

    public fun lastSeen(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, LocalDateTime> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "lastSeen")
    }

    public fun lastSeen(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "lastSeen").equal(value)
    }

    public fun nick(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String>(
              this, query, "nick")
    }

    public fun nick(value: String): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String>(
              this, query, "nick").equal(value)
    }

    public fun registered(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, LocalDateTime> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "registered")
    }

    public fun registered(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "registered").equal(value)
    }

    public fun userRegistered(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, LocalDateTime> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "userRegistered")
    }

    public fun userRegistered(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "userRegistered").equal(value)
    }

    public fun getUpdater(): NickServInfoUpdater {
        return NickServInfoUpdater()
    }

    public inner class NickServInfoUpdater {
        var updateOperations = ds.createUpdateOperations(NickServInfo::class.java)

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

        public fun account(value: String): NickServInfoUpdater {
            updateOperations.set("account", value)
            return this
        }

        public fun unsetAccount(): NickServInfoUpdater {
            updateOperations.unset("account")
            return this
        }

        public fun created(value: LocalDateTime): NickServInfoUpdater {
            updateOperations.set("created", value)
            return this
        }

        public fun unsetCreated(): NickServInfoUpdater {
            updateOperations.unset("created")
            return this
        }

        public fun extraneous(value: Map<String, String>): NickServInfoUpdater {
            updateOperations.set("extraneous", value)
            return this
        }

        public fun unsetExtraneous(): NickServInfoUpdater {
            updateOperations.unset("extraneous")
            return this
        }

        public fun lastAddress(value: String): NickServInfoUpdater {
            updateOperations.set("lastAddress", value)
            return this
        }

        public fun unsetLastAddress(): NickServInfoUpdater {
            updateOperations.unset("lastAddress")
            return this
        }

        public fun lastSeen(value: LocalDateTime): NickServInfoUpdater {
            updateOperations.set("lastSeen", value)
            return this
        }

        public fun unsetLastSeen(): NickServInfoUpdater {
            updateOperations.unset("lastSeen")
            return this
        }

        public fun nick(value: String): NickServInfoUpdater {
            updateOperations.set("nick", value)
            return this
        }

        public fun unsetNick(): NickServInfoUpdater {
            updateOperations.unset("nick")
            return this
        }

        public fun registered(value: LocalDateTime): NickServInfoUpdater {
            updateOperations.set("registered", value)
            return this
        }

        public fun unsetRegistered(): NickServInfoUpdater {
            updateOperations.unset("registered")
            return this
        }

        public fun userRegistered(value: LocalDateTime): NickServInfoUpdater {
            updateOperations.set("userRegistered", value)
            return this
        }

        public fun unsetUserRegistered(): NickServInfoUpdater {
            updateOperations.unset("userRegistered")
            return this
        }
    }
}
