package javabot.model

import org.mongodb.morphia.annotations.Embedded

@Embedded
class JavabotUser {
    var nick: String = ""
    var userName: String = ""
    var hostmask: String = ""

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
