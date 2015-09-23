package javabot.javadoc

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist

import java.util.ArrayList

Entity(value = "classes", noClassnameStored = true)
Indexes(@Index(fields = @Field("apiId")), @Index(fields = @Field("upperName")),
      @Index(fields = { @Field("upperPackageName"), @Field("upperName") }),
      @Index(fields = { @Field("apiId"), @Field("upperPackageName"), @Field("upperName") }))
public class JavadocClass : JavadocElement {
    Id
    private var id: ObjectId? = null

    public var packageName: String? = null
    public var upperPackageName: String? = null
    public var name: String? = null
    public var upperName: String? = null
    public var superClassId: ObjectId? = null
    private val methods = ArrayList<JavadocMethod>()
    private val fields = ArrayList<JavadocField>()

    public constructor() {
    }

    public constructor(api: JavadocApi, pkg: String, name: String) {
        packageName = pkg
        this.name = name
        apiId = api.id
        directUrl = api.baseUrl + pkg.replace('.', '/') + "/" + name + ".html"
        longUrl = api.baseUrl + "index.html?" + pkg.replace('.', '/') + "/" + name + ".html"
    }

    override fun getId(): ObjectId {
        return id
    }

    override fun setId(classId: ObjectId) {
        id = classId
    }

    public fun setSuperClassId(javadocClass: JavadocClass) {
        superClassId = javadocClass.getId()
    }

    PrePersist
    public fun uppers() {
        upperName = name!!.toUpperCase()
        upperPackageName = packageName!!.toUpperCase()
    }

    override fun toString(): String {
        return packageName + "." + name
    }
}