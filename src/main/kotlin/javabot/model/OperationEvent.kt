package javabot.model

import dev.morphia.annotations.Entity

@Entity("events") class OperationEvent(requestedBy: String, type: EventType, var operation: String) : AdminEvent(requestedBy, type) {

    override fun add() {
        bot.enableOperation(operation)
    }

    override fun delete() {
        bot.disableOperation(operation)
    }
}
