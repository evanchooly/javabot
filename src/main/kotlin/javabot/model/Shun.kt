package javabot.model

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Field
import dev.morphia.annotations.Id
import dev.morphia.annotations.Index
import dev.morphia.annotations.IndexOptions
import dev.morphia.annotations.Indexed
import dev.morphia.annotations.Indexes
import org.bson.types.ObjectId
import java.io.Serializable
import java.time.LocalDateTime

@Entity(value = "shuns", useDiscriminator = false)
@Indexes(Index(fields = arrayOf(Field("upperNick"))))
class Shun : Serializable, Persistent {
    @Id
    var id: ObjectId? = null

    @Indexed(options = IndexOptions(unique = true))
    var nick: String? = null

    var upperNick: String? = null

    var expiry: LocalDateTime? = null
}
