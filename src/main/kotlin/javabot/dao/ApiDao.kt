package javabot.dao

import javabot.javadoc.JavadocApi
import javabot.javadoc.criteria.JavadocApiCriteria
import javax.inject.Inject

public class ApiDao() : BaseDao<JavadocApi>(JavadocApi::class.java) {
    @Inject
    lateinit var classDao: JavadocClassDao

    public fun find(name: String): JavadocApi? {
        val criteria = JavadocApiCriteria(ds)
        criteria.upperName().equal(name.toUpperCase())
        return criteria.query().get()
    }

    public fun delete(api: JavadocApi) {
        classDao.deleteFor(api)
        super.delete(api)
    }

    override fun findAll(): List<JavadocApi> {
        return ds.createQuery(JavadocApi::class.java).order("name").asList()
    }
}