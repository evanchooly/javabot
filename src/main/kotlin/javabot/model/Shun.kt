package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexed
import org.mongodb.morphia.annotations.Indexes
import java.io.Serializable
import java.time.LocalDateTime

@Entity(value = "shuns", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("upperNick"))))
public class Shun : Serializable, Persistent {
    override var id: ObjectId? = null

    @Indexed(unique = true)
    public var nick: String? = null

    public var upperNick: String? = null

    public var expiry: LocalDateTime? = null
}
