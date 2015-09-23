package javabot.model

import org.mongodb.morphia.annotations.Entity

Entity("events")
public class OperationEvent : AdminEvent() {
    public var operation: String? = null

    override fun add() {
        bot.enableOperation(operation)
    }

    override fun delete() {
        bot.disableOperation(operation)
    }
}
