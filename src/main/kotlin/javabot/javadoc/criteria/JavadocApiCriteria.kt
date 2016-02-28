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

class JavadocApiCriteria(ds: Datastore) : BaseCriteria<JavadocApi>(ds, JavadocApi::class.java) {

    fun baseUrl(): TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String> {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "baseUrl")
    }

    fun baseUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "baseUrl").equal(value)
    }

    fun downloadUrl(): TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String> {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "downloadUrl")
    }

    fun downloadUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "downloadUrl").equal(value)
    }

    fun id(): TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, ObjectId> {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun name(): TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String> {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "name")
    }

    fun name(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "name").equal(value)
    }

    fun upperName(): TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String> {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "upperName")
    }

    fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocApiCriteria, JavadocApi, String>(
              this, query, "upperName").equal(value)
    }

    fun getUpdater(): JavadocApiUpdater {
        return JavadocApiUpdater()
    }

    inner class JavadocApiUpdater {
        var updateOperations = ds.createUpdateOperations(JavadocApi::class.java)

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

        fun baseUrl(value: String): JavadocApiUpdater {
            updateOperations.set("baseUrl", value)
            return this
        }

        fun unsetBaseUrl(): JavadocApiUpdater {
            updateOperations.unset("baseUrl")
            return this
        }

        fun downloadUrl(value: String): JavadocApiUpdater {
            updateOperations.set("downloadUrl", value)
            return this
        }

        fun unsetDownloadUrl(): JavadocApiUpdater {
            updateOperations.unset("downloadUrl")
            return this
        }

        fun name(value: String): JavadocApiUpdater {
            updateOperations.set("name", value)
            return this
        }

        fun unsetName(): JavadocApiUpdater {
            updateOperations.unset("name")
            return this
        }

        fun upperName(value: String): JavadocApiUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        fun unsetUpperName(): JavadocApiUpdater {
            updateOperations.unset("upperName")
            return this
        }
    }
}
