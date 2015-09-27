package javabot.operations

import org.pircbotx.User

public class TellSubject(public val target: User, public val subject: String) {

    override fun toString(): String {
        return "TellSubject{subject='%s', target='%s'}".format(subject, target.nick)
    }
}
