package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id
import org.pircbotx.User

import java.io.Serializable
import java.util.UUID

@Entity(value = "registrations", noClassnameStored = true)
public class NickRegistration : Serializable, Persistent {
    override var id: ObjectId? = null
    public var url: String? = null
    public var nick: String? = null
    public var host: String? = null
    public var twitterName: String? = null

    public constructor() {
    }

    public constructor(sender: User, twitterName: String) {
        this.twitterName = twitterName
        url = UUID.randomUUID().toString()
        nick = sender.nick
        host = sender.hostmask
    }
}
