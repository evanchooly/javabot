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

public class NickRegistrationCriteria(ds: Datastore) : BaseCriteria<NickRegistration>(ds, NickRegistration::class.java) {

    public fun host(): TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String> {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "host")
    }

    public fun host(value: String): Criteria {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "host").equal(value)
    }

    public fun id(): TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, ObjectId> {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun nick(): TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String> {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "nick")
    }

    public fun nick(value: String): Criteria {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "nick").equal(value)
    }

    public fun twitterName(): TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String> {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "twitterName")
    }

    public fun twitterName(value: String): Criteria {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "twitterName").equal(value)
    }

    public fun url(): TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String> {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "url")
    }

    public fun url(value: String): Criteria {
        return TypeSafeFieldEnd<NickRegistrationCriteria, NickRegistration, String>(
              this, query, "url").equal(value)
    }

    public fun getUpdater(): NickRegistrationUpdater {
        return NickRegistrationUpdater()
    }

    public inner class NickRegistrationUpdater {
        var updateOperations = ds.createUpdateOperations(NickRegistration::class.java)

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

        public fun host(value: String): NickRegistrationUpdater {
            updateOperations.set("host", value)
            return this
        }

        public fun unsetHost(): NickRegistrationUpdater {
            updateOperations.unset("host")
            return this
        }

        public fun nick(value: String): NickRegistrationUpdater {
            updateOperations.set("nick", value)
            return this
        }

        public fun unsetNick(): NickRegistrationUpdater {
            updateOperations.unset("nick")
            return this
        }

        public fun twitterName(value: String): NickRegistrationUpdater {
            updateOperations.set("twitterName", value)
            return this
        }

        public fun unsetTwitterName(): NickRegistrationUpdater {
            updateOperations.unset("twitterName")
            return this
        }

        public fun url(value: String): NickRegistrationUpdater {
            updateOperations.set("url", value)
            return this
        }

        public fun unsetUrl(): NickRegistrationUpdater {
            updateOperations.unset("url")
            return this
        }
    }
}
