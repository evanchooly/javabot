package javabot.model

import org.mongodb.morphia.annotations.Embedded

@Embedded class IrcUser {
    var nick: String? = null
    var userName: String? = null
    var host: String? = null

    constructor(nick: String, userName: String, host: String) {
        this.host = host
        this.nick = nick
        this.userName = userName
    }

    constructor(nick: String) {
        this.nick = nick
    }

    override fun toString(): String {
        return nick ?: ""
    }
}
