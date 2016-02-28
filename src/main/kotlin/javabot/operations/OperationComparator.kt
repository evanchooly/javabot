package javabot.operations

import java.util.Comparator

class OperationComparator : Comparator<BotOperation> {
    override fun compare(botOperation: BotOperation, o: BotOperation): Int {
        var value = Integer.compare(o.getPriority(), botOperation.getPriority())
        if (value == 0) {
            value = botOperation.getName().compareTo(o.getName())
        }
        return value
    }
}