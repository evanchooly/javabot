package javabot.javadoc.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.javadoc.JavadocClass
import javabot.javadoc.JavadocField
import javabot.javadoc.JavadocMethod
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults

public class JavadocClassCriteria(ds: Datastore) : BaseCriteria<JavadocClass>(ds, JavadocClass::class.java) {

    public fun fields(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, List<Any>> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, List<Any>>(
              this, query, "fields")
    }

    public fun fields(value: List<JavadocField>): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, List<Any>>(
              this, query, "fields").equal(value)
    }

    public fun id(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, ObjectId> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun methods(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, List<Any>> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, List<Any>>(
              this, query, "methods")
    }

    public fun methods(value: List<JavadocMethod>): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, List<Any>>(
              this, query, "methods").equal(value)
    }

    public fun name(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "name")
    }

    public fun name(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "name").equal(value)
    }

    public fun packageName(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "packageName")
    }

    public fun packageName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "packageName").equal(value)
    }

    public fun superClassId(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, ObjectId> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(
              this, query, "superClassId")
    }

    public fun superClassId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(
              this, query, "superClassId").equal(value)
    }

    public fun upperName(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "upperName")
    }

    public fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "upperName").equal(value)
    }

    public fun upperPackageName(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "upperPackageName")
    }

    public fun upperPackageName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "upperPackageName").equal(value)
    }

    public fun apiId(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, ObjectId> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(
              this, query, "apiId")
    }

    public fun apiId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(
              this, query, "apiId").equal(value)
    }

    public fun directUrl(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "directUrl")
    }

    public fun directUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "directUrl").equal(value)
    }

    public fun longUrl(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "longUrl")
    }

    public fun longUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "longUrl").equal(value)
    }

    public fun shortUrl(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "shortUrl")
    }

    public fun shortUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "shortUrl").equal(value)
    }

    public fun getUpdater(): JavadocClassUpdater {
        return JavadocClassUpdater()
    }

    public inner class JavadocClassUpdater {
        var updateOperations = ds.createUpdateOperations(JavadocClass::class.java)

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

        public fun fields(value: List<JavadocField>): JavadocClassUpdater {
            updateOperations.set("fields", value)
            return this
        }

        public fun unsetFields(): JavadocClassUpdater {
            updateOperations.unset("fields")
            return this
        }

        public fun addToFields(value: List<JavadocField>): JavadocClassUpdater {
            updateOperations.add("fields", value)
            return this
        }

        public fun addToFields(value: List<JavadocField>,
                               addDups: Boolean): JavadocClassUpdater {
            updateOperations.add("fields", value, addDups)
            return this
        }

        public fun addAllToFields(values: List<JavadocField>,
                                  addDups: Boolean): JavadocClassUpdater {
            updateOperations.addAll("fields", values, addDups)
            return this
        }

        public fun removeFirstFromFields(): JavadocClassUpdater {
            updateOperations.removeFirst("fields")
            return this
        }

        public fun removeLastFromFields(): JavadocClassUpdater {
            updateOperations.removeLast("fields")
            return this
        }

        public fun removeFromFields(value: List<JavadocField>): JavadocClassUpdater {
            updateOperations.removeAll("fields", value)
            return this
        }

        public fun removeAllFromFields(values: List<JavadocField>): JavadocClassUpdater {
            updateOperations.removeAll("fields", values)
            return this
        }

        public fun methods(value: List<JavadocMethod>): JavadocClassUpdater {
            updateOperations.set("methods", value)
            return this
        }

        public fun unsetMethods(): JavadocClassUpdater {
            updateOperations.unset("methods")
            return this
        }

        public fun addToMethods(value: List<JavadocMethod>): JavadocClassUpdater {
            updateOperations.add("methods", value)
            return this
        }

        public fun addToMethods(value: List<JavadocMethod>,
                                addDups: Boolean): JavadocClassUpdater {
            updateOperations.add("methods", value, addDups)
            return this
        }

        public fun addAllToMethods(values: List<JavadocMethod>,
                                   addDups: Boolean): JavadocClassUpdater {
            updateOperations.addAll("methods", values, addDups)
            return this
        }

        public fun removeFirstFromMethods(): JavadocClassUpdater {
            updateOperations.removeFirst("methods")
            return this
        }

        public fun removeLastFromMethods(): JavadocClassUpdater {
            updateOperations.removeLast("methods")
            return this
        }

        public fun removeFromMethods(value: List<JavadocMethod>): JavadocClassUpdater {
            updateOperations.removeAll("methods", value)
            return this
        }

        public fun removeAllFromMethods(
              values: List<JavadocMethod>): JavadocClassUpdater {
            updateOperations.removeAll("methods", values)
            return this
        }

        public fun name(value: String): JavadocClassUpdater {
            updateOperations.set("name", value)
            return this
        }

        public fun unsetName(): JavadocClassUpdater {
            updateOperations.unset("name")
            return this
        }

        public fun packageName(value: String): JavadocClassUpdater {
            updateOperations.set("packageName", value)
            return this
        }

        public fun unsetPackageName(): JavadocClassUpdater {
            updateOperations.unset("packageName")
            return this
        }

        public fun superClassId(value: ObjectId): JavadocClassUpdater {
            updateOperations.set("superClassId", value)
            return this
        }

        public fun unsetSuperClassId(): JavadocClassUpdater {
            updateOperations.unset("superClassId")
            return this
        }

        public fun upperName(value: String): JavadocClassUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        public fun unsetUpperName(): JavadocClassUpdater {
            updateOperations.unset("upperName")
            return this
        }

        public fun upperPackageName(value: String): JavadocClassUpdater {
            updateOperations.set("upperPackageName", value)
            return this
        }

        public fun unsetUpperPackageName(): JavadocClassUpdater {
            updateOperations.unset("upperPackageName")
            return this
        }

        public fun apiId(value: ObjectId): JavadocClassUpdater {
            updateOperations.set("apiId", value)
            return this
        }

        public fun unsetApiId(): JavadocClassUpdater {
            updateOperations.unset("apiId")
            return this
        }

        public fun directUrl(value: String): JavadocClassUpdater {
            updateOperations.set("directUrl", value)
            return this
        }

        public fun unsetDirectUrl(): JavadocClassUpdater {
            updateOperations.unset("directUrl")
            return this
        }

        public fun longUrl(value: String): JavadocClassUpdater {
            updateOperations.set("longUrl", value)
            return this
        }

        public fun unsetLongUrl(): JavadocClassUpdater {
            updateOperations.unset("longUrl")
            return this
        }

        public fun shortUrl(value: String): JavadocClassUpdater {
            updateOperations.set("shortUrl", value)
            return this
        }

        public fun unsetShortUrl(): JavadocClassUpdater {
            updateOperations.unset("shortUrl")
            return this
        }
    }
}
