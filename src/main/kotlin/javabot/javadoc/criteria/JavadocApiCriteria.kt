package javabot.javadoc.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.javadoc.JavadocApi
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults

public class JavadocApiCriteria(ds: Datastore) : BaseCriteria<JavadocApi>(ds, JavadocApi::class.java) {

    public fun baseUrl(): TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String> {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "baseUrl")
    }

    public fun baseUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "baseUrl").equal(value)
    }

    public fun downloadUrl(): TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String> {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "downloadUrl")
    }

    public fun downloadUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "downloadUrl").equal(value)
    }

    public fun id(): TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, ObjectId> {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun name(): TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String> {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "name")
    }

    public fun name(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "name").equal(value)
    }

    public fun upperName(): TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String> {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "upperName")
    }

    public fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "upperName").equal(value)
    }

    public fun getUpdater(): JavadocApiUpdater {
        return JavadocApiUpdater()
    }

    public inner class JavadocApiUpdater {
        var updateOperations = ds.createUpdateOperations(JavadocApi::class.java)

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

        public fun baseUrl(value: String): JavadocApiUpdater {
            updateOperations.set("baseUrl", value)
            return this
        }

        public fun unsetBaseUrl(): JavadocApiUpdater {
            updateOperations.unset("baseUrl")
            return this
        }

        public fun downloadUrl(value: String): JavadocApiUpdater {
            updateOperations.set("downloadUrl", value)
            return this
        }

        public fun unsetDownloadUrl(): JavadocApiUpdater {
            updateOperations.unset("downloadUrl")
            return this
        }

        public fun name(value: String): JavadocApiUpdater {
            updateOperations.set("name", value)
            return this
        }

        public fun unsetName(): JavadocApiUpdater {
            updateOperations.unset("name")
            return this
        }

        public fun upperName(value: String): JavadocApiUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        public fun unsetUpperName(): JavadocApiUpdater {
            updateOperations.unset("upperName")
            return this
        }
    }
}
