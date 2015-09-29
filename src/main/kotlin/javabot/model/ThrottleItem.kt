package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Indexed
import org.pircbotx.User
import java.util.Date

@Entity(value = "throttled", noClassnameStored = true)
public class ThrottleItem(ircUser: User) : Persistent {
    @Id
    var id: ObjectId? = null
    var user: String
    @Indexed(expireAfterSeconds = 60)
    public var until = Date()

    init {
        user = ircUser.nick
    }
    override fun toString(): String {
        return "ThrottleItem{user='%s', until=%s}".format(user, until)
    }
}