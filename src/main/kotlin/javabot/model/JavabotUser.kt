package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.IndexOptions
import org.mongodb.morphia.annotations.Indexes

@Entity("users")
@Indexes(
        Index(fields = arrayOf(Field("nick")), options = IndexOptions(unique = true)),
        Index(fields = arrayOf(Field("hostmask")), options = IndexOptions(unique = true))
)
class JavabotUser {
    @Id
    var id: ObjectId = ObjectId()
    var nick: String = ""
    var userName: String = ""
    var hostmask: String = ""
    var nickServ: NickServInfo? = null

    constructor(nick: String, userName: String, host: String) {
        this.hostmask = host
        this.nick = nick
        this.userName = userName
    }

    constructor(nick: String) {
        this.nick = nick
    }

    override fun toString(): String {
        return nick
    }
}
