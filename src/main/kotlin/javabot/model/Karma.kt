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

@Entity(value = "karma", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("upperName")))) class Karma : Serializable, Persistent {
    @Id
    var id: ObjectId? = null

    lateinit var name: String

    private var upperName: String? = null

    var value: Int = 0

    lateinit var userName: String

    var updated: LocalDateTime? = null

    constructor() {
    }

    constructor(name: String, value: Int, userName: String) {
        this.name = name
        this.value = value
        this.userName = userName
    }

    @PrePersist fun uppers() {
        upperName = name.toUpperCase()
    }
}