package javabot.operations;

import javabot.model.IrcUser;

public final class TellSubject {
    private final IrcUser target;
    private final String subject;

    public TellSubject(final IrcUser target, final String subject) {
        this.target = target;
        this.subject = subject;
    }

    public IrcUser getTarget() {
        return target;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TellSubject");
        sb.append("{subject='").append(subject).append('\'');
        sb.append(", target='").append(target).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
