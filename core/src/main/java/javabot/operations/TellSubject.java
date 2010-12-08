package javabot.operations;

import org.schwering.irc.lib.IRCUser;

public final class TellSubject {
    private final IRCUser target;
    private final String subject;

    public TellSubject(final IRCUser target, final String subject) {
        this.target = target;
        this.subject = subject;
    }

    public IRCUser getTarget() {
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
