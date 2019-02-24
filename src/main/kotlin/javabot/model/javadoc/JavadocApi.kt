package javabot.model.javadoc

import javabot.JavabotConfig
import javabot.model.Persistent
import org.bson.types.ObjectId
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Field
import xyz.morphia.annotations.Id
import xyz.morphia.annotations.Index
import xyz.morphia.annotations.IndexOptions
import xyz.morphia.annotations.Indexes
import xyz.morphia.annotations.PrePersist

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
