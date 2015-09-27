package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.AlsoLoad
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.IndexOptions
import org.mongodb.morphia.annotations.Indexes
import java.io.Serializable
import java.time.LocalDateTime

@Entity(value = "admins", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("emailAddress")), options = IndexOptions(unique = true)),
      Index(fields = arrayOf(Field("ircName"), Field("hostName"))))
public class Admin : Serializable, Persistent {
    override var id: ObjectId? = null

    public var botOwner: Boolean = false

    public var hostName: String? = null

    public var ircName: String? = null

    @AlsoLoad("userName")
    lateinit var emailAddress: String

    public var addedBy: String? = null

    public var updated: LocalDateTime = LocalDateTime.now()

    override fun toString(): String {
        val sb = StringBuilder("Admin{")
        sb.append("id=").append(id)
        sb.append(", botOwner=").append(botOwner)
        sb.append(", hostName='").append(hostName).append('\'')
        sb.append(", emailAddress='").append(emailAddress).append('\'')
        sb.append(", ircName='").append(ircName).append('\'')
        sb.append(", addedBy='").append(addedBy).append('\'')
        sb.append(", updated=").append(updated)
        sb.append('}')
        return sb.toString()
    }
}