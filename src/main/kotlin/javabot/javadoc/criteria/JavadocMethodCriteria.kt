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

public class JavadocMethodCriteria(ds: Datastore) : BaseCriteria<JavadocMethod>(ds, JavadocMethod::class.java) {

    public fun id(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, ObjectId> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun javadocClassId(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, ObjectId> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(
              this, query, "javadocClassId")
    }

    public fun javadocClassId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(
              this, query, "javadocClassId").equal(value)
    }

    public fun longSignatureTypes(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "longSignatureTypes")
    }

    public fun longSignatureTypes(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "longSignatureTypes").equal(value)
    }

    public fun name(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "name")
    }

    public fun name(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "name").equal(value)
    }

    public fun paramCount(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, Int> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, Int>(
              this, query, "paramCount")
    }

    public fun paramCount(value: Int?): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, Int>(
              this, query, "paramCount").equal(value)
    }

    public fun parentClassName(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "parentClassName")
    }

    public fun parentClassName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "parentClassName").equal(value)
    }

    public fun shortSignatureTypes(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "shortSignatureTypes")
    }

    public fun shortSignatureTypes(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "shortSignatureTypes").equal(value)
    }

    public fun upperName(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "upperName")
    }

    public fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "upperName").equal(value)
    }

    public fun apiId(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, ObjectId> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(
              this, query, "apiId")
    }

    public fun apiId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, org.bson.types.ObjectId>(
              this, query, "apiId").equal(value)
    }

    public fun directUrl(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "directUrl")
    }

    public fun directUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "directUrl").equal(value)
    }

    public fun longUrl(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "longUrl")
    }

    public fun longUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "longUrl").equal(value)
    }

    public fun shortUrl(): TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String> {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "shortUrl")
    }

    public fun shortUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocMethodCriteria, JavadocMethod, String>(
              this, query, "shortUrl").equal(value)
    }

    public fun getUpdater(): JavadocMethodUpdater {
        return JavadocMethodUpdater()
    }

    public inner class JavadocMethodUpdater {
        var updateOperations = ds.createUpdateOperations(JavadocMethod::class.java)

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

        public fun javadocClassId(value: ObjectId): JavadocMethodUpdater {
            updateOperations.set("javadocClassId", value)
            return this
        }

        public fun unsetJavadocClassId(): JavadocMethodUpdater {
            updateOperations.unset("javadocClassId")
            return this
        }

        public fun longSignatureTypes(value: String): JavadocMethodUpdater {
            updateOperations.set("longSignatureTypes", value)
            return this
        }

        public fun unsetLongSignatureTypes(): JavadocMethodUpdater {
            updateOperations.unset("longSignatureTypes")
            return this
        }

        public fun name(value: String): JavadocMethodUpdater {
            updateOperations.set("name", value)
            return this
        }

        public fun unsetName(): JavadocMethodUpdater {
            updateOperations.unset("name")
            return this
        }

        public fun paramCount(value: Int?): JavadocMethodUpdater {
            updateOperations.set("paramCount", value)
            return this
        }

        public fun unsetParamCount(): JavadocMethodUpdater {
            updateOperations.unset("paramCount")
            return this
        }

        public fun decParamCount(): JavadocMethodUpdater {
            updateOperations.dec("paramCount")
            return this
        }

        public fun incParamCount(): JavadocMethodUpdater {
            updateOperations.inc("paramCount")
            return this
        }

        public fun incParamCount(value: Int?): JavadocMethodUpdater {
            updateOperations.inc("paramCount", value)
            return this
        }

        public fun parentClassName(value: String): JavadocMethodUpdater {
            updateOperations.set("parentClassName", value)
            return this
        }

        public fun unsetParentClassName(): JavadocMethodUpdater {
            updateOperations.unset("parentClassName")
            return this
        }

        public fun shortSignatureTypes(value: String): JavadocMethodUpdater {
            updateOperations.set("shortSignatureTypes", value)
            return this
        }

        public fun unsetShortSignatureTypes(): JavadocMethodUpdater {
            updateOperations.unset("shortSignatureTypes")
            return this
        }

        public fun upperName(value: String): JavadocMethodUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        public fun unsetUpperName(): JavadocMethodUpdater {
            updateOperations.unset("upperName")
            return this
        }

        public fun apiId(value: ObjectId): JavadocMethodUpdater {
            updateOperations.set("apiId", value)
            return this
        }

        public fun unsetApiId(): JavadocMethodUpdater {
            updateOperations.unset("apiId")
            return this
        }

        public fun directUrl(value: String): JavadocMethodUpdater {
            updateOperations.set("directUrl", value)
            return this
        }

        public fun unsetDirectUrl(): JavadocMethodUpdater {
            updateOperations.unset("directUrl")
            return this
        }

        public fun longUrl(value: String): JavadocMethodUpdater {
            updateOperations.set("longUrl", value)
            return this
        }

        public fun unsetLongUrl(): JavadocMethodUpdater {
            updateOperations.unset("longUrl")
            return this
        }

        public fun shortUrl(value: String): JavadocMethodUpdater {
            updateOperations.set("shortUrl", value)
            return this
        }

        public fun unsetShortUrl(): JavadocMethodUpdater {
            updateOperations.unset("shortUrl")
            return this
        }
    }
}
