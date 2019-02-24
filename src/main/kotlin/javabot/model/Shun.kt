package javabot.model

import org.bson.types.ObjectId
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Field
import xyz.morphia.annotations.Id
import xyz.morphia.annotations.Index
import xyz.morphia.annotations.Indexed
import xyz.morphia.annotations.Indexes
import java.io.Serializable
import java.time.LocalDateTime

@Entity(value = "shuns", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("upperNick")))) class Shun : Serializable, Persistent {
    @Id
    var id: ObjectId? = null

    @Indexed(unique = true) var nick: String? = null

    var upperNick: String? = null

    var expiry: LocalDateTime? = null
}
