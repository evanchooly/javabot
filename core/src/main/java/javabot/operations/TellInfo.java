package javabot.operations;

import javabot.operations.throttle.ThrottleItem;
import org.schwering.irc.lib.IRCUser;

public final class TellInfo implements ThrottleItem<TellInfo> {
    private final IRCUser user;
    private final String msg;

    public TellInfo(final IRCUser user, final String msg) {
        this.user = user;
        this.msg = msg;
    }

    public boolean matches(final TellInfo ti) {
        return user.equals(ti.user) && msg.equals(ti.msg);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TellInfo");
        sb.append("{msg='").append(msg).append('\'');
        sb.append(", nick='").append(user).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
