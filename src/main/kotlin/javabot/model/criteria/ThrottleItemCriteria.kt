package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.model.ThrottleItem
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import java.util.Date

class ThrottleItemCriteria(ds: Datastore) : BaseCriteria<ThrottleItem>(ds, ThrottleItem::class.java) {

    fun id(): TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, ObjectId> {
        return TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun user(): TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, String> {
        return TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, String>(
              this, query, "user")
    }

    fun user(value: String): Criteria {
        return TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, String>(
              this, query, "user").equal(value)
    }

    fun `when`(): TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, Date> {
        return TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, java.util.Date>(
              this, query, "when")
    }

    fun `when`(value: Date): Criteria {
        return TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, java.util.Date>(
              this, query, "when").equal(value)
    }

    fun getUpdater(): ThrottleItemUpdater {
        return ThrottleItemUpdater()
    }

    inner class ThrottleItemUpdater {
        var updateOperations = ds.createUpdateOperations(ThrottleItem::class.java)

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

        fun user(value: String): ThrottleItemUpdater {
            updateOperations.set("user", value)
            return this
        }

        fun unsetUser(): ThrottleItemUpdater {
            updateOperations.unset("user")
            return this
        }

        fun `when`(value: Date): ThrottleItemUpdater {
            updateOperations.set("when", value)
            return this
        }

        fun unsetWhen(): ThrottleItemUpdater {
            updateOperations.unset("when")
            return this
        }
    }
}
