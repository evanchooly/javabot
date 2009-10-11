package javabot.operations;

import javabot.operations.throttle.ThrottleItem;

public final class TellInfo implements ThrottleItem<TellInfo> {
    private final String nick;
    private final String msg;

    public TellInfo(final String nick, final String msg) {
        this.nick = nick;
        this.msg = msg;
    }

    public boolean matches(final TellInfo ti) {
        return nick.equals(ti.nick) && msg.equals(ti.msg);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TellInfo");
        sb.append("{msg='").append(msg).append('\'');
        sb.append(", nick='").append(nick).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
