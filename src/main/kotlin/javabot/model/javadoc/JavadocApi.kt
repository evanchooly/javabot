package javabot.model.javadoc

import javabot.JavabotConfig
import javabot.model.Persistent
import org.bson.types.ObjectId
import dev.morphia.annotations.Entity
import dev.morphia.annotations.Field
import dev.morphia.annotations.Id
import dev.morphia.annotations.Index
import dev.morphia.annotations.IndexOptions
import dev.morphia.annotations.Indexes
import dev.morphia.annotations.PrePersist

@Entity(value = "apis", useDiscriminator = false)
@Indexes(Index(fields = arrayOf(Field("name")), options = IndexOptions(unique = true)),
         Index(fields = arrayOf(Field("groupId"), Field("artifactId")), options = IndexOptions(unique = true)),
         Index(fields = arrayOf(Field("upperName")), options = IndexOptions(unique = true)))
class JavadocApi : Persistent {

    @Id
    var id: ObjectId = ObjectId()

    var name: String = ""

    var upperName: String = ""

    var baseUrl: String = ""

    var groupId: String = ""

    var artifactId: String = ""

    var version: String = ""

    constructor()

    constructor(config: JavabotConfig, apiName: String, groupId: String = "", artifactId: String = "", version: String = "") {
        name = apiName
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = version
        baseUrl = if (apiName == "JDK") {
            "https://docs.oracle.com/en/java/javase/$version/docs/api"
        } else {
            "${config.url()}/javadoc/$apiName/${version}/"
        }
    }

    @PrePersist
    fun uppers() {
        upperName = name.toUpperCase()
    }

    override fun toString(): String {
        return name
    }
}
