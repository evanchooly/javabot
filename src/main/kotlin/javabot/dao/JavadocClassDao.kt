package javabot.dao

import com.google.inject.Inject
import javabot.javadoc.JavadocParser
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import javabot.model.javadoc.JavadocMethod
import javabot.model.javadoc.criteria.JavadocClassCriteria
import javabot.model.javadoc.criteria.JavadocFieldCriteria
import javabot.model.javadoc.criteria.JavadocMethodCriteria
import org.mongodb.morphia.Datastore
import java.util.ArrayList

class JavadocClassDao @Inject constructor(ds: Datastore)  : BaseDao<JavadocClass>(ds, JavadocClass::class.java) {

    fun count() = ds.getCount(JavadocClass::class.java)

    fun getClass(api: JavadocApi? = null, name: String): List<JavadocClass> {
        val strings = JavadocParser.calculateNameAndPackage(name)
        val pkgName = strings.first
        val criteria = JavadocClassCriteria(ds)
        criteria.upperName().equal(strings.second.toUpperCase())
        api?.let { it.id.let { id -> criteria.apiId(id) } }
        if(pkgName != null) {
            criteria.upperPackageName().equal(pkgName.toUpperCase())
        }
        return criteria.query().asList()
    }

    private fun getClassByFqcn(fqcn: String): JavadocClass {
        val criteria = JavadocClassCriteria(ds)
        criteria.fqcn().equal(fqcn)
        try {
            return criteria.query().get()
        } catch(e: IllegalStateException) {
            throw IllegalStateException("fqcn = ${fqcn}", e)
        }
    }

    fun getClass(api: JavadocApi? = null, pkg: String, name: String): JavadocClass? {
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
            klass.parentTypes.forEach {
                try {
                    methods += getMethods(methodName, signatureTypes, getClassByFqcn(it))
                } catch(e: IllegalStateException) {
                    println("it = ${it}")
                    println("klass = ${klass}")
                    println("klass.parentTypes = ${klass.parentTypes}")
                    throw IllegalStateException("klass = ${klass}", e)
                }
            }
        }
        return methods
    }

    private fun getMethods(name: String, signatureTypes: String, javadocClass: JavadocClass): List<JavadocMethod> {
        val criteria = JavadocMethodCriteria(ds)
        criteria.classId(javadocClass.id)
        criteria.upperName().equal(name.toUpperCase())
        if ("*" != signatureTypes) {
            criteria.or(
                  criteria.shortSignatureTypes().equal(signatureTypes),
                  criteria.longSignatureTypes().equal(signatureTypes))
        }
        return criteria.query().asList()
    }

    fun delete(javadocClass: JavadocClass) {
        JavadocFieldCriteria(ds).apply {
            javadocClass(javadocClass)
            ds.delete(query())
        }

        JavadocMethodCriteria(ds).apply {
            classId(javadocClass.id)
            ds.delete(query())
        }

        super.delete(javadocClass)
    }
}
