package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist
import java.io.Serializable
import java.time.LocalDateTime

@Entity(value = "karma", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("upperName"))))
public class Karma : Serializable, Persistent {
    override var id: ObjectId? = null

    lateinit var name: String

    private var upperName: String? = null

    public var value: Int = 0

    lateinit var userName: String

    public var updated: LocalDateTime? = null

    public constructor() {
    }

    public constructor(name: String, value: Int, userName: String) {
        this.name = name
        this.value = value
        this.userName = userName
    }

    @PrePersist
    public fun uppers() {
        upperName = name.toUpperCase()
    }
}