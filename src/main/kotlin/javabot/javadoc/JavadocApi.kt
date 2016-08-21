package javabot.javadoc

import javabot.model.ApiEvent
import javabot.model.ApiEvent.Companion
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
      Index(fields = arrayOf(Field("upperName")), options = IndexOptions(unique = true)))
class JavadocApi : Persistent {

    @Id
    var id: ObjectId = ObjectId()

    lateinit var name: String

    private var upperName: String? = null

    lateinit var baseUrl: String

    lateinit var groupId: String

    lateinit var artifactId: String

    lateinit var version: String

    private constructor() {
        groupId = ""
        artifactId = ""
        version = ""
    }

    constructor(apiName: String, url: String, groupId: String, artifactId: String, version: String) {
        name = apiName
        this.groupId = groupId
        this.artifactId = artifactId
        this.version = if (name == "JDK") System.getProperty("java.specification.version") else version
        baseUrl = if (url.endsWith("/")) url else url + "/"
        baseUrl = "${baseUrl}javadoc/$apiName/${this.version}/"
    }

    @PrePersist fun uppers() {
        upperName = name.toUpperCase()
    }

    override fun toString(): String {
        return name
    }
}
