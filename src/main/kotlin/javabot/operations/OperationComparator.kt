package javabot.operations

import java.util.Comparator

public class OperationComparator : Comparator<BotOperation> {
    override fun compare(botOperation: BotOperation, o: BotOperation): Int {
        var value = Integer.compare(o.priority, botOperation.priority)
        if (value == 0) {
            value = botOperation.name.compareTo(o.name)
        }
        return value
    }
}