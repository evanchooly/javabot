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

public class ThrottleItemCriteria(ds: Datastore) : BaseCriteria<ThrottleItem>(ds, ThrottleItem::class.java) {

    public fun id(): TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, ObjectId> {
        return TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun user(): TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, String> {
        return TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, String>(
              this, query, "user")
    }

    public fun user(value: String): Criteria {
        return TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, String>(
              this, query, "user").equal(value)
    }

    public fun `when`(): TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, Date> {
        return TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, java.util.Date>(
              this, query, "when")
    }

    public fun `when`(value: Date): Criteria {
        return TypeSafeFieldEnd<ThrottleItemCriteria, ThrottleItem, java.util.Date>(
              this, query, "when").equal(value)
    }

    public fun getUpdater(): ThrottleItemUpdater {
        return ThrottleItemUpdater()
    }

    public inner class ThrottleItemUpdater {
        var updateOperations = ds.createUpdateOperations(ThrottleItem::class.java)

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

        public fun user(value: String): ThrottleItemUpdater {
            updateOperations.set("user", value)
            return this
        }

        public fun unsetUser(): ThrottleItemUpdater {
            updateOperations.unset("user")
            return this
        }

        public fun `when`(value: Date): ThrottleItemUpdater {
            updateOperations.set("when", value)
            return this
        }

        public fun unsetWhen(): ThrottleItemUpdater {
            updateOperations.unset("when")
            return this
        }
    }
}
