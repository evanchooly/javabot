package javabot.operations

import javabot.model.JavabotUser

data class TellSubject(val target: JavabotUser, val subject: String)