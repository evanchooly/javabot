package javabot.model

import javabot.Javabot
import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexed
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.Transient
import java.io.Serializable
import java.time.LocalDateTime
import javax.inject.Inject

@Entity("events")
@Indexes(Index(fields = arrayOf(Field("state"), Field("requestedOn") )))
public open class AdminEvent : Serializable, Persistent {
    public enum class State {
        NEW,
        PROCESSING,
        COMPLETED,
        FAILED
    }

    @Inject
    @Transient
    lateinit val bot: Javabot

    override var id: ObjectId? = null

    lateinit var requestedBy: String

    lateinit var requestedOn: LocalDateTime

    @Indexed(expireAfterSeconds = 60 * 60 * 24)
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

    public fun handle() {
        when (type) {
            EventType.ADD -> add()
            EventType.DELETE -> delete()
            EventType.UPDATE -> update()
            EventType.RELOAD -> reload()
        }
    }

    override fun toString(): String {
        return "AdminEvent{id=%s, requestedOn=%s, completed=%s, state=%s, type=%s}".format(id, requestedOn, completed, state, type)
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
