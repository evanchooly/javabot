package javabot.model

import jakarta.inject.Inject
import java.io.Serializable
import java.time.LocalDateTime
import javabot.Javabot
import org.bson.types.ObjectId

open class AdminEvent : Serializable {

    @Inject lateinit var bot: Javabot

    var id: ObjectId = ObjectId()

    @Volatile var completed: LocalDateTime? = null

    @Volatile var state: State = State.NEW

    lateinit var requestedBy: String
    lateinit var requestedOn: LocalDateTime
    lateinit var type: EventType

    constructor()

    constructor(
        requestedBy: String,
        type: EventType,
        requestedOn: LocalDateTime = LocalDateTime.now(),
    ) : this() {
        this.requestedBy = requestedBy
        this.requestedOn = requestedOn
        this.type = type
    }

    fun handle() {
        when (type) {
            EventType.ADD -> add()
            EventType.DELETE -> delete()
            EventType.UPDATE -> update()
            EventType.RELOAD -> reload()
        }
    }

    override fun toString(): String {
        return "AdminEvent{id=${id}, requestedOn=${requestedOn}, completed=${completed}, state=${state}, type=${type}}"
    }

    open fun add() {}

    open fun delete() {}

    open fun update() {}

    open fun reload() {}
}

enum class State {
    NEW,
    PROCESSING,
    COMPLETED,
    FAILED,
}
