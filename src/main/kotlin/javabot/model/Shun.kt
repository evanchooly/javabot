package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexed
import org.mongodb.morphia.annotations.Indexes
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
