package javabot.model

import org.bson.types.ObjectId
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Id

import java.io.Serializable
import java.util.UUID

@Entity(value = "registrations", noClassnameStored = true)
class NickRegistration : Serializable, Persistent {
    @Id
    var id: ObjectId? = null
    var url: String? = null
    var nick: String? = null
    var host: String? = null
    var twitterName: String? = null

    constructor() {
    }

    constructor(sender: JavabotUser, twitterName: String) {
        this.twitterName = twitterName
        url = UUID.randomUUID().toString()
        nick = sender.nick
        host = sender.hostmask
    }
}
