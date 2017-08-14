package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.*
import java.io.Serializable
import java.net.URL
import java.time.LocalDateTime

@Entity(value = "links", noClassnameStored = true)
//@Indexes(Index(fields = arrayOf(Field("upperName"))))
class Link(var channel:String="", var username:String="", var url:String="", var info:String="",var approved:Boolean? = null) : Serializable, Persistent {
    @Id
    var id: ObjectId? = null

    var updated: LocalDateTime? = null

    var created: LocalDateTime? = null

    var displayed:Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Link) return false

        if (channel != other.channel) return false
        if (username != other.username) return false
        if (url != other.url) return false
        if (info != other.info) return false
        if (updated != other.updated) return false
        if (approved != other.approved) return false

        return true
    }

    override fun hashCode(): Int {
        var result = channel.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + info.hashCode()
        result = 31 * result + (created?.hashCode() ?: 0)
        result = 31 * result + (approved?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Link(channel='$channel', username='$username', url='$url', info='$info', id=$id, updated=$updated, created=$created, approved=$approved)"
    }


}