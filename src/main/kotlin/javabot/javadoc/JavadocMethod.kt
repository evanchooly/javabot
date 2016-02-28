package javabot.javadoc

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist

@Entity(value = "methods", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("apiId"))), Index(fields = arrayOf(Field("javadocClassId"), Field("upperName") )),
      Index(fields = arrayOf(Field("apiId"), Field("javadocClassId"), Field("upperName") ))) class JavadocMethod : JavadocElement {
    @Id
    var id: ObjectId? = null
    lateinit var javadocClassId: ObjectId
    lateinit var name: String
    lateinit var upperName: String
    lateinit var longSignatureTypes: String
    lateinit var shortSignatureTypes: String
    var paramCount: Int = 0

    lateinit var parentClassName: String

    constructor() {
    }

    constructor(parent: JavadocClass, name: String, count: Int,
                       longArgs: List<String>, shortArgs: List<String>) {
        this.name = name
        javadocClassId = parent.id
        apiId = parent.apiId
        parentClassName = parent.toString()

        paramCount = count
        longArgs.joinToString(", ")
        longSignatureTypes = longArgs.joinToString(", ")
        shortSignatureTypes = shortArgs.joinToString(", ")
        buildUrl(parent, longArgs)
    }

    private fun buildUrl(parent: JavadocClass, longArgs: List<String>) {
        val parentUrl = parent.directUrl
        val java8 = parentUrl?.contains("se/8") ?: false
        val url = StringBuilder()
        for (arg in longArgs) {
            if (url.length != 0) {
                url.append(if (java8) "-" else ", ")
            }
            url.append(arg.replace("<.*".toRegex(), ""))
        }
        var directUrl = if (java8)
            parentUrl + "#" + this.name + "-" + url + "-"
        else
            parentUrl + "#" + this.name + "(" + url + ")"

        longUrl = directUrl
        this.directUrl = directUrl
    }

/*
    public fun setJavadocClassId(javadocClass: JavadocClass) {
        this.javadocClassId = javadocClass.id
        parentClassName = javadocClass.toString()
    }
*/

    fun getShortSignature(): String {
        return "$name($shortSignatureTypes)"
    }

    @PrePersist fun uppers() {
        upperName = name.toUpperCase()
    }

    override fun toString(): String {
        return parentClassName + "." + getShortSignature()
    }
}
