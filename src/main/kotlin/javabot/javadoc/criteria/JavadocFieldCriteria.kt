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

public class JavadocFieldCriteria(ds: Datastore) : BaseCriteria<JavadocField>(ds, JavadocField::class.java) {

    public fun id(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, ObjectId> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun javadocClassId(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, ObjectId> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(
              this, query, "javadocClassId")
    }

    public fun javadocClassId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(
              this, query, "javadocClassId").equal(value)
    }

    public fun name(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "name")
    }

    public fun name(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "name").equal(value)
    }

    public fun parentClassName(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "parentClassName")
    }

    public fun parentClassName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "parentClassName").equal(value)
    }

    public fun type(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "type")
    }

    public fun type(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "type").equal(value)
    }

    public fun upperName(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "upperName")
    }

    public fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "upperName").equal(value)
    }

    public fun apiId(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, ObjectId> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(
              this, query, "apiId")
    }

    public fun apiId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, org.bson.types.ObjectId>(
              this, query, "apiId").equal(value)
    }

    public fun directUrl(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "directUrl")
    }

    public fun directUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "directUrl").equal(value)
    }

    public fun longUrl(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "longUrl")
    }

    public fun longUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "longUrl").equal(value)
    }

    public fun shortUrl(): TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String> {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "shortUrl")
    }

    public fun shortUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocFieldCriteria, JavadocField, String>(
              this, query, "shortUrl").equal(value)
    }

    public fun getUpdater(): JavadocFieldUpdater {
        return JavadocFieldUpdater()
    }

    public inner class JavadocFieldUpdater {
        var updateOperations = ds.createUpdateOperations(JavadocField::class.java)

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

        public fun javadocClassId(value: ObjectId): JavadocFieldUpdater {
            updateOperations.set("javadocClassId", value)
            return this
        }

        public fun unsetJavadocClassId(): JavadocFieldUpdater {
            updateOperations.unset("javadocClassId")
            return this
        }

        public fun name(value: String): JavadocFieldUpdater {
            updateOperations.set("name", value)
            return this
        }

        public fun unsetName(): JavadocFieldUpdater {
            updateOperations.unset("name")
            return this
        }

        public fun parentClassName(value: String): JavadocFieldUpdater {
            updateOperations.set("parentClassName", value)
            return this
        }

        public fun unsetParentClassName(): JavadocFieldUpdater {
            updateOperations.unset("parentClassName")
            return this
        }

        public fun type(value: String): JavadocFieldUpdater {
            updateOperations.set("type", value)
            return this
        }

        public fun unsetType(): JavadocFieldUpdater {
            updateOperations.unset("type")
            return this
        }

        public fun upperName(value: String): JavadocFieldUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        public fun unsetUpperName(): JavadocFieldUpdater {
            updateOperations.unset("upperName")
            return this
        }

        public fun apiId(value: ObjectId): JavadocFieldUpdater {
            updateOperations.set("apiId", value)
            return this
        }

        public fun unsetApiId(): JavadocFieldUpdater {
            updateOperations.unset("apiId")
            return this
        }

        public fun directUrl(value: String): JavadocFieldUpdater {
            updateOperations.set("directUrl", value)
            return this
        }

        public fun unsetDirectUrl(): JavadocFieldUpdater {
            updateOperations.unset("directUrl")
            return this
        }

        public fun longUrl(value: String): JavadocFieldUpdater {
            updateOperations.set("longUrl", value)
            return this
        }

        public fun unsetLongUrl(): JavadocFieldUpdater {
            updateOperations.unset("longUrl")
            return this
        }

        public fun shortUrl(value: String): JavadocFieldUpdater {
            updateOperations.set("shortUrl", value)
            return this
        }

        public fun unsetShortUrl(): JavadocFieldUpdater {
            updateOperations.unset("shortUrl")
            return this
        }
    }
}
