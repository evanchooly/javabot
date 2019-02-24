package javabot.model.javadoc

import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Field
import xyz.morphia.annotations.Index
import xyz.morphia.annotations.Indexes
import xyz.morphia.annotations.PrePersist
import xyz.morphia.annotations.Reference

@Entity(value = "fields", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("javadocClass"), Field("upperName") )),
      Index(fields = arrayOf(Field("apiId"), Field("javadocClass"), Field("upperName") )))
class JavadocField : JavadocElement {
    @Reference(lazy = true, idOnly = true)
    lateinit var javadocClass: JavadocClass
    lateinit var name: String
    lateinit var upperName: String
    lateinit var parentClassName: String

    var type: String? = null

    private constructor()

    constructor(parent: JavadocClass, fieldName: String, urlFragment: String) {
        javadocClass = parent
        name = fieldName
        apiId = parent.apiId
        url = "${parent.url}$urlFragment"
        parentClassName = parent.toString()
    }

    @PrePersist fun uppers() {
        upperName = name.toUpperCase()
    }

    override fun toString(): String {
        return "$parentClassName#$name:$type"
    }
}
