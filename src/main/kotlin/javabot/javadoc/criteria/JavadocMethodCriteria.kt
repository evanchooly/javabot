package javabot.javadoc.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.javadoc.JavadocMethod
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults

class JavadocMethodCriteria(ds: Datastore) : BaseCriteria<JavadocMethod>(ds, JavadocMethod::class.java) {

    fun id(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, ObjectId> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun javadocClassId(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, ObjectId> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(
              this, query, "javadocClassId")
    }

    fun javadocClassId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(
              this, query, "javadocClassId").equal(value)
    }

    fun longSignatureTypes(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "longSignatureTypes")
    }

    fun longSignatureTypes(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "longSignatureTypes").equal(value)
    }

    fun name(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "name")
    }

    fun name(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "name").equal(value)
    }

    fun paramCount(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, Int> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, Int>(
              this, query, "paramCount")
    }

    fun paramCount(value: Int?): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, Int>(
              this, query, "paramCount").equal(value)
    }

    fun parentClassName(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "parentClassName")
    }

    fun parentClassName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "parentClassName").equal(value)
    }

    fun shortSignatureTypes(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "shortSignatureTypes")
    }

    fun shortSignatureTypes(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "shortSignatureTypes").equal(value)
    }

    fun upperName(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "upperName")
    }

    fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "upperName").equal(value)
    }

    fun apiId(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, ObjectId> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(
              this, query, "apiId")
    }

    fun apiId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(
              this, query, "apiId").equal(value)
    }

    fun directUrl(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "directUrl")
    }

    fun directUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "directUrl").equal(value)
    }

    fun longUrl(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "longUrl")
    }

    fun longUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "longUrl").equal(value)
    }

    fun shortUrl(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "shortUrl")
    }

    fun shortUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "shortUrl").equal(value)
    }

    fun getUpdater(): JavadocMethodUpdater {
        return JavadocMethodUpdater()
    }

    inner class JavadocMethodUpdater {
        var updateOperations = ds.createUpdateOperations(JavadocMethod::class.java)

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

        fun javadocClassId(value: ObjectId): JavadocMethodUpdater {
            updateOperations.set("javadocClassId", value)
            return this
        }

        fun unsetJavadocClassId(): JavadocMethodUpdater {
            updateOperations.unset("javadocClassId")
            return this
        }

        fun longSignatureTypes(value: String): JavadocMethodUpdater {
            updateOperations.set("longSignatureTypes", value)
            return this
        }

        fun unsetLongSignatureTypes(): JavadocMethodUpdater {
            updateOperations.unset("longSignatureTypes")
            return this
        }

        fun name(value: String): JavadocMethodUpdater {
            updateOperations.set("name", value)
            return this
        }

        fun unsetName(): JavadocMethodUpdater {
            updateOperations.unset("name")
            return this
        }

        fun paramCount(value: Int?): JavadocMethodUpdater {
            updateOperations.set("paramCount", value)
            return this
        }

        fun unsetParamCount(): JavadocMethodUpdater {
            updateOperations.unset("paramCount")
            return this
        }

        fun decParamCount(): JavadocMethodUpdater {
            updateOperations.dec("paramCount")
            return this
        }

        fun incParamCount(): JavadocMethodUpdater {
            updateOperations.inc("paramCount")
            return this
        }

        fun incParamCount(value: Int?): JavadocMethodUpdater {
            updateOperations.inc("paramCount", value)
            return this
        }

        fun parentClassName(value: String): JavadocMethodUpdater {
            updateOperations.set("parentClassName", value)
            return this
        }

        fun unsetParentClassName(): JavadocMethodUpdater {
            updateOperations.unset("parentClassName")
            return this
        }

        fun shortSignatureTypes(value: String): JavadocMethodUpdater {
            updateOperations.set("shortSignatureTypes", value)
            return this
        }

        fun unsetShortSignatureTypes(): JavadocMethodUpdater {
            updateOperations.unset("shortSignatureTypes")
            return this
        }

        fun upperName(value: String): JavadocMethodUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        fun unsetUpperName(): JavadocMethodUpdater {
            updateOperations.unset("upperName")
            return this
        }

        fun apiId(value: ObjectId): JavadocMethodUpdater {
            updateOperations.set("apiId", value)
            return this
        }

        fun unsetApiId(): JavadocMethodUpdater {
            updateOperations.unset("apiId")
            return this
        }

        fun directUrl(value: String): JavadocMethodUpdater {
            updateOperations.set("directUrl", value)
            return this
        }

        fun unsetDirectUrl(): JavadocMethodUpdater {
            updateOperations.unset("directUrl")
            return this
        }

        fun longUrl(value: String): JavadocMethodUpdater {
            updateOperations.set("longUrl", value)
            return this
        }

        fun unsetLongUrl(): JavadocMethodUpdater {
            updateOperations.unset("longUrl")
            return this
        }

        fun shortUrl(value: String): JavadocMethodUpdater {
            updateOperations.set("shortUrl", value)
            return this
        }

        fun unsetShortUrl(): JavadocMethodUpdater {
            updateOperations.unset("shortUrl")
            return this
        }
    }
}
