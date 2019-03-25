package javabot.model

import javabot.model.Logs.Type
import org.bson.types.ObjectId
import dev.morphia.annotations.Entity
import dev.morphia.annotations.Field
import dev.morphia.annotations.Id
import dev.morphia.annotations.Index
import dev.morphia.annotations.IndexOptions
import dev.morphia.annotations.Indexes
import dev.morphia.annotations.PrePersist
import java.time.LocalDateTime

@Entity(value = "logs", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("channel"), Field("upperNick"), Field("updated")),
        options = IndexOptions(name = "seen")))
class Logs : Persistent {
    enum class Type {
        ACTION,
        BAN,
        DISCONNECTED,
        ERROR,
        INVITE,
        JOIN,
        PART,
        KICK,
        MESSAGE,
        QUIT,
        REGISTERED,
        TOPIC,
        NICK
    }

    @Id
    var id: ObjectId = ObjectId()
    
    var nick: String? = null

    var channel: String? = null

    lateinit var message: String

    lateinit var updated: LocalDateTime

    lateinit var type: Type

    var upperNick: String? = null

    private constructor() {
    }

    constructor(nick: String?, message: String, type: Type, channel: String?, updated: LocalDateTime = LocalDateTime.now()) : this() {
        this.nick = nick
        this.message = message
        this.type = type
        this.updated = updated
        this.channel = channel
        upperNick()
    }

    fun isAction(): Boolean {
        return Type.ACTION == type
    }

    fun isKick(): Boolean {
        return Type.KICK == type
    }

    fun isServerMessage(): Boolean {
        return Type.JOIN == type || Type.PART == type || Type.QUIT == type
    }

    @PrePersist
    fun upperNick() {
        upperNick = nick?.toUpperCase()
    }

    override fun toString(): String {
        return "Logs{ id=$id, type=$type, channel='$channel', updated=$updated, nick='$nick', message='$message', upperNick='$upperNick'}"
    }
}
