package javabot.model

import org.bson.types.ObjectId
import xyz.morphia.annotations.Entity
import xyz.morphia.annotations.Id
import java.io.Serializable
import java.util.ArrayList

@Entity("configuration")
class Config : Serializable, Persistent {
    @Id
    var id: ObjectId? = null

    var server: String = "irc.freenode.org"

    var url: String = ""

    var port: Int = 6667

    var historyLength: Int = 12

    var trigger: String = "~"

    var nick: String = ""

    var password: String? = null

    var schemaVersion: Int = 0

    var operations: MutableList<String> = ArrayList()

    var throttleThreshold: Int = 5

    var minimumNickServAge: Int = 14

    constructor() {
    }

    constructor(id: ObjectId, server: String, url: String, port: Int, historyLength: Int, trigger: String, nick: String, password: String,
                operations: MutableList<String>) {
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
