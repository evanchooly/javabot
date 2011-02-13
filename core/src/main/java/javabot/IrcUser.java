package javabot;

public class IrcUser {
    private String nick;
    private String userName;
    private String host;

    public IrcUser(final String nick, final String userName, final String host) {
        this.host = host;
        this.nick = nick;
        this.userName = userName;
    }

    public IrcUser(final String nick) {
        this.nick = nick;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(final String nick) {
        this.nick = nick;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return nick;
    }
}
