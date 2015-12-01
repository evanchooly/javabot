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

public class AdminCriteria(ds: Datastore) : BaseCriteria<Admin>(ds, Admin::class.java) {

    public fun addedBy(): TypeSafeFieldEnd<AdminCriteria, Admin, String> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "addedBy")
    }

    public fun addedBy(value: String): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "addedBy").equal(value)
    }

    public fun botOwner(): TypeSafeFieldEnd<AdminCriteria, Admin, Boolean> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, Boolean>(
              this, query, "botOwner")
    }

    public fun botOwner(value: Boolean?): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, Boolean>(
              this, query, "botOwner").equal(value)
    }

    public fun emailAddress(): TypeSafeFieldEnd<AdminCriteria, Admin, String> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "emailAddress")
    }

    public fun emailAddress(value: String): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "emailAddress").equal(value)
    }

    public fun hostName(): TypeSafeFieldEnd<AdminCriteria, Admin, String> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "hostName")
    }

    public fun hostName(value: String): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "hostName").equal(value)
    }

    public fun id(): TypeSafeFieldEnd<AdminCriteria, Admin, ObjectId> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun ircName(): TypeSafeFieldEnd<AdminCriteria, Admin, String> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "ircName")
    }

    public fun ircName(value: String): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, String>(
              this, query, "ircName").equal(value)
    }

    public fun updated(): TypeSafeFieldEnd<AdminCriteria, Admin, LocalDateTime> {
        return TypeSafeFieldEnd<AdminCriteria, Admin, java.time.LocalDateTime>(
              this, query, "updated")
    }

    public fun updated(value: LocalDateTime): Criteria {
        return TypeSafeFieldEnd<AdminCriteria, Admin, java.time.LocalDateTime>(
              this, query, "updated").equal(value)
    }

    public fun getUpdater(): AdminUpdater {
        return AdminUpdater()
    }

    public inner class AdminUpdater {
        var updateOperations = ds.createUpdateOperations(Admin::class.java)

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

        public fun addedBy(value: String): AdminUpdater {
            updateOperations.set("addedBy", value)
            return this
        }

        public fun unsetAddedBy(): AdminUpdater {
            updateOperations.unset("addedBy")
            return this
        }

        public fun botOwner(value: Boolean?): AdminUpdater {
            updateOperations.set("botOwner", value)
            return this
        }

        public fun unsetBotOwner(): AdminUpdater {
            updateOperations.unset("botOwner")
            return this
        }

        public fun emailAddress(value: String): AdminUpdater {
            updateOperations.set("emailAddress", value)
            return this
        }

        public fun unsetEmailAddress(): AdminUpdater {
            updateOperations.unset("emailAddress")
            return this
        }

        public fun hostName(value: String): AdminUpdater {
            updateOperations.set("hostName", value)
            return this
        }

        public fun unsetHostName(): AdminUpdater {
            updateOperations.unset("hostName")
            return this
        }

        public fun ircName(value: String): AdminUpdater {
            updateOperations.set("ircName", value)
            return this
        }

        public fun unsetIrcName(): AdminUpdater {
            updateOperations.unset("ircName")
            return this
        }

        public fun updated(value: LocalDateTime): AdminUpdater {
            updateOperations.set("updated", value)
            return this
        }

        public fun unsetUpdated(): AdminUpdater {
            updateOperations.unset("updated")
            return this
        }
    }
}
