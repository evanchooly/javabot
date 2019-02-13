package javabot.model.javadoc

import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.IndexOptions
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist

@Entity(value = "classes", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("apiId"))), Index(fields = arrayOf(Field("upperName"))),
      Index(fields = arrayOf(Field("upperPackageName"), Field("upperName") )),
      Index(fields = arrayOf(Field("apiId"), Field("upperPackageName"), Field("upperName") ), options = IndexOptions(unique = true)))
open class JavadocClass : JavadocElement {
    lateinit var packageName: String
    lateinit var name: String
    lateinit var fqcn: String
    val parentTypes = mutableListOf<String>()
    var isClass: Boolean = false
    var isEnum: Boolean = false
    var isInterface: Boolean = false
    var isAnnotation: Boolean = false

    lateinit var upperPackageName: String
    lateinit var upperName: String

    constructor()

    constructor(api: JavadocApi, pkg: String, name: String) {
        packageName = pkg
        this.name = name
        fqcn = "$packageName.$name"
        apiId = api.id
        url = "${api.baseUrl}index.html?${packageName.replace('.', '/')}/${name}.html"
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
