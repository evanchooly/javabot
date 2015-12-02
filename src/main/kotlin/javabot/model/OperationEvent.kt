package javabot.model

import org.mongodb.morphia.annotations.Entity

@Entity("events")
public class OperationEvent(requestedBy: String, type: EventType, var operation: String) : AdminEvent(requestedBy, type) {

    override fun add() {
        bot.enableOperation(operation)
    }

    override fun delete() {
        bot.disableOperation(operation)
    }
}
