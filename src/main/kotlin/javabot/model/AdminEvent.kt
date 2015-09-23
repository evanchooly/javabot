package javabot.model

import javabot.Javabot
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexed
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.Transient

import javax.inject.Inject
import java.io.Serializable
import java.time.LocalDateTime

Entity("events")
Indexes(@Index(fields = { @Field("state"), @Field("requestedOn") }))
public open class AdminEvent : Serializable, Persistent {
    public enum class State {
        NEW,
        PROCESSING,
        COMPLETED,
        FAILED
    }

    Inject
    Transient
    public val bot: Javabot? = null

    Id
    private var id: ObjectId? = null

    public var requestedBy: String? = null

    public var requestedOn: LocalDateTime? = null

    Indexed(expireAfterSeconds = 60 * 60 * 24)
    public var completed: LocalDateTime? = null

    public var state: State = State.NEW

    public var type: EventType? = null

    protected constructor() {
    }

    public constructor(type: EventType, requestedBy: String) {
        this.type = type
        this.requestedBy = requestedBy
        requestedOn = LocalDateTime.now()
    }

    override fun getId(): ObjectId {
        return id
    }

    override fun setId(id: ObjectId) {
        this.id = id
    }

    public fun handle() {
        when (type) {
            EventType.ADD -> add()
            EventType.DELETE -> delete()
            EventType.UPDATE -> update()
            EventType.RELOAD -> reload()
        }
    }

    override fun toString(): String {
        return String.format("AdminEvent{id=%s, requestedOn=%s, completed=%s, state=%s, type=%s}", id, requestedOn, completed, state, type)
    }

    public open fun add() {
    }

    public open fun delete() {
    }

    public open fun update() {
    }

    public open fun reload() {
    }
}
