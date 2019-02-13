package javabot.model.javadoc

import javabot.JavabotConfig
import javabot.model.Persistent
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.IndexOptions
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist

@Entity(value = "apis", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("name")), options = IndexOptions(unique = true)),
         Index(fields = arrayOf(Field("groupId"), Field("artifactId")), options = IndexOptions(unique = true)),
         Index(fields = arrayOf(Field("upperName")), options = IndexOptions(unique = true)))
class JavadocApi : Persistent {

    @Id
    var id: ObjectId = ObjectId()

    var name: String = ""

    private var upperName: String = ""

    var baseUrl: String = ""

    var groupId: String = ""

    var artifactId: String = ""

    var version: String = ""

    private constructor()

    constructor(config: JavabotConfig, apiName: String, groupId: String = "", artifactId: String = "", version: String = "") {
        name = apiName
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
        baseUrl = "${config.url()}/javadoc/$apiName/${version}/"
    }

    @PrePersist
    fun uppers() {
        upperName = name.toUpperCase()
    }

    override fun toString(): String {
        return name
    }
}
