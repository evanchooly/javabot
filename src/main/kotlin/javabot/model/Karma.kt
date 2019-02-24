package javabot.model

import org.bson.types.ObjectId
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Field
import xyz.morphia.annotations.Id
import xyz.morphia.annotations.Index
import xyz.morphia.annotations.Indexes
import xyz.morphia.annotations.PrePersist
import java.io.Serializable
import java.time.LocalDateTime

@Entity(value = "karma", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("upperName"))),
        Index(fields = arrayOf(Field("value"), Field("name"))))
class Karma : Serializable, Persistent {
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
