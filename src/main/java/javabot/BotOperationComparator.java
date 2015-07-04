package javabot;

import java.util.Comparator;

import javabot.operations.BotOperation;

public class BotOperationComparator implements Comparator<BotOperation> {
    @Override
    public int compare(final BotOperation o1, final BotOperation o2) {
        if ("GetFactoid".equals(o1.getName())) {
            return 1;
        }
        if ("GetFactoid".equals(o2.getName())) {
            return -1;
        }
        return o1.getName().compareTo(o2.getName());
    }
}
