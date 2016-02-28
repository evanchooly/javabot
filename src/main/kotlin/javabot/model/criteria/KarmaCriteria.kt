package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.model.Karma
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import java.time.LocalDateTime

class KarmaCriteria(ds: Datastore) : BaseCriteria<Karma>(ds, Karma::class.java) {

    fun id(): TypeSafeFieldEnd<KarmaCriteria, Karma, ObjectId> {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun name(): TypeSafeFieldEnd<KarmaCriteria, Karma, String> {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, String>(
              this, query, "name")
    }

    fun name(value: String): Criteria {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, String>(
              this, query, "name").equal(value)
    }

    fun updated(): TypeSafeFieldEnd<KarmaCriteria, Karma, LocalDateTime> {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, java.time.LocalDateTime>(
              this, query, "updated")
    }

    fun updated(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, java.time.LocalDateTime>(
              this, query, "updated").equal(value)
    }

    fun upperName(): TypeSafeFieldEnd<KarmaCriteria, Karma, String> {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, String>(
              this, query, "upperName")
    }

    fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, String>(
              this, query, "upperName").equal(value)
    }

    fun userName(): TypeSafeFieldEnd<KarmaCriteria, Karma, String> {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, String>(
              this, query, "userName")
    }

    fun userName(value: String): Criteria {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, String>(
              this, query, "userName").equal(value)
    }

    fun value(): TypeSafeFieldEnd<KarmaCriteria, Karma, Int> {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, Int>(
              this, query, "value")
    }

    fun value(value: Int?): Criteria {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, Int>(
              this, query, "value").equal(value)
    }

    fun getUpdater(): KarmaUpdater {
        return KarmaUpdater()
    }

    inner class KarmaUpdater {
        var updateOperations = ds.createUpdateOperations(Karma::class.java)

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

        fun name(value: String): KarmaUpdater {
            updateOperations.set("name", value)
            return this
        }

        fun unsetName(): KarmaUpdater {
            updateOperations.unset("name")
            return this
        }

        fun updated(value: LocalDateTime): KarmaUpdater {
            updateOperations.set("updated", value)
            return this
        }

        fun unsetUpdated(): KarmaUpdater {
            updateOperations.unset("updated")
            return this
        }

        fun upperName(value: String): KarmaUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        fun unsetUpperName(): KarmaUpdater {
            updateOperations.unset("upperName")
            return this
        }

        fun userName(value: String): KarmaUpdater {
            updateOperations.set("userName", value)
            return this
        }

        fun unsetUserName(): KarmaUpdater {
            updateOperations.unset("userName")
            return this
        }

        fun value(value: Int?): KarmaUpdater {
            updateOperations.set("value", value)
            return this
        }

        fun unsetValue(): KarmaUpdater {
            updateOperations.unset("value")
            return this
        }

        fun decValue(): KarmaUpdater {
            updateOperations.dec("value")
            return this
        }

        fun incValue(): KarmaUpdater {
            updateOperations.inc("value")
            return this
        }

        fun incValue(value: Int?): KarmaUpdater {
            updateOperations.inc("value", value)
            return this
        }
    }
}
