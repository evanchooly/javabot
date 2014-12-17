package javabot.operations;

import java.util.Comparator;

public class OperationComparator implements Comparator<BotOperation> {
    @Override
    public int compare(final BotOperation botOperation, final BotOperation o) {
        int value=Integer.compare(o.getPriority(), botOperation.getPriority());
        if(value==0) {
            value = botOperation.getName().compareTo(o.getName());
        }
        return value;
    }
}