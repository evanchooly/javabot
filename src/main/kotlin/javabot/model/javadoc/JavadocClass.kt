package javabot.model.javadoc

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Field
import dev.morphia.annotations.Index
import dev.morphia.annotations.IndexOptions
import dev.morphia.annotations.Indexes
import dev.morphia.annotations.PrePersist

@Entity(value = "classes", useDiscriminator = false)
@Indexes(Index(fields = arrayOf(Field("apiId"))), Index(fields = arrayOf(Field("upperName"))),
      Index(fields = arrayOf(Field("upperPackageName"), Field("upperName") )),
      Index(fields = arrayOf(Field("fqcn")), options = IndexOptions(unique = true)),
      Index(fields = arrayOf(Field("apiId"), Field("upperPackageName"), Field("upperName") ), options = IndexOptions(unique = true)))
open class JavadocClass : JavadocElement {
    lateinit var packageName: String
    lateinit var name: String
    lateinit var fqcn: String
    lateinit var module: String
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
        this.module = module ?: ""
        fqcn = "$packageName.$name"
        apiId = api.id
        url = if(this.module.isNotEmpty())
            "${api.baseUrl}${module}/${packageName.replace('.', '/')}/${name}.html"
        else
            "${api.baseUrl}${packageName.replace('.', '/')}/${name}.html"
    }

    @PrePersist
    fun uppers() {
        upperName = name.toUpperCase()
        upperPackageName = packageName.toUpperCase()
    }

    override fun toString(): String {
        return "$packageName.$name [${apiId}]"
    }
}

enum class Visibility {
    PackagePrivate,
    Private,
    Protected,
    Public
}
