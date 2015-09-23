package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexed
import org.mongodb.morphia.annotations.Indexes

import java.io.Serializable
import java.time.LocalDateTime

Entity(value = "shuns", noClassnameStored = true)
Indexes(@Index(fields = @Field("upperNick")))
public class Shun : Serializable, Persistent {
    Id
    private var id: ObjectId? = null

    Indexed(unique = true)
    public var nick: String? = null

    public var upperNick: String? = null

    public var expiry: LocalDateTime? = null

    override fun getId(): ObjectId {
        return id
    }

    override fun setId(id: ObjectId) {
        this.id = id
    }
}
