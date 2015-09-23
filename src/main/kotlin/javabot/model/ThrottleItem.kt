package javabot.model

import java.util.Date

import java.lang.String.*
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Indexed
import org.pircbotx.User

Entity(value = "throttled", noClassnameStored = true)
public class ThrottleItem(user: User) : Persistent {
    Id
    private var id: ObjectId? = null
    public val user: String
    Indexed(expireAfterSeconds = 60)
    public val `when`: Date

    init {
        this.user = user.nick
        `when` = Date()
    }

    override fun getId(): ObjectId {
        return id
    }

    override fun setId(id: ObjectId) {
        this.id = id
    }

    override fun toString(): String {
        return format("ThrottleItem{user='%s', when=%s}", user, `when`)
    }
}