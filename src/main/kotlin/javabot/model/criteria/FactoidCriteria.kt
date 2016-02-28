package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.model.Factoid
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import java.time.LocalDateTime

class FactoidCriteria(ds: Datastore) : BaseCriteria<Factoid>(ds, Factoid::class.java) {

    fun id(): TypeSafeFieldEnd<FactoidCriteria, Factoid, ObjectId> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun lastUsed(): TypeSafeFieldEnd<FactoidCriteria, Factoid, LocalDateTime> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime>(
              this, query, "lastUsed")
    }

    fun lastUsed(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime>(
              this, query, "lastUsed").equal(value)
    }

    fun locked(): TypeSafeFieldEnd<FactoidCriteria, Factoid, Boolean> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, Boolean>(
              this, query, "locked")
    }

    fun locked(value: Boolean?): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, Boolean>(
              this, query, "locked").equal(value)
    }

    fun name(): TypeSafeFieldEnd<FactoidCriteria, Factoid, String> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "name")
    }

    fun name(value: String): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "name").equal(value)
    }

    fun updated(): TypeSafeFieldEnd<FactoidCriteria, Factoid, LocalDateTime> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime>(
              this, query, "updated")
    }

    fun updated(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime>(
              this, query, "updated").equal(value)
    }

    fun upperName(): TypeSafeFieldEnd<FactoidCriteria, Factoid, String> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "upperName")
    }

    fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "upperName").equal(value)
    }

    fun upperUserName(): TypeSafeFieldEnd<FactoidCriteria, Factoid, String> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "upperUserName")
    }

    fun upperUserName(value: String): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "upperUserName").equal(value)
    }

    fun upperValue(): TypeSafeFieldEnd<FactoidCriteria, Factoid, String> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "upperValue")
    }

    fun upperValue(value: String): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "upperValue").equal(value)
    }

    fun userName(): TypeSafeFieldEnd<FactoidCriteria, Factoid, String> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "userName")
    }

    fun userName(value: String): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "userName").equal(value)
    }

    fun value(): TypeSafeFieldEnd<FactoidCriteria, Factoid, String> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "value")
    }

    fun value(value: String): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "value").equal(value)
    }

    fun getUpdater(): FactoidUpdater {
        return FactoidUpdater()
    }

    inner class FactoidUpdater {
        var updateOperations = ds.createUpdateOperations(Factoid::class.java)

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

        fun lastUsed(value: LocalDateTime): FactoidUpdater {
            updateOperations.set("lastUsed", value)
            return this
        }

        fun unsetLastUsed(): FactoidUpdater {
            updateOperations.unset("lastUsed")
            return this
        }

        fun locked(value: Boolean?): FactoidUpdater {
            updateOperations.set("locked", value)
            return this
        }

        fun unsetLocked(): FactoidUpdater {
            updateOperations.unset("locked")
            return this
        }

        fun name(value: String): FactoidUpdater {
            updateOperations.set("name", value)
            return this
        }

        fun unsetName(): FactoidUpdater {
            updateOperations.unset("name")
            return this
        }

        fun updated(value: LocalDateTime): FactoidUpdater {
            updateOperations.set("updated", value)
            return this
        }

        fun unsetUpdated(): FactoidUpdater {
            updateOperations.unset("updated")
            return this
        }

        fun upperName(value: String): FactoidUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        fun unsetUpperName(): FactoidUpdater {
            updateOperations.unset("upperName")
            return this
        }

        fun upperUserName(value: String): FactoidUpdater {
            updateOperations.set("upperUserName", value)
            return this
        }

        fun unsetUpperUserName(): FactoidUpdater {
            updateOperations.unset("upperUserName")
            return this
        }

        fun upperValue(value: String): FactoidUpdater {
            updateOperations.set("upperValue", value)
            return this
        }

        fun unsetUpperValue(): FactoidUpdater {
            updateOperations.unset("upperValue")
            return this
        }

        fun userName(value: String): FactoidUpdater {
            updateOperations.set("userName", value)
            return this
        }

        fun unsetUserName(): FactoidUpdater {
            updateOperations.unset("userName")
            return this
        }

        fun value(value: String): FactoidUpdater {
            updateOperations.set("value", value)
            return this
        }

        fun unsetValue(): FactoidUpdater {
            updateOperations.unset("value")
            return this
        }
    }
}
