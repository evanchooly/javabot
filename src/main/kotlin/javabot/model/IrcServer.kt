package javabot.model

import dev.morphia.annotations.Embedded

@Embedded
class IrcServer(
    var server: String = "irc.freenode.org",
    var url: String = "",
    var port: Int = 6667,
    var password: String? = null,
    var ssl: Boolean = false
)