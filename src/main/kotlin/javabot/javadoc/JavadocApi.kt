package javabot.javadoc

import javabot.model.Persistent
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.IndexOptions
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist
import org.slf4j.LoggerFactory
import java.util.Arrays

@Entity(value = "apis", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("name")), options = IndexOptions(unique = true)),
      Index(fields = arrayOf(Field("upperName")), options = IndexOptions(unique = true)))
class JavadocApi : Persistent {

    companion object {
        private val log = LoggerFactory.getLogger(JavadocApi::class.java)

        public val JDK_JARS: List<String> = Arrays.asList("rt.jar", "jce.jar")
    }

    override var id: ObjectId? = null

    lateinit var name: String

    private var upperName: String? = null

    public var baseUrl: String? = null

    lateinit var downloadUrl: String

    public constructor() {
    }

    public constructor(apiName: String, url: String, downloadUrl: String) {
        name = apiName
        baseUrl = if (url.endsWith("/")) url else url + "/"
        this.downloadUrl = downloadUrl
    }

    @PrePersist
    public fun uppers() {
        upperName = name.toUpperCase()
    }

    override fun toString(): String {
        return name
    }
}
