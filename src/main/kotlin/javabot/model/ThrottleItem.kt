package javabot.model

import org.bson.types.ObjectId
import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import dev.morphia.annotations.Indexed
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
