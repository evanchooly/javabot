package javabot.model

import dev.morphia.annotations.Entity

@Entity("events")
class OperationEvent() : AdminEvent() {

    lateinit var operation: String

    constructor(requestedBy: String, type: EventType, operation: String): this() {
        this.requestedBy = requestedBy
        this.type = type
        this.operation = operation
    }

    override fun add() {
        bot.enableOperation(operation)
    }

    override fun delete() {
        bot.disableOperation(operation)
    }
}
