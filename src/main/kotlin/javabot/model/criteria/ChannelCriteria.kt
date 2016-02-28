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

class ChannelCriteria(ds: Datastore) : BaseCriteria<Channel>(ds, Channel::class.java) {

    fun id(): TypeSafeFieldEnd<ChannelCriteria, Channel, ObjectId> {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun key(): TypeSafeFieldEnd<ChannelCriteria, Channel, String> {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, String>(
              this, query, "key")
    }

    fun key(value: String): Criteria {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, String>(
              this, query, "key").equal(value)
    }

    fun logged(): TypeSafeFieldEnd<ChannelCriteria, Channel, Boolean> {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, Boolean>(
              this, query, "logged")
    }

    fun logged(value: Boolean?): Criteria {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, Boolean>(
              this, query, "logged").equal(value)
    }

    fun name(): TypeSafeFieldEnd<ChannelCriteria, Channel, String> {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, String>(
              this, query, "name")
    }

    fun name(value: String): Criteria {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, String>(
              this, query, "name").equal(value)
    }

    fun updated(): TypeSafeFieldEnd<ChannelCriteria, Channel, LocalDateTime> {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, java.time.LocalDateTime>(
              this, query, "updated")
    }

    fun updated(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, java.time.LocalDateTime>(
              this, query, "updated").equal(value)
    }

    fun upperName(): TypeSafeFieldEnd<ChannelCriteria, Channel, String> {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, String>(
              this, query, "upperName")
    }

    fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<ChannelCriteria, Channel, String>(
              this, query, "upperName").equal(value)
    }

    fun getUpdater(): ChannelUpdater {
        return ChannelUpdater()
    }

    inner class ChannelUpdater {
        var updateOperations = ds.createUpdateOperations(Channel::class.java)

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

        fun key(value: String): ChannelUpdater {
            updateOperations.set("key", value)
            return this
        }

        fun unsetKey(): ChannelUpdater {
            updateOperations.unset("key")
            return this
        }

        fun logged(value: Boolean?): ChannelUpdater {
            updateOperations.set("logged", value)
            return this
        }

        fun unsetLogged(): ChannelUpdater {
            updateOperations.unset("logged")
            return this
        }

        fun name(value: String): ChannelUpdater {
            updateOperations.set("name", value)
            return this
        }

        fun unsetName(): ChannelUpdater {
            updateOperations.unset("name")
            return this
        }

        fun updated(value: LocalDateTime): ChannelUpdater {
            updateOperations.set("updated", value)
            return this
        }

        fun unsetUpdated(): ChannelUpdater {
            updateOperations.unset("updated")
            return this
        }

        fun upperName(value: String): ChannelUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        fun unsetUpperName(): ChannelUpdater {
            updateOperations.unset("upperName")
            return this
        }
    }
}
