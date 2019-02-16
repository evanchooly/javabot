package javabot.dao

import javabot.model.javadoc.JavadocApi
import javabot.javadoc.JavadocSource
import javabot.model.javadoc.criteria.JavadocApiCriteria
import org.mongodb.morphia.Datastore
import javax.inject.Inject

class ApiDao @Inject constructor(ds: Datastore, var classDao: JavadocClassDao) : BaseDao<JavadocApi>(ds, JavadocApi::class.java) {
    fun find(name: String): JavadocApi? {
        val criteria = JavadocApiCriteria(ds)
        criteria.upperName().equal(name.toUpperCase())
        return criteria.query().get()
    }

    fun delete(api: String) {
        find(api)?.let { delete(it) }
    }

    fun delete(api: JavadocApi) {
        ds.delete(ds.createQuery(JavadocSource::class.java)
                .filter("api", api))
        classDao.deleteFor(api)
        super.delete(api)
    }

    override fun findAll(): List<JavadocApi> {
        return ds.createQuery(JavadocApi::class.java).order("name").asList()
    }
}
