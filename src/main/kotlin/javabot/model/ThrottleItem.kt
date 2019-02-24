package javabot.model

import org.bson.types.ObjectId
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Id
import xyz.morphia.annotations.Indexed
import java.util.Date

@Entity(value = "throttled", noClassnameStored = true)
class ThrottleItem(var user: String) : Persistent {
    @Id
    var id: ObjectId? = null

    @Indexed(expireAfterSeconds = 60)
    var until: Date = Date()

    override fun toString(): String {
        return "ThrottleItem{user='%s', until=%s}".format(user, until)
    }
}
