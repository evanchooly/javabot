package javabot.dao

import com.google.inject.Inject
import dev.morphia.Datastore
import dev.morphia.query.QueryException
import dev.morphia.query.experimental.filters.Filters.eq
import dev.morphia.query.experimental.filters.Filters.or
import javabot.model.javadoc.JavadocApi
import javabot.model.javadoc.JavadocClass
import javabot.model.javadoc.JavadocField
import javabot.model.javadoc.JavadocMethod

class JavadocClassDao @Inject constructor(ds: Datastore) : BaseDao<JavadocClass>(ds, JavadocClass::class.java) {
    companion object {
        private fun calculateNameAndPackage(value: String): Pair<String?, String> {
            var clsName = value
            while (clsName.contains(".") && Character.isLowerCase(clsName[0])) {
                clsName = clsName.substring(clsName.indexOf(".") + 1)
            }
            val pkgName = if (value != clsName) {
                value.substring(0, value.indexOf(clsName) - 1)
            } else {
                null
            }

            return pkgName to clsName
        }
    }

    fun count() = ds.find(JavadocClass::class.java).count()

    fun getClass(api: JavadocApi? = null, name: String): List<JavadocClass> {
        val strings = calculateNameAndPackage(name)
        val pkgName = strings.first
        val query = ds.find(JavadocClass::class.java)
                .filter(eq("upperName", strings.second.toUpperCase()))
        api?.id?.let { id ->
            query.filter(eq("apiId", id))
        }
        if (pkgName != null) {
            query.filter(eq("upperPackageName", pkgName.toUpperCase()))
        }
        return query.iterator().toList()
    }

    private fun getClassByFqcn(fqcn: String): JavadocClass {
        try {
            return ds.find(JavadocClass::class.java)
                    .filter(eq("fqcn", fqcn))
                    .first() ?: throw QueryException("Could not find class by fqcn: $fqcn")
        } catch (e: IllegalStateException) {
            throw IllegalStateException("fqcn = ${fqcn}", e)
        }
    }

    fun getClass(api: JavadocApi? = null, pkg: String, name: String): JavadocClass? {
        val query = ds.find(JavadocClass::class.java)
        if (api != null) {
            query.filter(eq("apiId", api.id))
        }
        query.filter(eq("upperPackageName", pkg.toUpperCase()))
                .filter(eq("upperName", name.toUpperCase()))

        return query.first()
    }

    fun getField(api: JavadocApi?, className: String, fieldName: String): List<JavadocField> {
        val classes = getClass(api, className)
        return if (classes.isNotEmpty()) {
            ds.find(JavadocField::class.java)
                    .filter(eq("javadocClass", classes[0]))
                    .filter(eq("upperName", fieldName.toUpperCase()))
                    .iterator()
                    .toList()
        } else emptyList()
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
                } catch (e: QueryException) {
                    // parent type isn't in the bot
                }
            }
        }
        return methods
    }

    private fun getMethods(name: String, signatureTypes: String, javadocClass: JavadocClass): List<JavadocMethod> {
        val query = ds.find(JavadocMethod::class.java)
                .filter(eq("classId", javadocClass.id))
                .filter(eq("upperName", name.toUpperCase()))
        if ("*" != signatureTypes) {
            query.filter(or(
                    eq("shortSignatureTypes", signatureTypes),
                    eq("longSignatureTypes", signatureTypes)))
        }
        return query.iterator().toList()
    }

    fun delete(javadocClass: JavadocClass) {
        ds.find(JavadocField::class.java)
                .filter(eq("javadocClass", javadocClass))
                .delete()

        ds.find(JavadocMethod::class.java)
                .filter(eq("classId", javadocClass.id))
                .delete()

        super.delete(javadocClass)
    }
}
