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

public class ShunCriteria(ds: Datastore) : BaseCriteria<Shun>(ds, Shun::class.java) {

    public fun expiry(): TypeSafeFieldEnd<ShunCriteria, Shun, LocalDateTime> {
        return TypeSafeFieldEnd<ShunCriteria, Shun, java.time.LocalDateTime>(
              this, query, "expiry")
    }

    public fun expiry(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ShunCriteria, Shun, java.time.LocalDateTime>(
              this, query, "expiry").equal(value)
    }

    public fun id(): TypeSafeFieldEnd<ShunCriteria, Shun, ObjectId> {
        return TypeSafeFieldEnd<ShunCriteria, Shun, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ShunCriteria, Shun, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun nick(): TypeSafeFieldEnd<ShunCriteria, Shun, String> {
        return TypeSafeFieldEnd<ShunCriteria, Shun, String>(this,
              query, "nick")
    }

    public fun nick(value: String): Criteria {
        return TypeSafeFieldEnd<ShunCriteria, Shun, String>(this,
              query, "nick").equal(value)
    }

    public fun upperNick(): TypeSafeFieldEnd<ShunCriteria, Shun, String> {
        return TypeSafeFieldEnd<ShunCriteria, Shun, String>(this,
              query, "upperNick")
    }

    public fun upperNick(value: String): Criteria {
        return TypeSafeFieldEnd<ShunCriteria, Shun, String>(this,
              query, "upperNick").equal(value)
    }

    public fun getUpdater(): ShunUpdater {
        return ShunUpdater()
    }

    public inner class ShunUpdater {
        var updateOperations = ds.createUpdateOperations(Shun::class.java)

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

        public fun expiry(value: LocalDateTime): ShunUpdater {
            updateOperations.set("expiry", value)
            return this
        }

        public fun unsetExpiry(): ShunUpdater {
            updateOperations.unset("expiry")
            return this
        }

        public fun nick(value: String): ShunUpdater {
            updateOperations.set("nick", value)
            return this
        }

        public fun unsetNick(): ShunUpdater {
            updateOperations.unset("nick")
            return this
        }

        public fun upperNick(value: String): ShunUpdater {
            updateOperations.set("upperNick", value)
            return this
        }

        public fun unsetUpperNick(): ShunUpdater {
            updateOperations.unset("upperNick")
            return this
        }
    }
}
