package javabot.dao

import javax.inject.Inject

import javabot.javadoc.JavadocApi
import javabot.javadoc.criteria.JavadocApiCriteria
import org.bson.types.ObjectId

public class ApiDao protected constructor() : BaseDao<JavadocApi>(JavadocApi::class.java) {
    Inject
    public var classDao: JavadocClassDao

    public fun find(name: String): JavadocApi {
        val criteria = JavadocApiCriteria(ds)
        criteria.upperName().equal(name.toUpperCase())
        return criteria.query().get()
    }

    public fun delete(api: JavadocApi) {
        classDao.deleteFor(api)
        super.delete(api)
    }

    override fun delete(id: ObjectId) {
        delete(find(id))
    }

    override fun findAll(): List<JavadocApi> {
        return ds.createQuery(JavadocApi::class.java).order("name").asList()
    }
}