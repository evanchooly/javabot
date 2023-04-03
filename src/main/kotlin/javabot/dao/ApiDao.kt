package javabot.dao

import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import java.util.Locale
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import javabot.model.javadoc.JavadocMethod
import javabot.model.javadoc.criteria.JavadocApiCriteria
import javabot.model.javadoc.criteria.JavadocApiCriteria.Companion.upperName
import javabot.model.javadoc.criteria.JavadocClassCriteria
import javabot.model.javadoc.criteria.JavadocFieldCriteria
import javabot.model.javadoc.criteria.JavadocMethodCriteria
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import javax.inject.Inject

class ApiDao @Inject constructor(ds: Datastore) : BaseDao<JavadocApi>(ds, JavadocApi::class.java) {
    companion object {
        private val LOG = LoggerFactory.getLogger(ApiDao::class.java)
    }

    fun find(name: String) = ds.find(JavadocApi::class.java)
            .filter(upperName().eq(name.uppercase(Locale.getDefault())))
            .first()

    fun delete(api: String) {
        find(api)?.let { delete(it) }
    }

    override fun delete(id: ObjectId?) {
        find(id)?.let { delete(it) }
    }

    fun delete(api: JavadocApi) {
        LOG.debug("Dropping fields from " + api.name)

        ds.find(JavadocField::class.java)
                .filter(JavadocFieldCriteria.apiId().eq(api.id))
                .delete(DeleteOptions().multi(true))

        LOG.debug("Dropping methods from " + api.name)
        ds.find(JavadocMethod::class.java)
                .filter(JavadocMethodCriteria.apiId(). eq(api.id))
                .delete(DeleteOptions().multi(true))

        LOG.debug("Dropping classes from " + api.name)
        ds.find(JavadocClass::class.java)
                .filter(JavadocClassCriteria.apiId().eq(api.id))
                .delete(DeleteOptions().multi(true))

        super.delete(api)
    }

    override fun findAll(): List<JavadocApi> {
        return ds.find(JavadocApi::class.java)
                .iterator(FindOptions()
                        .sort(Sort.ascending(JavadocApiCriteria.name)))
                .toList()
    }
}
