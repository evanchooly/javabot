package javabot.model

import dev.morphia.annotations.Entity
import dev.morphia.annotations.Id
import org.bson.types.ObjectId
import java.io.Serializable

@Entity("configuration")
class Config : Serializable, Persistent {
    @Id
    var id: ObjectId? = null
    var servers: List<IrcServer> = listOf()
    var historyLength: Int = 12
    var trigger: String = "~"
    var nick: String = ""
    var schemaVersion: Int = 0
    var operations: MutableList<String> = ArrayList()
    var throttleThreshold: Int = 5
    var minimumNickServAge: Int = 14

    constructor() {
    }

    constructor(
        id: ObjectId, servers: List<IrcServer>, historyLength: Int, trigger: String, nick: String, password: String,
        operations: MutableList<String>
    ) {
        this.id = id
        this.historyLength = historyLength
        this.nick = nick
        this.operations = operations
        this.servers = servers
        this.trigger = trigger
    }

    override fun toString(): String {
        return "Config{nick='$nick', trigger='$trigger', historyLength=$historyLength, " +
            "schemaVersion=$schemaVersion, throttleThreshold=$throttleThreshold, mininumNickServAge=$minimumNickServAge, " +
            "servers='$servers', operations=$operations}"
    }
}
