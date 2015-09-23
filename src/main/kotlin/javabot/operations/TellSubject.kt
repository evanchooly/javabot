package javabot.operations

import org.pircbotx.User

public class TellSubject(public val target: User, public val subject: String) {

    override fun toString(): String {
        return String.format("TellSubject{subject='%s', target='%s'}", subject, target.nick)
    }
}
