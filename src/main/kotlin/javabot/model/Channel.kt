package javabot.model

import com.fasterxml.jackson.annotation.JsonView
import javabot.json.Views.PUBLIC
import javabot.json.Views.SYSTEM
import org.bson.types.ObjectId
import dev.morphia.annotations.Entity
import dev.morphia.annotations.Field
import dev.morphia.annotations.Id
import dev.morphia.annotations.Index
import dev.morphia.annotations.IndexOptions
import dev.morphia.annotations.Indexes
import dev.morphia.annotations.PrePersist
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Locale

@Entity(value = "channels", useDiscriminator = false)
@Indexes(Index(fields = arrayOf(Field("upperName")), options = IndexOptions(unique = true)))
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

    @PrePersist
    fun uppers() {
        upperName = name.uppercase(Locale.getDefault())
    }
}
