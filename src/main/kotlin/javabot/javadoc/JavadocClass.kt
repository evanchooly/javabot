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
      Index(fields = arrayOf(Field("apiId"), Field("upperPackageName"), Field("upperName") )))
public class JavadocClass : JavadocElement {
    @Id
    var id: ObjectId = ObjectId()

    public var packageName: String? = null
    public var upperPackageName: String? = null
    lateinit var name: String
    public var upperName: String? = null
    public var superClassId: ObjectId? = null
//    private val methods = ArrayList<JavadocMethod>()
//    private val fields = ArrayList<JavadocField>()

    public constructor() {
    }

    public constructor(api: JavadocApi, pkg: String, name: String) {
        packageName = pkg
        this.name = name
        apiId = api.id
        directUrl = api.baseUrl + pkg.replace('.', '/') + "/" + name + ".html"
        longUrl = api.baseUrl + "index.html?" + pkg.replace('.', '/') + "/" + name + ".html"
    }

    public fun setSuperClassId(javadocClass: JavadocClass) {
        superClassId = javadocClass.id
    }

    @PrePersist
    public fun uppers() {
        upperName = name.toUpperCase()
        upperPackageName = packageName!!.toUpperCase()
    }

    override fun toString(): String {
        return packageName + "." + name
    }
}