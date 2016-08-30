package javabot.javadoc

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Embedded
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist
import org.mongodb.morphia.annotations.Reference

@Entity(value = "classes", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("apiId"))), Index(fields = arrayOf(Field("upperName"))),
      Index(fields = arrayOf(Field("upperPackageName"), Field("upperName") )),
      Index(fields = arrayOf(Field("apiId"), Field("upperPackageName"), Field("upperName") )))
open class JavadocClass : JavadocElement {
    lateinit var packageName: String
    lateinit var name: String
    @Reference(lazy = true, idOnly = true)
    var parentClass: JavadocClass? = null
    @Reference(lazy = true, idOnly = true)
    var interfaces = mutableListOf<JavadocClass>()
    @Reference(lazy = true, idOnly = true)
    var methods = mutableListOf<JavadocMethod>()
    @Reference(lazy = true, idOnly = true)
    var fields = mutableListOf<JavadocField>()
    var isClass = false
    var isEnum = false
    var isInterface = false
    var isAnnotation = false
    @Embedded
    var typeVariables = mutableListOf<JavadocType>()

    lateinit var upperPackageName: String
    lateinit var upperName: String

    constructor() {
    }

    constructor(api: JavadocApi, pkg: String, name: String) {
        packageName = pkg
        this.name = name
        apiId = api.id
        url = api.baseUrl + "index.html?" + pkg.replace('.', '/') + "/" + name + ".html"
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