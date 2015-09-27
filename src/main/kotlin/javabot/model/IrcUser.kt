package javabot.model

import org.mongodb.morphia.annotations.Embedded

@Embedded
public class IrcUser {
    public var nick: String? = null
    public var userName: String? = null
    public var host: String? = null

    public constructor(nick: String, userName: String, host: String) {
        this.host = host
        this.nick = nick
        this.userName = userName
    }

    public constructor(nick: String) {
        this.nick = nick
    }

    override fun toString(): String {
        return nick ?: ""
    }
}
