package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.model.Shun
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import java.time.LocalDateTime

class ShunCriteria(ds: Datastore) : BaseCriteria<Shun>(ds, Shun::class.java) {

    fun expiry(): TypeSafeFieldEnd<ShunCriteria, Shun, LocalDateTime> {
        return TypeSafeFieldEnd<ShunCriteria, Shun, java.time.LocalDateTime>(
              this, query, "expiry")
    }

    fun expiry(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ShunCriteria, Shun, java.time.LocalDateTime>(
              this, query, "expiry").equal(value)
    }

    fun id(): TypeSafeFieldEnd<ShunCriteria, Shun, ObjectId> {
        return TypeSafeFieldEnd<ShunCriteria, Shun, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ShunCriteria, Shun, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun nick(): TypeSafeFieldEnd<ShunCriteria, Shun, String> {
        return TypeSafeFieldEnd<ShunCriteria, Shun, String>(this,
              query, "nick")
    }

    fun nick(value: String): Criteria {
        return TypeSafeFieldEnd<ShunCriteria, Shun, String>(this,
              query, "nick").equal(value)
    }

    fun upperNick(): TypeSafeFieldEnd<ShunCriteria, Shun, String> {
        return TypeSafeFieldEnd<ShunCriteria, Shun, String>(this,
              query, "upperNick")
    }

    fun upperNick(value: String): Criteria {
        return TypeSafeFieldEnd<ShunCriteria, Shun, String>(this,
              query, "upperNick").equal(value)
    }

    fun getUpdater(): ShunUpdater {
        return ShunUpdater()
    }

    inner class ShunUpdater {
        var updateOperations = ds.createUpdateOperations(Shun::class.java)

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

        fun expiry(value: LocalDateTime): ShunUpdater {
            updateOperations.set("expiry", value)
            return this
        }

        fun unsetExpiry(): ShunUpdater {
            updateOperations.unset("expiry")
            return this
        }

        fun nick(value: String): ShunUpdater {
            updateOperations.set("nick", value)
            return this
        }

        fun unsetNick(): ShunUpdater {
            updateOperations.unset("nick")
            return this
        }

        fun upperNick(value: String): ShunUpdater {
            updateOperations.set("upperNick", value)
            return this
        }

        fun unsetUpperNick(): ShunUpdater {
            updateOperations.unset("upperNick")
            return this
        }
    }
}
