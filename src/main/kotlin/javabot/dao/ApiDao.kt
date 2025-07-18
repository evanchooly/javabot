package javabot.dao

import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import dev.morphia.query.FindOptions
import dev.morphia.query.Sort
import dev.morphia.query.filters.Filters.eq
import java.util.Locale
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import javabot.model.javadoc.JavadocMethod
import javax.inject.Inject
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory

class ApiDao @Inject constructor(ds: Datastore) : BaseDao<JavadocApi>(ds, JavadocApi::class.java) {
    companion object {
        private val LOG = LoggerFactory.getLogger(ApiDao::class.java)
    }

    fun find(name: String) =
        ds.find(JavadocApi::class.java)
            .filter(eq("upperName", name.uppercase(Locale.getDefault())))
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
            .filter(eq("apiId", api.id))
            .delete(DeleteOptions().multi(true))

        LOG.debug("Dropping methods from " + api.name)
        ds.find(JavadocMethod::class.java)
            .filter(eq("apiId", api.id))
            .delete(DeleteOptions().multi(true))

        LOG.debug("Dropping classes from " + api.name)
        ds.find(JavadocClass::class.java)
            .filter(eq("apiId", api.id))
            .delete(DeleteOptions().multi(true))

        super.delete(api)
    }

    override fun findAll(): List<JavadocApi> {
        return ds.find(JavadocApi::class.java)
            .iterator(FindOptions().sort(Sort.ascending("name")))
            .toList()
    }
}
