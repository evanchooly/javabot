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

class JavadocClassCriteria(ds: Datastore) : BaseCriteria<JavadocClass>(ds, JavadocClass::class.java) {

    fun fields(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, List<Any>> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, List<Any>>(
              this, query, "fields")
    }

    fun fields(value: List<JavadocField>): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, List<Any>>(
              this, query, "fields").equal(value)
    }

    fun id(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, ObjectId> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun methods(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, List<Any>> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, List<Any>>(
              this, query, "methods")
    }

    fun methods(value: List<JavadocMethod>): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, List<Any>>(
              this, query, "methods").equal(value)
    }

    fun name(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "name")
    }

    fun name(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "name").equal(value)
    }

    fun packageName(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "packageName")
    }

    fun packageName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "packageName").equal(value)
    }

    fun parentClass(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, ObjectId> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(
              this, query, "superClassId")
    }

    fun parentClass(value: JavadocClass): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, JavadocClass>(
              this, query, "parentClass").equal(value)
    }

    fun upperName(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "upperName")
    }

    fun upperName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "upperName").equal(value)
    }

    fun upperPackageName(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "upperPackageName")
    }

    fun upperPackageName(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "upperPackageName").equal(value)
    }

    fun apiId(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, ObjectId> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(
              this, query, "apiId")
    }

    fun apiId(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, org.bson.types.ObjectId>(
              this, query, "apiId").equal(value)
    }

    fun url(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "url")
    }

    fun url(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "url").equal(value)
    }

    fun shortUrl(): TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String> {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "shortUrl")
    }

    fun shortUrl(value: String): Criteria {
        return TypeSafeFieldEnd<JavadocClassCriteria, JavadocClass, String>(
              this, query, "shortUrl").equal(value)
    }

    fun getUpdater(): JavadocClassUpdater {
        return JavadocClassUpdater()
    }

    inner class JavadocClassUpdater {
        var updateOperations = ds.createUpdateOperations(JavadocClass::class.java)

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

        fun fields(value: List<JavadocField>): JavadocClassUpdater {
            updateOperations.set("fields", value)
            return this
        }

        fun unsetFields(): JavadocClassUpdater {
            updateOperations.unset("fields")
            return this
        }

        fun addToFields(value: List<JavadocField>): JavadocClassUpdater {
            updateOperations.add("fields", value)
            return this
        }

        fun addToFields(value: List<JavadocField>,
                               addDups: Boolean): JavadocClassUpdater {
            updateOperations.add("fields", value, addDups)
            return this
        }

        fun addAllToFields(values: List<JavadocField>,
                                  addDups: Boolean): JavadocClassUpdater {
            updateOperations.addAll("fields", values, addDups)
            return this
        }

        fun removeFirstFromFields(): JavadocClassUpdater {
            updateOperations.removeFirst("fields")
            return this
        }

        fun removeLastFromFields(): JavadocClassUpdater {
            updateOperations.removeLast("fields")
            return this
        }

        fun removeFromFields(value: List<JavadocField>): JavadocClassUpdater {
            updateOperations.removeAll("fields", value)
            return this
        }

        fun removeAllFromFields(values: List<JavadocField>): JavadocClassUpdater {
            updateOperations.removeAll("fields", values)
            return this
        }

        fun methods(value: List<JavadocMethod>): JavadocClassUpdater {
            updateOperations.set("methods", value)
            return this
        }

        fun unsetMethods(): JavadocClassUpdater {
            updateOperations.unset("methods")
            return this
        }

        fun addToMethods(value: List<JavadocMethod>): JavadocClassUpdater {
            updateOperations.add("methods", value)
            return this
        }

        fun addToMethods(value: List<JavadocMethod>,
                                addDups: Boolean): JavadocClassUpdater {
            updateOperations.add("methods", value, addDups)
            return this
        }

        fun addAllToMethods(values: List<JavadocMethod>,
                                   addDups: Boolean): JavadocClassUpdater {
            updateOperations.addAll("methods", values, addDups)
            return this
        }

        fun removeFirstFromMethods(): JavadocClassUpdater {
            updateOperations.removeFirst("methods")
            return this
        }

        fun removeLastFromMethods(): JavadocClassUpdater {
            updateOperations.removeLast("methods")
            return this
        }

        fun removeFromMethods(value: List<JavadocMethod>): JavadocClassUpdater {
            updateOperations.removeAll("methods", value)
            return this
        }

        fun removeAllFromMethods(
              values: List<JavadocMethod>): JavadocClassUpdater {
            updateOperations.removeAll("methods", values)
            return this
        }

        fun name(value: String): JavadocClassUpdater {
            updateOperations.set("name", value)
            return this
        }

        fun unsetName(): JavadocClassUpdater {
            updateOperations.unset("name")
            return this
        }

        fun packageName(value: String): JavadocClassUpdater {
            updateOperations.set("packageName", value)
            return this
        }

        fun unsetPackageName(): JavadocClassUpdater {
            updateOperations.unset("packageName")
            return this
        }

        fun superClassId(value: ObjectId): JavadocClassUpdater {
            updateOperations.set("superClassId", value)
            return this
        }

        fun unsetSuperClassId(): JavadocClassUpdater {
            updateOperations.unset("superClassId")
            return this
        }

        fun upperName(value: String): JavadocClassUpdater {
            updateOperations.set("upperName", value)
            return this
        }

        fun unsetUpperName(): JavadocClassUpdater {
            updateOperations.unset("upperName")
            return this
        }

        fun upperPackageName(value: String): JavadocClassUpdater {
            updateOperations.set("upperPackageName", value)
            return this
        }

        fun unsetUpperPackageName(): JavadocClassUpdater {
            updateOperations.unset("upperPackageName")
            return this
        }

        fun apiId(value: ObjectId): JavadocClassUpdater {
            updateOperations.set("apiId", value)
            return this
        }

        fun unsetApiId(): JavadocClassUpdater {
            updateOperations.unset("apiId")
            return this
        }

        fun url(value: String): JavadocClassUpdater {
            updateOperations.set("url", value)
            return this
        }

        fun unsetUrl(): JavadocClassUpdater {
            updateOperations.unset("url")
            return this
        }

        fun shortUrl(value: String): JavadocClassUpdater {
            updateOperations.set("shortUrl", value)
            return this
        }

        fun unsetShortUrl(): JavadocClassUpdater {
            updateOperations.unset("shortUrl")
            return this
        }
    }
}
