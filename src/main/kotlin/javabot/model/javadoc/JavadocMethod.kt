package javabot.model.javadoc

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist

@Entity(value = "methods", noClassnameStored = true)
@Indexes(
        Index(fields = arrayOf(Field("apiId"))),
        Index(fields = arrayOf(Field("javadocClass"), Field("upperName") )),
        Index(fields = arrayOf(Field("apiId"), Field("javadocClass"), Field("upperName") )))
class JavadocMethod : JavadocElement {
    lateinit var javadocClass: ObjectId
    var isConstructor: Boolean = false
    lateinit var name: String
    lateinit var upperName: String
    lateinit var longSignatureTypes: String
    lateinit var shortSignatureTypes: String
    var paramCount: Int = 0

    lateinit var parentClassName: String

    constructor()

    constructor(parent: JavadocClass, name: String, urlFragment: String, longArgs: List<String>, shortArgs: List<String>) {
        this.name = name
        isConstructor = name == parent.name
        javadocClass = parent.id
        apiId = parent.apiId
        parentClassName = parent.toString()

        paramCount = longArgs.size
        longArgs.joinToString(", ")
        longSignatureTypes = longArgs.joinToString(", ")
        shortSignatureTypes = shortArgs.joinToString(", ")
        this.url = "${parent.url}${urlFragment}"
    }

    fun getShortSignature(): String {
        return "$name($shortSignatureTypes)"
    }

    @PrePersist
    fun uppers() {
        upperName = name.toUpperCase()
    }

    override fun toString(): String {
        return parentClassName + "#" + getShortSignature()
    }
}
