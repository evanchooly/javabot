package javabot.dao

import com.google.inject.Inject
import javabot.javadoc.JavadocApi
import javabot.javadoc.JavadocClass
import javabot.javadoc.JavadocClassParser
import javabot.javadoc.JavadocField
import javabot.javadoc.JavadocMethod
import javabot.javadoc.criteria.JavadocClassCriteria
import javabot.javadoc.criteria.JavadocFieldCriteria
import javabot.javadoc.criteria.JavadocMethodCriteria
import org.mongodb.morphia.Datastore
import org.slf4j.LoggerFactory
import java.util.ArrayList

class JavadocClassDao @Inject constructor(ds: Datastore)  : BaseDao<JavadocClass>(ds, JavadocClass::class.java) {

    fun getClass(api: JavadocApi?, name: String): List<JavadocClass> {
        val strings = JavadocClassParser.calculateNameAndPackage(name)
        val pkgName = strings.first
        val criteria = JavadocClassCriteria(ds)
        criteria.upperName().equal(strings.second.toUpperCase())
        api?.let { it.id.let { id -> criteria.apiId(id) } }
        if(pkgName != null) {
            criteria.upperPackageName().equal(pkgName.toUpperCase())
        }
        return criteria.query().asList()
    }

    fun getClass(api: JavadocApi?, pkg: String, name: String): JavadocClass? {
        val criteria: JavadocClassCriteria
        try {
            criteria = JavadocClassCriteria(ds)
            if (api != null) {
                criteria.apiId(api.id)
            }
            criteria.upperPackageName().equal(pkg.toUpperCase())
            criteria.upperName().equal(name.toUpperCase())
        } catch (e: NullPointerException) {
            throw e
        }

        return criteria.query().get()
    }

    fun getField(api: JavadocApi?, className: String, fieldName: String): List<JavadocField> {
        val criteria = JavadocFieldCriteria(ds)
        val classes = getClass(api, className)
        if (!classes.isEmpty()) {
            val javadocClass = classes[0]
            criteria.javadocClass(javadocClass)
            criteria.upperName().equal(fieldName.toUpperCase())
            return criteria.query().asList()
        }
        return emptyList()
    }

    fun getMethods(api: JavadocApi?, className: String, methodName: String,
                                                  signatureTypes: String): List<JavadocMethod> {
        val classes = getClass(api, className)
        val list = ArrayList(classes)
        val methods = ArrayList<JavadocMethod>()

        list.forEach { klass ->
            methods.addAll(getMethods(methodName, signatureTypes, klass))
            klass.parentClass?.let { methods.addAll(getMethods(methodName, signatureTypes, it)) }
            klass.interfaces.forEach { methods.addAll(getMethods(methodName, signatureTypes, it)) }
        }
        return methods
    }

    private fun getMethods(name: String, signatureTypes: String, javadocClass: JavadocClass): List<JavadocMethod> {
        val criteria = JavadocMethodCriteria(ds)
        criteria.javadocClass(javadocClass)
        criteria.upperName().equal(name.toUpperCase())
        if ("*" != signatureTypes) {
            criteria.or(
                  criteria.shortSignatureTypes().equal(signatureTypes),
                  criteria.longSignatureTypes().equal(signatureTypes))
        }
        return criteria.query().asList()
    }

    fun delete(javadocClass: JavadocClass) {
        deleteFields(javadocClass)
        deleteMethods(javadocClass)
        super.delete(javadocClass)
    }

    private fun deleteFields(javadocClass: JavadocClass) {
        val criteria = JavadocFieldCriteria(ds)
        criteria.javadocClass(javadocClass)
        ds.delete(criteria.query())
    }

    private fun deleteMethods(javadocClass: JavadocClass) {
        val criteria = JavadocMethodCriteria(ds)
        criteria.javadocClass(javadocClass)
        ds.delete(criteria.query())
    }

    fun countMethods(): Long {
        return ds.getCount(JavadocMethod::class.java)
    }

    fun deleteFor(api: JavadocApi?) {
        api?.let { api ->
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
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(JavadocClassDao::class.java)
    }
}