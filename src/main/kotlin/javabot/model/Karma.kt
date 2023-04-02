package javabot.model

import org.bson.types.ObjectId
import dev.morphia.annotations.Entity
import dev.morphia.annotations.Field
import dev.morphia.annotations.Id
import dev.morphia.annotations.Index
import dev.morphia.annotations.Indexes
import dev.morphia.annotations.PrePersist
import java.io.Serializable
import java.time.LocalDateTime

@Entity(value = "karma", useDiscriminator = false)
@Indexes(Index(fields = arrayOf(Field("upperName"))),
        Index(fields = arrayOf(Field("value"), Field("name"))))
class Karma : Serializable, Persistent {
    @Id
    var id: ObjectId? = null

    lateinit var name: String

    var upperName: String? = null

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
