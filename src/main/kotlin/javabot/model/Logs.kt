package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.IndexOptions
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist
import java.time.LocalDateTime

@Entity(value = "logs", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("channel"), Field("upperNick"), Field("updated")), options = IndexOptions(name = "seen")))
public class Logs : Persistent {
    @Id
    var id: ObjectId? = null

    lateinit var nick: String

    public var upperNick: String? = null

    lateinit var channel: String

    public var message: String = ""

    lateinit var updated: LocalDateTime

    public enum class Type {
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

    public var type: Type? = null

    public fun isAction(): Boolean {
        return Type.ACTION == type
    }

    public fun isKick(): Boolean {
        return Type.KICK == type
    }

    public fun isServerMessage(): Boolean {
        return Type.JOIN == type || Type.PART == type || Type.QUIT == type
    }

    @PrePersist
    public fun upperNick() {
        upperNick = if (nick == null) null else nick.toUpperCase()
    }

    override fun toString(): String {
        return "Logs{ id=$id, type=$type, channel='$channel', updated=$updated, nick='$nick', message='$message', upperNick='$upperNick'}"
    }
}