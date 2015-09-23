package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist

import java.io.Serializable
import java.time.LocalDateTime

Entity(value = "karma", noClassnameStored = true)
Indexes(@Index(fields = @Field("upperName")))
public class Karma : Serializable, Persistent {
    Id
    private var id: ObjectId? = null

    public var name: String? = null

    private var upperName: String? = null

    public var value: Int? = 0

    public var userName: String? = null

    public var updated: LocalDateTime? = null

    public constructor() {
    }

    public constructor(name: String, value: Int?, userName: String) {
        this.name = name
        this.value = value
        this.userName = userName
    }

    override fun getId(): ObjectId {
        return id
    }

    override fun setId(karmaId: ObjectId) {
        id = karmaId
    }

    PrePersist
    public fun uppers() {
        upperName = name!!.toUpperCase()
    }
}