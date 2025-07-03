package javabot.model

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import java.io.Serializable
import org.bson.types.ObjectId

@Entity("configuration")
class Config : Serializable, Persistent {
    @Id var id: ObjectId? = null

    var server = "irc.libera.chat"

    var url = ""

    var port = 6667

    var historyLength = 12

    var trigger = "~"

    var nick: String = ""

    var password: String? = null

    var schemaVersion = 0

    var operations = mutableListOf<String>()

    var throttleThreshold = 5

    var minimumNickServAge = 14

    constructor() {}

    constructor(
        id: ObjectId?,
        server: String,
        url: String,
        port: Int,
        historyLength: Int,
        trigger: String,
        nick: String,
        password: String?,
        operations: MutableList<String>,
    ) {
        this.id = id
        this.historyLength = historyLength
        this.nick = nick
        this.operations = operations
        this.password = password
        this.port = port
        this.server = server
        this.trigger = trigger
        this.url = url
    }

    override fun toString(): String {
        return "Config{server='$server', url='$url', port=$port, historyLength=$historyLength, trigger='$trigger', nick='$nick', " +
            "schemaVersion=$schemaVersion, throttleThreshold=$throttleThreshold, mininumNickServAge=$minimumNickServAge, " +
            "operations=$operations}"
    }
}
