package javabot.javadoc

import javabot.model.Persistent
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.IndexOptions
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.Arrays

Entity(value = "apis", noClassnameStored = true)
Indexes(@Index(fields = @Field("name"), options = @IndexOptions(unique = true)),
      @Index(fields = @Field("upperName"), options = @IndexOptions(unique = true)))
public class JavadocApi : Persistent {

    Id
    private var id: ObjectId? = null

    public var name: String? = null

    private var upperName: String? = null

    public var baseUrl: String? = null

    public var downloadUrl: String? = null

    public constructor() {
    }

    public constructor(apiName: String, url: String, downloadUrl: String) {
        name = apiName
        baseUrl = if (url.endsWith("/")) url else url + "/"
        this.downloadUrl = downloadUrl
    }

    override fun getId(): ObjectId {
        return id
    }

    override fun setId(id: ObjectId) {
        this.id = id
    }

    PrePersist
    public fun uppers() {
        upperName = name!!.toUpperCase()
    }

    override fun toString(): String {
        return name
    }

    companion object {
        private val log = LoggerFactory.getLogger(JavadocApi::class.java)

        public val JDK_JARS: List<String> = Arrays.asList("rt.jar", "jce.jar")
    }
}
