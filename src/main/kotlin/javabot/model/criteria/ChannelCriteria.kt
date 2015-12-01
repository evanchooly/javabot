package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.model.Channel
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import java.time.LocalDateTime

public class ChannelCriteria(ds: Datastore) : BaseCriteria<Channel>(ds, Channel::class.java) {

    public fun id(): TypeSafeFieldEnd<ChannelCriteria, Channel, ObjectId> {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun key(): TypeSafeFieldEnd<ChannelCriteria, Channel, String> {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, String>(
              this, query, "key")
    }

    public fun key(value: String): Criteria {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, String>(
              this, query, "key").equal(value)
    }

    public fun logged(): TypeSafeFieldEnd<ChannelCriteria, Channel, Boolean> {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, Boolean>(
              this, query, "logged")
    }

    public fun logged(value: Boolean?): Criteria {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, Boolean>(
              this, query, "logged").equal(value)
    }

    public fun name(): TypeSafeFieldEnd<ChannelCriteria, Channel, String> {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, String>(
              this, query, "name")
    }

    public fun name(value: String): Criteria {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, String>(
              this, query, "name").equal(value)
    }

    public fun updated(): TypeSafeFieldEnd<ChannelCriteria, Channel, LocalDateTime> {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, java.time.LocalDateTime>(
              this, query, "updated")
    }

    public fun updated(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, java.time.LocalDateTime>(
              this, query, "updated").equal(value)
    }

    public fun upperName(): TypeSafeFieldEnd<ChannelCriteria, Channel, String> {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, String>(
              this, query, "upperName")
    }

    public fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, String>(
              this, query, "upperName").equal(value)
    }

    public fun getUpdater(): ChannelUpdater {
        return ChannelUpdater()
    }

    public inner class ChannelUpdater {
        var updateOperations = ds.createUpdateOperations(Channel::class.java)

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

        public fun key(value: String): ChannelUpdater {
            updateOperations.set("key", value)
            return this
        }

        public fun unsetKey(): ChannelUpdater {
            updateOperations.unset("key")
            return this
        }

        public fun logged(value: Boolean?): ChannelUpdater {
            updateOperations.set("logged", value)
            return this
        }

        public fun unsetLogged(): ChannelUpdater {
            updateOperations.unset("logged")
            return this
        }

        public fun name(value: String): ChannelUpdater {
            updateOperations.set("name", value)
            return this
        }

        public fun unsetName(): ChannelUpdater {
            updateOperations.unset("name")
            return this
        }

        public fun updated(value: LocalDateTime): ChannelUpdater {
            updateOperations.set("updated", value)
            return this
        }

        public fun unsetUpdated(): ChannelUpdater {
            updateOperations.unset("updated")
            return this
        }

        public fun upperName(value: String): ChannelUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        public fun unsetUpperName(): ChannelUpdater {
            updateOperations.unset("upperName")
            return this
        }
    }
}
