package javabot.javadoc

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist

@Entity(value = "fields", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("javadocClassId"), Field("upperName") )),
      Index(fields = arrayOf(Field("apiId"), Field("javadocClassId"), Field("upperName") )))
public class JavadocField : JavadocElement {
    @Id
    var id: ObjectId? = null

    public var javadocClassId: ObjectId? = null
    public var name: String? = null
    private var upperName: String? = null
    private var parentClassName: String? = null

    public var type: String? = null

    public constructor() {
    }

    public constructor(parent: JavadocClass, fieldName: String, fieldType: String) {

        javadocClassId = parent.id
        name = fieldName
        type = fieldType
        apiId = parent.apiId
        val url = parent.directUrl + "#" + name
        longUrl = url
        directUrl = url
        parentClassName = parent.toString()
    }

    public fun setJavadocClassId(javadocClass: JavadocClass) {
        this.javadocClassId = javadocClass.id
        parentClassName = javadocClass.toString()
    }

    @PrePersist
    public fun uppers() {
        upperName = name!!.toUpperCase()
    }

    override fun toString(): String {
        return "$parentClassName#$name:$type"
    }
}