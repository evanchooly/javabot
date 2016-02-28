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

class ChangeCriteria(ds: Datastore) : BaseCriteria<Change>(ds, Change::class.java) {

    fun changeDate(): TypeSafeFieldEnd<ChangeCriteria, Change, LocalDateTime> {
        return TypeSafeFieldEnd<ChangeCriteria, Change, java.time.LocalDateTime>(
              this, query, "changeDate")
    }

    fun changeDate(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ChangeCriteria, Change, java.time.LocalDateTime>(
              this, query, "changeDate").equal(value)
    }

    fun id(): TypeSafeFieldEnd<ChangeCriteria, Change, ObjectId> {
        return TypeSafeFieldEnd<ChangeCriteria, Change, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ChangeCriteria, Change, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun message(): TypeSafeFieldEnd<ChangeCriteria, Change, String> {
        return TypeSafeFieldEnd<ChangeCriteria, Change, String>(
              this, query, "message")
    }

    fun message(value: String): Criteria {
        return TypeSafeFieldEnd<ChangeCriteria, Change, String>(
              this, query, "message").equal(value)
    }

    fun getUpdater(): ChangeUpdater {
        return ChangeUpdater()
    }

    inner class ChangeUpdater {
        var updateOperations = ds.createUpdateOperations(Change::class.java)

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

        fun changeDate(value: LocalDateTime): ChangeUpdater {
            updateOperations.set("changeDate", value)
            return this
        }

        fun unsetChangeDate(): ChangeUpdater {
            updateOperations.unset("changeDate")
            return this
        }

        fun message(value: String): ChangeUpdater {
            updateOperations.set("message", value)
            return this
        }

        fun unsetMessage(): ChangeUpdater {
            updateOperations.unset("message")
            return this
        }
    }
}
