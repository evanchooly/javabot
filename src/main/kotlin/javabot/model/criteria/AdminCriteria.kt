package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.model.Admin
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults
import java.time.LocalDateTime

class AdminCriteria(ds: Datastore) : BaseCriteria<Admin>(ds, Admin::class.java) {

    fun addedBy(): TypeSafeFieldEnd<AdminCriteria, Admin, String> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "addedBy")
    }

    fun addedBy(value: String): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "addedBy").equal(value)
    }

    fun botOwner(): TypeSafeFieldEnd<AdminCriteria, Admin, Boolean> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, Boolean>(
              this, query, "botOwner")
    }

    fun botOwner(value: Boolean?): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, Boolean>(
              this, query, "botOwner").equal(value)
    }

    fun emailAddress(): TypeSafeFieldEnd<AdminCriteria, Admin, String> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "emailAddress")
    }

    fun emailAddress(value: String): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "emailAddress").equal(value)
    }

    fun hostName(): TypeSafeFieldEnd<AdminCriteria, Admin, String> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "hostName")
    }

    fun hostName(value: String): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "hostName").equal(value)
    }

    fun id(): TypeSafeFieldEnd<AdminCriteria, Admin, ObjectId> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun ircName(): TypeSafeFieldEnd<AdminCriteria, Admin, String> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "ircName")
    }

    fun ircName(value: String): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "ircName").equal(value)
    }

    fun updated(): TypeSafeFieldEnd<AdminCriteria, Admin, LocalDateTime> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, java.time.LocalDateTime>(
              this, query, "updated")
    }

    fun updated(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, java.time.LocalDateTime>(
              this, query, "updated").equal(value)
    }

    fun getUpdater(): AdminUpdater {
        return AdminUpdater()
    }

    inner class AdminUpdater {
        var updateOperations = ds.createUpdateOperations(Admin::class.java)

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

        fun addedBy(value: String): AdminUpdater {
            updateOperations.set("addedBy", value)
            return this
        }

        fun unsetAddedBy(): AdminUpdater {
            updateOperations.unset("addedBy")
            return this
        }

        fun botOwner(value: Boolean?): AdminUpdater {
            updateOperations.set("botOwner", value)
            return this
        }

        fun unsetBotOwner(): AdminUpdater {
            updateOperations.unset("botOwner")
            return this
        }

        fun emailAddress(value: String): AdminUpdater {
            updateOperations.set("emailAddress", value)
            return this
        }

        fun unsetEmailAddress(): AdminUpdater {
            updateOperations.unset("emailAddress")
            return this
        }

        fun hostName(value: String): AdminUpdater {
            updateOperations.set("hostName", value)
            return this
        }

        fun unsetHostName(): AdminUpdater {
            updateOperations.unset("hostName")
            return this
        }

        fun ircName(value: String): AdminUpdater {
            updateOperations.set("ircName", value)
            return this
        }

        fun unsetIrcName(): AdminUpdater {
            updateOperations.unset("ircName")
            return this
        }

        fun updated(value: LocalDateTime): AdminUpdater {
            updateOperations.set("updated", value)
            return this
        }

        fun unsetUpdated(): AdminUpdater {
            updateOperations.unset("updated")
            return this
        }
    }
}
