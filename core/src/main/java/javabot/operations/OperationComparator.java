package javabot.operations;

public class OperationComparator implements Comparable<BotOperation> {
    private final BotOperation botOperation;

    public OperationComparator(BotOperation botOperation) {
        this.botOperation = botOperation;
    }

    @Override
    public int compareTo(final BotOperation o) {
        if (o.getPriority() != botOperation.getPriority()) {
            return Integer.valueOf(botOperation.getPriority()).compareTo(o.getPriority());
        }
        if (botOperation.isStandardOperation() != o.isStandardOperation()) {
            return Boolean.valueOf(botOperation.isStandardOperation()).compareTo(o.isStandardOperation());
        }
        return botOperation.getName().compareTo(o.getName());
    }
}