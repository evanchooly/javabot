package javabot.operations;

public final class TellSubject {
    private final String target;
    private final String subject;

    public TellSubject(final String target, final String subject) {
        this.target = target;
        this.subject = subject;
    }

    public String getTarget() {
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
