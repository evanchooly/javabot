package javabot.model

import org.bson.types.ObjectId
import xyz.morphia.annotations.AlsoLoad
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Field
import xyz.morphia.annotations.Id
import xyz.morphia.annotations.Index
import xyz.morphia.annotations.IndexOptions
import xyz.morphia.annotations.Indexes
import java.io.Serializable
import java.time.LocalDateTime

@Entity(value = "admins", noClassnameStored = true)
@Indexes(Index(fields = arrayOf(Field("emailAddress")), options = IndexOptions(unique = true)),
        Index(fields = arrayOf(Field("ircName"), Field("hostName"))))
class Admin : Serializable, Persistent {
    @Id
    var id: ObjectId? = null

    var hostName: String = ""

    var ircName: String = ""

    @AlsoLoad("userName")
    var emailAddress: String

    var botOwner: Boolean = false

    var addedBy: String? = null

    var updated: LocalDateTime = LocalDateTime.now()

    constructor() {
        emailAddress = ""
    }

    constructor(emailAddress: String) : this() {
        this.emailAddress = emailAddress
        this.botOwner = true
    }

    constructor(ircName: String, userName: String, hostName: String, owner: Boolean) : this() {
        this.ircName = ircName
        this.emailAddress = userName
        this.hostName = hostName
        this.botOwner = owner
    }

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
