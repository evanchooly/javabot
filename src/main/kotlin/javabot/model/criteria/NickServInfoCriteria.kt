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

class NickServInfoCriteria(ds: Datastore) : BaseCriteria<NickServInfo>(ds, NickServInfo::class.java) {

    fun account(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String>(
              this, query, "account")
    }

    fun account(value: String): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String>(
              this, query, "account").equal(value)
    }

    fun created(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, LocalDateTime> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "created")
    }

    fun created(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "created").equal(value)
    }

    fun extraneous(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, Map<Any, Any>> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, Map<Any, Any>>(
              this, query, "extraneous")
    }

    fun id(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, ObjectId> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun lastAddress(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String>(
              this, query, "lastAddress")
    }

    fun lastAddress(value: String): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String>(
              this, query, "lastAddress").equal(value)
    }

    fun lastSeen(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, LocalDateTime> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "lastSeen")
    }

    fun lastSeen(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "lastSeen").equal(value)
    }

    fun nick(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String>(
              this, query, "nick")
    }

    fun nick(value: String): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, String>(
              this, query, "nick").equal(value)
    }

    fun registered(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, LocalDateTime> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "registered")
    }

    fun registered(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "registered").equal(value)
    }

    fun userRegistered(): TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, LocalDateTime> {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "userRegistered")
    }

    fun userRegistered(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<NickServInfoCriteria, NickServInfo, java.time.LocalDateTime>(
              this, query, "userRegistered").equal(value)
    }

    fun getUpdater(): NickServInfoUpdater {
        return NickServInfoUpdater()
    }

    inner class NickServInfoUpdater {
        var updateOperations = ds.createUpdateOperations(NickServInfo::class.java)

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

        fun account(value: String): NickServInfoUpdater {
            updateOperations.set("account", value)
            return this
        }

        fun unsetAccount(): NickServInfoUpdater {
            updateOperations.unset("account")
            return this
        }

        fun created(value: LocalDateTime): NickServInfoUpdater {
            updateOperations.set("created", value)
            return this
        }

        fun unsetCreated(): NickServInfoUpdater {
            updateOperations.unset("created")
            return this
        }

        fun extraneous(value: Map<String, String>): NickServInfoUpdater {
            updateOperations.set("extraneous", value)
            return this
        }

        fun unsetExtraneous(): NickServInfoUpdater {
            updateOperations.unset("extraneous")
            return this
        }

        fun lastAddress(value: String): NickServInfoUpdater {
            updateOperations.set("lastAddress", value)
            return this
        }

        fun unsetLastAddress(): NickServInfoUpdater {
            updateOperations.unset("lastAddress")
            return this
        }

        fun lastSeen(value: LocalDateTime): NickServInfoUpdater {
            updateOperations.set("lastSeen", value)
            return this
        }

        fun unsetLastSeen(): NickServInfoUpdater {
            updateOperations.unset("lastSeen")
            return this
        }

        fun nick(value: String): NickServInfoUpdater {
            updateOperations.set("nick", value)
            return this
        }

        fun unsetNick(): NickServInfoUpdater {
            updateOperations.unset("nick")
            return this
        }

        fun registered(value: LocalDateTime): NickServInfoUpdater {
            updateOperations.set("registered", value)
            return this
        }

        fun unsetRegistered(): NickServInfoUpdater {
            updateOperations.unset("registered")
            return this
        }

        fun userRegistered(value: LocalDateTime): NickServInfoUpdater {
            updateOperations.set("userRegistered", value)
            return this
        }

        fun unsetUserRegistered(): NickServInfoUpdater {
            updateOperations.unset("userRegistered")
            return this
        }
    }
}
