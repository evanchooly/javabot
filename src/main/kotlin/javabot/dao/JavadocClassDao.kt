package javabot.dao

import javabot.javadoc.criteria.JavadocClassCriteria
import javabot.javadoc.criteria.JavadocFieldCriteria
import javabot.javadoc.criteria.JavadocMethodCriteria
import javabot.javadoc.JavadocApi
import javabot.javadoc.JavadocClass
import javabot.javadoc.JavadocClassVisitor
import javabot.javadoc.JavadocField
import javabot.javadoc.JavadocMethod
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.ArrayList
import java.util.Collections

public class JavadocClassDao protected constructor() : BaseDao<JavadocClass>(JavadocClass::class.java) {

    SuppressWarnings("unchecked")
    public fun getClass(api: JavadocApi?, name: String): List<JavadocClass> {
        val strings = JavadocClassVisitor.calculateNameAndPackage(name)
        val pkgName = strings[0]
        val criteria = JavadocClassCriteria(ds)
        criteria.upperName().equal(strings[1].toUpperCase())
        if (api != null) {
            criteria.apiId(api.id)
        }
        if (pkgName != null) {
            criteria.upperPackageName().equal(pkgName.toUpperCase())
        }
        return criteria.query().asList()
    }

    SuppressWarnings("unchecked")
    public fun getClass(api: JavadocApi, pkg: String, name: String): Array<JavadocClass> {
        val criteria: JavadocClassCriteria
        try {
            criteria = JavadocClassCriteria(ds)
            criteria.apiId(api.id)
            criteria.upperPackageName().equal(pkg.toUpperCase())
            criteria.upperName().equal(name.toUpperCase())
        } catch (e: NullPointerException) {
            LOG.debug("pkg = " + pkg)
            LOG.debug("name = " + name)
            throw e
        }

        return criteria.query().asList().toArray(arrayOfNulls<JavadocClass>(0))
    }

    SuppressWarnings("unchecked")
    public fun getField(api: JavadocApi, className: String, fieldName: String): List<JavadocField> {
        val criteria = JavadocFieldCriteria(ds)
        val classes = getClass(api, className)
        if (!classes.isEmpty()) {
            val javadocClass = classes.get(0)
            criteria.javadocClassId(javadocClass.id)
            criteria.upperName().equal(fieldName.toUpperCase())
            return criteria.query().asList()
        }
        return emptyList()
    }

    SuppressWarnings("unchecked")
    public fun getMethods(api: JavadocApi, className: String, methodName: String,
                          signatureTypes: String): List<JavadocMethod> {
        val classes = getClass(api, className)
        val list = ArrayList(classes)
        val methods = ArrayList<JavadocMethod>()

        while (!list.isEmpty()) {
            val javadocClass = list.remove(0)
            if (javadocClass != null) {
                methods.addAll(getMethods(methodName, signatureTypes, javadocClass))
                val superClassId = javadocClass.superClassId
                if (superClassId != null) {
                    list.add(find(superClassId))
                }
            }
        }
        return methods
    }

    private fun getMethods(name: String, signatureTypes: String, javadocClass: JavadocClass): List<Any> {
        val criteria = JavadocMethodCriteria(ds)
        criteria.javadocClassId(javadocClass.id)
        criteria.upperName().equal(name.toUpperCase())
        if ("*" != signatureTypes) {
            criteria.or(
                  criteria.shortSignatureTypes().equal(signatureTypes),
                  criteria.longSignatureTypes().equal(signatureTypes))
        }
        return criteria.query().asList()
    }

    public fun delete(javadocClass: JavadocClass) {
        deleteFields(javadocClass)
        deleteMethods(javadocClass)
        super.delete(javadocClass)
    }

    private fun deleteFields(javadocClass: JavadocClass) {
        val criteria = JavadocFieldCriteria(ds)
        criteria.javadocClassId(javadocClass.id)
        ds.delete(criteria.query())
    }

    private fun deleteMethods(javadocClass: JavadocClass) {
        val criteria = JavadocMethodCriteria(ds)
        criteria.javadocClassId(javadocClass.id)
        ds.delete(criteria.query())
    }

    public fun countMethods(): Long {
        return ds.getCount(JavadocMethod::class.java)
    }

    public fun deleteFor(api: JavadocApi?) {
        if (api != null) {
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