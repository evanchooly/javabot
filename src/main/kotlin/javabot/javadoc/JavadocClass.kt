package javabot.javadoc

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist

@Entity(value = "classes", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("apiId"))), Index(fields = arrayOf(Field("upperName"))),
      Index(fields = arrayOf(Field("upperPackageName"), Field("upperName") )),
      Index(fields = arrayOf(Field("apiId"), Field("upperPackageName"), Field("upperName") ))) class JavadocClass : JavadocElement {
    @Id
    var id: ObjectId = ObjectId()

    lateinit var packageName: String
    lateinit var upperPackageName: String
    lateinit var name: String
    lateinit var upperName: String
    var superClassId: ObjectId? = null
//    private val methods = ArrayList<JavadocMethod>()
//    private val fields = ArrayList<JavadocField>()

    constructor() {
    }

    constructor(api: JavadocApi, pkg: String, name: String) {
        packageName = pkg
        this.name = name
        apiId = api.id
        directUrl = api.baseUrl + pkg.replace('.', '/') + "/" + name + ".html"
        longUrl = api.baseUrl + "index.html?" + pkg.replace('.', '/') + "/" + name + ".html"
    }

    fun setSuperClassId(javadocClass: JavadocClass) {
        superClassId = javadocClass.id
    }

    @PrePersist fun uppers() {
        upperName = name.toUpperCase()
        upperPackageName = packageName.toUpperCase()
    }

    override fun toString(): String {
        return packageName + "." + name
    }
}