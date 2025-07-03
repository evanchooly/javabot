package javabot.model.javadoc

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Field
import dev.morphia.annotations.Index
import dev.morphia.annotations.Indexes
import dev.morphia.annotations.PrePersist
import java.util.Locale
import org.bson.types.ObjectId

@Entity(value = "methods", useDiscriminator = false)
@Indexes(
    Index(fields = arrayOf(Field("apiId"))),
    Index(fields = arrayOf(Field("classId"), Field("upperName"))),
    Index(fields = arrayOf(Field("apiId"), Field("classId"), Field("upperName"))),
)
class JavadocMethod() : JavadocElement() {
    lateinit var classId: ObjectId
    var isConstructor: Boolean = false
    lateinit var name: String
    lateinit var upperName: String
    lateinit var longSignatureTypes: String
    lateinit var shortSignatureTypes: String
    var paramCount: Int = 0

    lateinit var parentClassName: String

    constructor(
        parent: JavadocClass,
        name: String,
        urlFragment: String,
        longArgs: List<String>,
        shortArgs: List<String>,
    ) : this() {
        this.name = name
        isConstructor = name == "<init>"
        classId = parent.id!!
        apiId = parent.apiId
        parentClassName = parent.toString()

        paramCount = longArgs.size
        longArgs.joinToString(", ")
        longSignatureTypes = longArgs.joinToString(", ")
        shortSignatureTypes = shortArgs.joinToString(", ")
        this.url = "${parent.url}${urlFragment}"
        if (isConstructor) {
            this.name = parent.name
        }
    }

    fun getShortSignature(): String {
        return "$name($shortSignatureTypes)"
    }

    @PrePersist
    fun uppers() {
        upperName = name.uppercase(Locale.getDefault())
    }

    override fun toString(): String {
        return parentClassName + "#" + getShortSignature()
    }
}
