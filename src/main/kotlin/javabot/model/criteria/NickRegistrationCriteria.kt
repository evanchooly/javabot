package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.model.NickRegistration
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults

class NickRegistrationCriteria(ds: Datastore) : BaseCriteria<NickRegistration>(ds, NickRegistration::class.java) {

    fun host(): TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String> {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "host")
    }

    fun host(value: String): Criteria {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "host").equal(value)
    }

    fun id(): TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, ObjectId> {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun nick(): TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String> {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "nick")
    }

    fun nick(value: String): Criteria {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "nick").equal(value)
    }

    fun twitterName(): TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String> {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "twitterName")
    }

    fun twitterName(value: String): Criteria {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "twitterName").equal(value)
    }

    fun url(): TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String> {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "url")
    }

    fun url(value: String): Criteria {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "url").equal(value)
    }

    fun getUpdater(): NickRegistrationUpdater {
        return NickRegistrationUpdater()
    }

    inner class NickRegistrationUpdater {
        var updateOperations = ds.createUpdateOperations(NickRegistration::class.java)

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

        fun host(value: String): NickRegistrationUpdater {
            updateOperations.set("host", value)
            return this
        }

        fun unsetHost(): NickRegistrationUpdater {
            updateOperations.unset("host")
            return this
        }

        fun nick(value: String): NickRegistrationUpdater {
            updateOperations.set("nick", value)
            return this
        }

        fun unsetNick(): NickRegistrationUpdater {
            updateOperations.unset("nick")
            return this
        }

        fun twitterName(value: String): NickRegistrationUpdater {
            updateOperations.set("twitterName", value)
            return this
        }

        fun unsetTwitterName(): NickRegistrationUpdater {
            updateOperations.unset("twitterName")
            return this
        }

        fun url(value: String): NickRegistrationUpdater {
            updateOperations.set("url", value)
            return this
        }

        fun unsetUrl(): NickRegistrationUpdater {
            updateOperations.unset("url")
            return this
        }
    }
}
