package javabot.javadoc

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist

Entity(value = "methods", noClassnameStored = true)
Indexes(@Index(fields = @Field("apiId")), @Index(fields = { @Field("javadocClassId"), @Field("upperName") }),
      @Index(fields = { @Field("apiId"), @Field("javadocClassId"), @Field("upperName") }))
public class JavadocMethod : JavadocElement {
    Id
    private var id: ObjectId? = null
    public var javadocClassId: ObjectId? = null

    public var name: String? = null
    public var upperName: String? = null
    public var longSignatureTypes: String? = null
    public var shortSignatureTypes: String? = null
    public var paramCount: Int? = null

    private var parentClassName: String? = null

    public constructor() {
    }

    public constructor(parent: JavadocClass, name: String, count: Int,
                       longArgs: List<String>, shortArgs: List<String>) {
        this.name = name
        javadocClassId = parent.id
        apiId = parent.apiId
        parentClassName = parent.toString()

        paramCount = count
        longSignatureTypes = String.join(", ", longArgs)
        shortSignatureTypes = String.join(", ", shortArgs)
        buildUrl(parent, longArgs)
    }

    private fun buildUrl(parent: JavadocClass, longArgs: List<String>) {
        val parentUrl = parent.directUrl
        val java8 = parentUrl.contains("se/8")
        val url = StringBuilder()
        for (arg in longArgs) {
            if (url.length() != 0) {
                url.append(if (java8) "-" else ", ")
            }
            url.append(arg.replaceAll("<.*", ""))
        }
        var directUrl = if (java8)
            parentUrl + "#" + this.name + "-" + url + "-"
        else
            parentUrl + "#" + this.name + "(" + url + ")"

        longUrl = directUrl
        directUrl = directUrl
    }

    override fun getId(): ObjectId {
        return id
    }

    public fun setJavadocClassId(javadocClass: JavadocClass) {
        this.javadocClassId = javadocClass.id
        parentClassName = javadocClass.toString()
    }

    override fun setId(methodId: ObjectId) {
        id = methodId
    }

    public fun getShortSignature(): String {
        return "$name($shortSignatureTypes)"
    }

    PrePersist
    public fun uppers() {
        upperName = name!!.toUpperCase()
    }

    override fun toString(): String {
        return parentClassName + "." + getShortSignature()
    }
}
