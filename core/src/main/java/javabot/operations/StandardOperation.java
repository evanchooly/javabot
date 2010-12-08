package javabot.operations;

public abstract class StandardOperation extends BotOperation {
    @Override
    public final boolean isStandardOperation() {
        return true;
    }

    @Override
    public final boolean isEnabled() {
        return true;
    }
}
