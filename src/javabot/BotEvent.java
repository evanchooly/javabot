package javabot;

/**
 * @author ricky_clarkson
 */
public class BotEvent {
    private Javabot _bot;
    private String _channel;
    private String _sender;
    private String _login;
    private String _hostname;
    private String _message;

    /**
     * @param bot
     * @param channel
     * @param sender
     * @param login
     * @param hostname
     * @param message
     */
    public BotEvent(Javabot bot, String channel, String sender, String login,
        String hostname, String message) {
        this._bot = bot;
        this._channel = channel;
        this._sender = sender;
        this._login = login;
        this._hostname = hostname;
        this._message = message;
    }

    /**
     * @return
     */
    public Javabot getBot() {
        return _bot;
    }

    /**
     * @return
     */
    public String getChannel() {
        return _channel;
    }

    /**
     * @return
     */
    public String getSender() {
        return _sender;
    }

    /**
     * @return
     */
    public String getLogin() {
        return _login;
    }

    /**
     * @return
     */
    public String getHostname() {
        return _hostname;
    }

    /**
     * @return
     */
    public String getMessage() {
        return _message;
    }
}