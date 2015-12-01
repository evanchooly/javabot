package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.model.Change
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import java.time.LocalDateTime

public class ChangeCriteria(ds: Datastore) : BaseCriteria<Change>(ds, Change::class.java) {

    public fun changeDate(): TypeSafeFieldEnd<ChangeCriteria, Change, LocalDateTime> {
        return TypeSafeFieldEnd<ChangeCriteria, Change, java.time.LocalDateTime>(
              this, query, "changeDate")
    }

    public fun changeDate(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ChangeCriteria, Change, java.time.LocalDateTime>(
              this, query, "changeDate").equal(value)
    }

    public fun id(): TypeSafeFieldEnd<ChangeCriteria, Change, ObjectId> {
        return TypeSafeFieldEnd<ChangeCriteria, Change, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ChangeCriteria, Change, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun message(): TypeSafeFieldEnd<ChangeCriteria, Change, String> {
        return TypeSafeFieldEnd<ChangeCriteria, Change, String>(
              this, query, "message")
    }

    public fun message(value: String): Criteria {
        return TypeSafeFieldEnd<ChangeCriteria, Change, String>(
              this, query, "message").equal(value)
    }

    public fun getUpdater(): ChangeUpdater {
        return ChangeUpdater()
    }

    public inner class ChangeUpdater {
        var updateOperations = ds.createUpdateOperations(Change::class.java)

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

        public fun changeDate(value: LocalDateTime): ChangeUpdater {
            updateOperations.set("changeDate", value)
            return this
        }

        public fun unsetChangeDate(): ChangeUpdater {
            updateOperations.unset("changeDate")
            return this
        }

        public fun message(value: String): ChangeUpdater {
            updateOperations.set("message", value)
            return this
        }

        public fun unsetMessage(): ChangeUpdater {
            updateOperations.unset("message")
            return this
        }
    }
}
