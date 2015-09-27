package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Indexed
import org.pircbotx.User
import java.util.Date

@Entity(value = "throttled", noClassnameStored = true)
public class ThrottleItem(val user: User) : Persistent {
    override var id: ObjectId? = null
    @Indexed(expireAfterSeconds = 60)
    public var until = Date()

    override fun toString(): String {
        return "ThrottleItem{user='%s', until=%s}".format(user, until)
    }
}