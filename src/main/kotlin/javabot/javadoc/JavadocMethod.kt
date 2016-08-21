package javabot.javadoc

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist
import org.mongodb.morphia.annotations.Reference

@Entity(value = "methods", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("apiId"))), Index(fields = arrayOf(Field("javadocClass"), Field("upperName") )),
      Index(fields = arrayOf(Field("apiId"), Field("javadocClass"), Field("upperName") )))
class JavadocMethod : JavadocElement {
    @Reference(lazy = true, idOnly = true)
    lateinit var javadocClass: JavadocClass
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
        javadocClass = parent
        apiId = parent.apiId
        parentClassName = parent.toString()

        paramCount = count
        longArgs.joinToString(", ")
        longSignatureTypes = longArgs.joinToString(", ")
        shortSignatureTypes = shortArgs.joinToString(", ")
        buildUrl(parent, longArgs)
    }

    private fun buildUrl(parent: JavadocClass, longArgs: List<String>) {
        val parentUrl = parent.url
        val modern = parentUrl.contains("se/8") || parentUrl.contains("ee/7")
        val url = StringBuilder()
        for (arg in longArgs) {
            if (url.length != 0) {
                url.append(if (modern) "-" else ", ")
            }
            url.append(arg.replace("<.*?>".toRegex(), ""))
        }
        var directUrl = if (modern)
            parentUrl + "#" + this.name + "-" + url + "-"
        else
            parentUrl + "#" + this.name + "(" + url + ")"

        this.url = directUrl
    }

    fun getShortSignature(): String {
        return "$name($shortSignatureTypes)"
    }

    @PrePersist
    fun uppers() {
        upperName = name.toUpperCase()
    }

    override fun toString(): String {
        return parentClassName + "." + getShortSignature()
    }
}
