package javabot.javadoc.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.javadoc.JavadocField
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults

class JavadocFieldCriteria(ds: Datastore) : BaseCriteria<JavadocField>(ds, JavadocField::class.java) {

    fun id(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, ObjectId> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun javadocClassId(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, ObjectId> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(
              this, query, "javadocClassId")
    }

    fun javadocClassId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(
              this, query, "javadocClassId").equal(value)
    }

    fun name(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "name")
    }

    fun name(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "name").equal(value)
    }

    fun parentClassName(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "parentClassName")
    }

    fun parentClassName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "parentClassName").equal(value)
    }

    fun type(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "type")
    }

    fun type(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "type").equal(value)
    }

    fun upperName(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "upperName")
    }

    fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "upperName").equal(value)
    }

    fun apiId(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, ObjectId> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(
              this, query, "apiId")
    }

    fun apiId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(
              this, query, "apiId").equal(value)
    }

    fun directUrl(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "directUrl")
    }

    fun directUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "directUrl").equal(value)
    }

    fun longUrl(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "longUrl")
    }

    fun longUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "longUrl").equal(value)
    }

    fun shortUrl(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "shortUrl")
    }

    fun shortUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "shortUrl").equal(value)
    }

    fun getUpdater(): JavadocFieldUpdater {
        return JavadocFieldUpdater()
    }

    inner class JavadocFieldUpdater {
        var updateOperations = ds.createUpdateOperations(JavadocField::class.java)

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

        fun javadocClassId(value: ObjectId): JavadocFieldUpdater {
            updateOperations.set("javadocClassId", value)
            return this
        }

        fun unsetJavadocClassId(): JavadocFieldUpdater {
            updateOperations.unset("javadocClassId")
            return this
        }

        fun name(value: String): JavadocFieldUpdater {
            updateOperations.set("name", value)
            return this
        }

        fun unsetName(): JavadocFieldUpdater {
            updateOperations.unset("name")
            return this
        }

        fun parentClassName(value: String): JavadocFieldUpdater {
            updateOperations.set("parentClassName", value)
            return this
        }

        fun unsetParentClassName(): JavadocFieldUpdater {
            updateOperations.unset("parentClassName")
            return this
        }

        fun type(value: String): JavadocFieldUpdater {
            updateOperations.set("type", value)
            return this
        }

        fun unsetType(): JavadocFieldUpdater {
            updateOperations.unset("type")
            return this
        }

        fun upperName(value: String): JavadocFieldUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        fun unsetUpperName(): JavadocFieldUpdater {
            updateOperations.unset("upperName")
            return this
        }

        fun apiId(value: ObjectId): JavadocFieldUpdater {
            updateOperations.set("apiId", value)
            return this
        }

        fun unsetApiId(): JavadocFieldUpdater {
            updateOperations.unset("apiId")
            return this
        }

        fun directUrl(value: String): JavadocFieldUpdater {
            updateOperations.set("directUrl", value)
            return this
        }

        fun unsetDirectUrl(): JavadocFieldUpdater {
            updateOperations.unset("directUrl")
            return this
        }

        fun longUrl(value: String): JavadocFieldUpdater {
            updateOperations.set("longUrl", value)
            return this
        }

        fun unsetLongUrl(): JavadocFieldUpdater {
            updateOperations.unset("longUrl")
            return this
        }

        fun shortUrl(value: String): JavadocFieldUpdater {
            updateOperations.set("shortUrl", value)
            return this
        }

        fun unsetShortUrl(): JavadocFieldUpdater {
            updateOperations.unset("shortUrl")
            return this
        }
    }
}
