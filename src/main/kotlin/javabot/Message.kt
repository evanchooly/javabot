package javabot

import org.pircbotx.Channel
import org.pircbotx.User

import java.lang.String.format

public class Message {
    public val channel: Channel?
    public val user: User
    public val value: String
    public val sender: User?
    private val tell: Boolean

    public constructor(dest: Channel, user: User, value: String) {
        channel = dest
        this.user = user
        this.value = value
        sender = null
        tell = false
    }

    public constructor(user: User, value: String) {
        channel = null
        this.user = user
        this.value = value
        sender = null
        tell = false
    }

    public constructor(dest: Channel, user: User, value: String, sender: User) {
        channel = dest
        this.user = user
        this.value = value
        this.sender = sender
        this.tell = true
    }

    public constructor(message: Message, value: String) {
        channel = message.channel
        user = message.user
        this.value = value
        tell = message.isTell()
        sender = null
    }

    public constructor(message: Message, channel: Channel) {
        this.channel = channel
        user = message.user
        this.value = message.value
        tell = message.isTell()
        sender = null
    }

    public fun isTell(): Boolean {
        return tell
    }

    override fun toString(): String {
        return "Message{" + "channel=" + (if (channel != null) channel.name else "private message") + ", user=" + user.nick + ", message='" + value + '\'' + ", tell=" + tell + '}'
    }

    public fun getOriginalUser(): User {
        return sender ?: user
    }

    public fun massageTell(message: String): String {
        return if (tell && !message.contains(user.nick)) format("%s, %s", user.nick, message) else message
    }
}
