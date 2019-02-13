package javabot.model.javadoc.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import java.util.Date
import javabot.model.javadoc.JavadocApi
import javabot.javadoc.JavadocSource
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.CriteriaContainer
import org.mongodb.morphia.query.Query
import org.mongodb.morphia.query.UpdateOperations
import org.mongodb.morphia.query.UpdateResults

@Suppress("UNCHECKED_CAST")
class JavadocSourceCriteria(val datastore: Datastore, private val query: Query<*>, fieldName: String? = null) {
    companion object {
        val api = "api"
        val created = "created"
        val name = "name"
        val processed = "processed"
    }
    constructor(ds: Datastore, fieldName: String? = null): this(ds, ds.find(JavadocSource::class.java), fieldName)

    private var prefix = fieldName?.let { fieldName + "." } ?: "" 
    class JavadocSourceUpdater(private val ds: Datastore, private val query: Query<*>, private val updateOperations: UpdateOperations<*>, fieldName: String? = null) {
        private var prefix = fieldName?.let { fieldName + "." } ?: "" 
        fun updateAll(wc: WriteConcern = ds.defaultWriteConcern): UpdateResults {
            return ds.update(query as Query<Any>, updateOperations as UpdateOperations<Any>, false, wc)
        }
        fun updateFirst(wc: WriteConcern = ds.defaultWriteConcern): UpdateResults {
            return ds.updateFirst(query as Query<Any>, updateOperations as UpdateOperations<Any>, false, wc)
        }
        fun upsert(wc: WriteConcern = ds.defaultWriteConcern): UpdateResults {
            return ds.update(query as Query<Any>, updateOperations as UpdateOperations<Any>, true, wc)
        }
        fun remove(wc: WriteConcern = ds.defaultWriteConcern): WriteResult {
            return ds.delete(query, wc)
        }
        fun api(__newValue: JavadocApi): JavadocSourceUpdater {
            updateOperations.set(prefix + "api", __newValue)
            return this
        }
        fun unsetApi(): JavadocSourceUpdater {
            updateOperations.unset(prefix + "api")
            return this
        }
        fun created(__newValue: Date): JavadocSourceUpdater {
            updateOperations.set(prefix + "created", __newValue)
            return this
        }
        fun unsetCreated(): JavadocSourceUpdater {
            updateOperations.unset(prefix + "created")
            return this
        }
        fun name(__newValue: String): JavadocSourceUpdater {
            updateOperations.set(prefix + "name", __newValue)
            return this
        }
        fun unsetName(): JavadocSourceUpdater {
            updateOperations.unset(prefix + "name")
            return this
        }
        fun processed(__newValue: Boolean): JavadocSourceUpdater {
            updateOperations.set(prefix + "processed", __newValue)
            return this
        }
        fun unsetProcessed(): JavadocSourceUpdater {
            updateOperations.unset(prefix + "processed")
            return this
        }
    }
    fun query(): Query<JavadocSource> {
        return query as Query<JavadocSource>
    }
    fun delete(wc: WriteConcern = datastore.defaultWriteConcern): WriteResult {
        return datastore.delete(query, wc)
    }
    fun or(vararg criteria: Criteria): CriteriaContainer {
        return query.or(*criteria)
    }
    fun and(vararg criteria: Criteria): CriteriaContainer {
        return query.and(*criteria)
    }
    fun api(): TypeSafeFieldEnd<JavadocSourceCriteria, JavadocApi> {
        return TypeSafeFieldEnd(this, query, api)
    }
    fun api(__newValue: JavadocApi): Criteria {
        return com.antwerkz.critter.TypeSafeFieldEnd<JavadocSourceCriteria, javabot.model.javadoc.JavadocApi>(this, query, api).equal(__newValue)
    }
    fun created(): TypeSafeFieldEnd<JavadocSourceCriteria, Date> {
        return TypeSafeFieldEnd(this, query, created)
    }
    fun created(__newValue: Date): Criteria {
        return com.antwerkz.critter.TypeSafeFieldEnd<JavadocSourceCriteria, java.util.Date>(this, query, created).equal(__newValue)
    }
    fun name(): TypeSafeFieldEnd<JavadocSourceCriteria, String> {
        return TypeSafeFieldEnd(this, query, name)
    }
    fun name(__newValue: String): Criteria {
        return com.antwerkz.critter.TypeSafeFieldEnd<JavadocSourceCriteria, String>(this, query, name).equal(__newValue)
    }
    fun processed(): TypeSafeFieldEnd<JavadocSourceCriteria, Boolean> {
        return TypeSafeFieldEnd(this, query, processed)
    }
    fun processed(__newValue: Boolean): Criteria {
        return com.antwerkz.critter.TypeSafeFieldEnd<JavadocSourceCriteria, Boolean>(this, query, processed).equal(__newValue)
    }
    fun updater(): JavadocSourceUpdater {
        return JavadocSourceUpdater(datastore, query, datastore.createUpdateOperations(JavadocSource::class.java),
                                    if(prefix.isNotEmpty()) prefix else null)
    }
}
