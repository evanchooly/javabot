package javabot.operations;

import org.pircbotx.User;

public final class TellSubject {
    private final User target;
    private final String subject;

    public TellSubject(final User target, final String subject) {
        this.target = target;
        this.subject = subject;
    }

    public User getTarget() {
        return target;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return String.format("TellSubject{subject='%s', target='%s'}", subject, target.getNick());
    }
}
