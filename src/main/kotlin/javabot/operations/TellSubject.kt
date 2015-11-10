package javabot.operations

import org.pircbotx.User

data class TellSubject(public val target: User, public val subject: String)
