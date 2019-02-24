package javabot.model

import org.bson.types.ObjectId
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Field
import xyz.morphia.annotations.Id
import xyz.morphia.annotations.Index
import xyz.morphia.annotations.IndexOptions
import xyz.morphia.annotations.Indexes

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
