package javabot.model.javadoc

import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Field
import xyz.morphia.annotations.Index
import xyz.morphia.annotations.IndexOptions
import xyz.morphia.annotations.Indexes
import xyz.morphia.annotations.PrePersist

@Entity(value = "classes", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("apiId"))), Index(fields = arrayOf(Field("upperName"))),
      Index(fields = arrayOf(Field("upperPackageName"), Field("upperName") )),
      Index(fields = arrayOf(Field("apiId"), Field("upperPackageName"), Field("upperName") ), options = IndexOptions(unique = true)))
open class JavadocClass : JavadocElement {
    lateinit var packageName: String
    lateinit var name: String
    lateinit var fqcn: String
    var module: String? = null
    val parentTypes: MutableList<String> = mutableListOf()
    var isClass: Boolean = false
    var isEnum: Boolean = false
    var isInterface: Boolean = false

    var isAnnotation: Boolean = false
    lateinit var upperPackageName: String

    lateinit var upperName: String

    constructor()

    constructor(api: JavadocApi, module: String?, pkg: String, name: String) {
        packageName = pkg
        this.name = name
        this.module = module
        fqcn = "$packageName.$name"
        apiId = api.id
        url = if(module != null)
            "${api.baseUrl}${module}/${packageName.replace('.', '/')}/${name}.html"
        else
            "${api.baseUrl}index.html?${packageName.replace('.', '/')}/${name}.html"
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
