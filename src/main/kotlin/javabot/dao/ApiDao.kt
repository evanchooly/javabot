package javabot.dao

import javabot.javadoc.JavadocApi
import javabot.javadoc.criteria.JavadocApiCriteria
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
}