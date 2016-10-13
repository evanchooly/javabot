package javabot.javadoc

import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist

@Entity(value = "classes", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("apiId"))), Index(fields = arrayOf(Field("upperName"))),
      Index(fields = arrayOf(Field("upperPackageName"), Field("upperName") )),
      Index(fields = arrayOf(Field("apiId"), Field("upperPackageName"), Field("upperName") )))
open class JavadocClass : JavadocElement {
    lateinit var packageName: String
    lateinit var name: String
    lateinit var fqcn: String
    var parentClass: String? = null
    var interfaces = mutableListOf<String>()
    var isClass = false
    var isEnum = false
    var isInterface = false
    var isAnnotation = false
    var typeVariables = mutableListOf<String>()

    lateinit var upperPackageName: String
    lateinit var upperName: String

    constructor() {
    }

    constructor(api: JavadocApi, pkg: String, name: String) {
        packageName = pkg
        this.name = name
        fqcn = "$packageName.$name"
        apiId = api.id
        url = api.baseUrl + "index.html?" + fqcn.replace('.', '/') + ".html"
    }

    @PrePersist
    fun uppers() {
        upperName = name.toUpperCase()
        upperPackageName = packageName.toUpperCase()
    }

    override fun toString(): String {
        return packageName + "." + name
    }
}

enum class Visibility {
    PackagePrivate,
    Private,
    Protected,
    Public
}
