package javabot.dao

import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocSource
import javabot.model.javadoc.criteria.JavadocApiCriteria
import org.mongodb.morphia.Datastore
import javax.inject.Inject

class ApiDao @Inject constructor(ds: Datastore, var classDao: JavadocClassDao) : BaseDao<JavadocApi>(ds, JavadocApi::class.java) {
    fun find(name: String): JavadocApi? {
        val criteria = JavadocApiCriteria(ds)
        criteria.upperName().equal(name.toUpperCase())
        return criteria.query().get()
    }

    fun delete(api: JavadocApi) {
        classDao.deleteFor(api)
        super.delete(api)
    }

    override fun findAll(): List<JavadocApi> {
        return ds.createQuery(JavadocApi::class.java).order("name").asList()
    }

    fun findUnprocessedSource(api: JavadocApi): JavadocSource? {
        val query = ds.createQuery(JavadocSource::class.java)
                .filter("api", api.id)
                .filter("processed", false)
        return ds.findAndDelete(query)
    }

    fun countUnprocessed(api: JavadocApi): Long {
        val query = ds.createQuery(JavadocSource::class.java)
                .filter("api", api.id)
                .filter("processed", false)

        return ds.getCount(query)
    }
}