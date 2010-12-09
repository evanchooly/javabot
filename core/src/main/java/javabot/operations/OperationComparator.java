package javabot.operations;

import java.util.Comparator;

public class OperationComparator implements Comparator<BotOperation> {
    @Override
    public int compare(final BotOperation botOperation, final BotOperation o) {
        return botOperation.getName().compareTo(o.getName());
    }
}