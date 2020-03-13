package javabot.model

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import dev.morphia.annotations.IndexOptions
import dev.morphia.annotations.Indexed
import org.bson.types.ObjectId
import java.util.Date

@Entity(value = "throttled", useDiscriminator = false)
class ThrottleItem(var user: String) : Persistent {
    @Id
    var id: ObjectId? = null

    @Indexed(options = IndexOptions(expireAfterSeconds = 60))
    var until: Date = Date()

    constructor(): this("")

    override fun toString(): String {
        return "ThrottleItem{user='%s', until=%s}".format(user, until)
    }
}
