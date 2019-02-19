package javabot.dao

import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.criteria.JavadocApiCriteria
import javabot.model.javadoc.criteria.JavadocClassCriteria
import javabot.model.javadoc.criteria.JavadocFieldCriteria
import javabot.model.javadoc.criteria.JavadocMethodCriteria
import org.mongodb.morphia.Datastore
import org.slf4j.LoggerFactory
import javax.inject.Inject

class ApiDao @Inject constructor(ds: Datastore) : BaseDao<JavadocApi>(ds, JavadocApi::class.java) {
    companion object {
        private val LOG = LoggerFactory.getLogger(ApiDao::class.java)
    }

    fun find(name: String): JavadocApi? {
        val criteria = JavadocApiCriteria(ds)
        criteria.upperName().equal(name.toUpperCase())
        return criteria.query().get()
    }

    fun delete(api: String) {
        find(api)?.let { delete(it) }
    }

    fun delete(api: JavadocApi) {
        LOG.debug("Dropping fields from " + api.name)
        val criteria = JavadocFieldCriteria(ds)
        criteria.apiId(api.id)
        ds.delete(criteria.query())

        LOG.debug("Dropping methods from " + api.name)
        val method = JavadocMethodCriteria(ds)
        method.apiId(api.id)
        ds.delete(method.query())

        LOG.debug("Dropping classes from " + api.name)
        val klass = JavadocClassCriteria(ds)
        klass.apiId(api.id)
        ds.delete(klass.query())

        super.delete(api)
    }

    override fun findAll(): List<JavadocApi> {
        return ds.createQuery(JavadocApi::class.java).order("name").asList()
    }
}
