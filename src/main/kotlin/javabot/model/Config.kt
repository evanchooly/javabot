package javabot.model

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id

import java.io.Serializable
import java.util.ArrayList
import java.util.TreeSet

Entity("configuration")
public class Config : Serializable, Persistent {
    Id
    private var id: ObjectId? = null

    public var server: String = "irc.freenode.org"

    public var url: String? = null

    public var port: Int? = 6667

    public var historyLength: Int? = 12

    public var trigger: String = "~"

    public var nick: String? = null

    public var password: String? = null

    private var schemaVersion: Int? = null

    public var operations: List<String> = ArrayList()

    public var throttleThreshold: Int? = 5

    public var minimumNickServAge: Int? = 14

    public constructor() {
    }

    public constructor(id: ObjectId, server: String, url: String, port: Int?,
                       historyLength: Int?, trigger: String, nick: String, password: String,
                       operations: List<String>) {
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

    override fun getId(): ObjectId {
        return id
    }

    override fun setId(configId: ObjectId) {
        id = configId
    }

    public fun getSchemaVersion(): Int? {
        return if (schemaVersion == null) 0 else schemaVersion
    }

    public fun setSchemaVersion(schemaVersion: Int?) {
        this.schemaVersion = schemaVersion
    }

    override fun toString(): String {
        return "Config{server='$server', url='$url', port=$port, historyLength=$historyLength, trigger='$trigger', nick='$nick', schemaVersion=$schemaVersion, throttleThreshold=$throttleThreshold, mininumNickServAge=$minimumNickServAge, operations=$operations}"
    }
}
