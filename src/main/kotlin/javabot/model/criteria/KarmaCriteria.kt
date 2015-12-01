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

public class KarmaCriteria(ds: Datastore) : BaseCriteria<Karma>(ds, Karma::class.java) {

    public fun id(): TypeSafeFieldEnd<KarmaCriteria, Karma, ObjectId> {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun name(): TypeSafeFieldEnd<KarmaCriteria, Karma, String> {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, String>(
              this, query, "name")
    }

    public fun name(value: String): Criteria {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, String>(
              this, query, "name").equal(value)
    }

    public fun updated(): TypeSafeFieldEnd<KarmaCriteria, Karma, LocalDateTime> {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, java.time.LocalDateTime>(
              this, query, "updated")
    }

    public fun updated(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, java.time.LocalDateTime>(
              this, query, "updated").equal(value)
    }

    public fun upperName(): TypeSafeFieldEnd<KarmaCriteria, Karma, String> {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, String>(
              this, query, "upperName")
    }

    public fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, String>(
              this, query, "upperName").equal(value)
    }

    public fun userName(): TypeSafeFieldEnd<KarmaCriteria, Karma, String> {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, String>(
              this, query, "userName")
    }

    public fun userName(value: String): Criteria {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, String>(
              this, query, "userName").equal(value)
    }

    public fun value(): TypeSafeFieldEnd<KarmaCriteria, Karma, Int> {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, Int>(
              this, query, "value")
    }

    public fun value(value: Int?): Criteria {
        return TypeSafeFieldEnd<KarmaCriteria, Karma, Int>(
              this, query, "value").equal(value)
    }

    public fun getUpdater(): KarmaUpdater {
        return KarmaUpdater()
    }

    public inner class KarmaUpdater {
        var updateOperations = ds.createUpdateOperations(Karma::class.java)

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

        public fun name(value: String): KarmaUpdater {
            updateOperations.set("name", value)
            return this
        }

        public fun unsetName(): KarmaUpdater {
            updateOperations.unset("name")
            return this
        }

        public fun updated(value: LocalDateTime): KarmaUpdater {
            updateOperations.set("updated", value)
            return this
        }

        public fun unsetUpdated(): KarmaUpdater {
            updateOperations.unset("updated")
            return this
        }

        public fun upperName(value: String): KarmaUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        public fun unsetUpperName(): KarmaUpdater {
            updateOperations.unset("upperName")
            return this
        }

        public fun userName(value: String): KarmaUpdater {
            updateOperations.set("userName", value)
            return this
        }

        public fun unsetUserName(): KarmaUpdater {
            updateOperations.unset("userName")
            return this
        }

        public fun value(value: Int?): KarmaUpdater {
            updateOperations.set("value", value)
            return this
        }

        public fun unsetValue(): KarmaUpdater {
            updateOperations.unset("value")
            return this
        }

        public fun decValue(): KarmaUpdater {
            updateOperations.dec("value")
            return this
        }

        public fun incValue(): KarmaUpdater {
            updateOperations.inc("value")
            return this
        }

        public fun incValue(value: Int?): KarmaUpdater {
            updateOperations.inc("value", value)
            return this
        }
    }
}
