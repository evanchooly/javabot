package javabot.model

import com.fasterxml.jackson.annotation.JsonView
import javabot.json.Views.PUBLIC
import javabot.json.Views.SYSTEM
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.IndexOptions
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist
import java.io.Serializable
import java.time.LocalDateTime

@Entity(value = "channels", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("upperName")), options = IndexOptions(unique = true, dropDups = true)))
class Channel : Serializable, Persistent {
    @Id
    var id: ObjectId? = null

    @JsonView(PUBLIC::class)
    lateinit var name: String

    @JsonView(SYSTEM::class)
    lateinit var upperName: String

    @JsonView(PUBLIC::class) var key: String? = null

    @JsonView(PUBLIC::class) var updated: LocalDateTime? = null

    @JsonView(PUBLIC::class)
    var logged: Boolean = true

    constructor() {
    }

    constructor(name: String, logged: Boolean = true) {
        this.name = name
        this.logged = logged
    }

    constructor(name: String, key: String, logged: Boolean = true) {
        this.name = name
        this.key = key
        this.logged = logged
    }

    constructor(id: ObjectId, name: String, key: String, logged: Boolean = true) {
        this.id = id
        this.name = name
        this.key = key
        this.logged = logged
    }

    override fun toString(): String {
        return "Channel{id=$id, logged=$logged, name='$name', updated=$updated}"
    }

    @PrePersist fun uppers() {
        upperName = name.toUpperCase()
    }
}