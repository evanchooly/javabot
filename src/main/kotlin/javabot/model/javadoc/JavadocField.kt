package javabot.model.javadoc

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Field
import dev.morphia.annotations.Index
import dev.morphia.annotations.Indexes
import dev.morphia.annotations.PrePersist
import dev.morphia.annotations.Reference

@Entity(value = "fields", useDiscriminator = false)
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
