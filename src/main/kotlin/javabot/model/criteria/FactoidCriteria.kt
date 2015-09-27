package javabot.model.criteria

import com.antwerkz.critter.criteria.BaseCriteria
import javabot.model.Factoid
import org.mongodb.morphia.Datastore
import org.bson.types.ObjectId
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.FieldEndImpl
import org.mongodb.morphia.query.QueryImpl
import com.antwerkz.critter.TypeSafeFieldEnd
import java.time.LocalDateTime
import org.mongodb.morphia.query.UpdateOperations
import org.mongodb.morphia.query.UpdateResults
import com.mongodb.WriteConcern
import com.mongodb.WriteResult

public class FactoidCriteria(ds: Datastore) : BaseCriteria<Factoid>(ds, Factoid::class.java) {

    public fun id(): TypeSafeFieldEnd<FactoidCriteria, Factoid, ObjectId> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun lastUsed(): TypeSafeFieldEnd<FactoidCriteria, Factoid, LocalDateTime> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime>(
              this, query, "lastUsed")
    }

    public fun lastUsed(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime>(
              this, query, "lastUsed").equal(value)
    }

    public fun locked(): TypeSafeFieldEnd<FactoidCriteria, Factoid, Boolean> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, Boolean>(
              this, query, "locked")
    }

    public fun locked(value: Boolean?): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, Boolean>(
              this, query, "locked").equal(value)
    }

    public fun name(): TypeSafeFieldEnd<FactoidCriteria, Factoid, String> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "name")
    }

    public fun name(value: String): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "name").equal(value)
    }

    public fun updated(): TypeSafeFieldEnd<FactoidCriteria, Factoid, LocalDateTime> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime>(
              this, query, "updated")
    }

    public fun updated(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, java.time.LocalDateTime>(
              this, query, "updated").equal(value)
    }

    public fun upperName(): TypeSafeFieldEnd<FactoidCriteria, Factoid, String> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "upperName")
    }

    public fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "upperName").equal(value)
    }

    public fun upperUserName(): TypeSafeFieldEnd<FactoidCriteria, Factoid, String> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "upperUserName")
    }

    public fun upperUserName(value: String): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "upperUserName").equal(value)
    }

    public fun upperValue(): TypeSafeFieldEnd<FactoidCriteria, Factoid, String> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "upperValue")
    }

    public fun upperValue(value: String): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "upperValue").equal(value)
    }

    public fun userName(): TypeSafeFieldEnd<FactoidCriteria, Factoid, String> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "userName")
    }

    public fun userName(value: String): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "userName").equal(value)
    }

    public fun value(): TypeSafeFieldEnd<FactoidCriteria, Factoid, String> {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "value")
    }

    public fun value(value: String): Criteria {
        return TypeSafeFieldEnd<FactoidCriteria, Factoid, String>(
              this, query, "value").equal(value)
    }

    public fun getUpdater(): FactoidUpdater {
        return FactoidUpdater()
    }

    public inner class FactoidUpdater {
        var updateOperations = ds.createUpdateOperations(Factoid::class.java)

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

        public fun lastUsed(value: LocalDateTime): FactoidUpdater {
            updateOperations.set("lastUsed", value)
            return this
        }

        public fun unsetLastUsed(): FactoidUpdater {
            updateOperations.unset("lastUsed")
            return this
        }

        public fun locked(value: Boolean?): FactoidUpdater {
            updateOperations.set("locked", value)
            return this
        }

        public fun unsetLocked(): FactoidUpdater {
            updateOperations.unset("locked")
            return this
        }

        public fun name(value: String): FactoidUpdater {
            updateOperations.set("name", value)
            return this
        }

        public fun unsetName(): FactoidUpdater {
            updateOperations.unset("name")
            return this
        }

        public fun updated(value: LocalDateTime): FactoidUpdater {
            updateOperations.set("updated", value)
            return this
        }

        public fun unsetUpdated(): FactoidUpdater {
            updateOperations.unset("updated")
            return this
        }

        public fun upperName(value: String): FactoidUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        public fun unsetUpperName(): FactoidUpdater {
            updateOperations.unset("upperName")
            return this
        }

        public fun upperUserName(value: String): FactoidUpdater {
            updateOperations.set("upperUserName", value)
            return this
        }

        public fun unsetUpperUserName(): FactoidUpdater {
            updateOperations.unset("upperUserName")
            return this
        }

        public fun upperValue(value: String): FactoidUpdater {
            updateOperations.set("upperValue", value)
            return this
        }

        public fun unsetUpperValue(): FactoidUpdater {
            updateOperations.unset("upperValue")
            return this
        }

        public fun userName(value: String): FactoidUpdater {
            updateOperations.set("userName", value)
            return this
        }

        public fun unsetUserName(): FactoidUpdater {
            updateOperations.unset("userName")
            return this
        }

        public fun value(value: String): FactoidUpdater {
            updateOperations.set("value", value)
            return this
        }

        public fun unsetValue(): FactoidUpdater {
            updateOperations.unset("value")
            return this
        }
    }
}
