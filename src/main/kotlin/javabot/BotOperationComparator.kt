package javabot

import java.util.Comparator

import javabot.operations.BotOperation

public class BotOperationComparator : Comparator<BotOperation> {
    override fun compare(o1: BotOperation, o2: BotOperation): Int {
        if ("GetFactoid" == o1.getName()) {
            return 1
        }
        if ("GetFactoid" == o2.getName()) {
            return -1
        }
        return o1.getName().compareTo(o2.getName())
    }
}
