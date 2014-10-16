package javabot;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({"classpath:javabot.properties",
          "file:javabot.properties"
          })
public interface JavabotConfig extends Config {
    @Key("javabot.server")
    @DefaultValue("irc.freenode.org")
    String ircHost();

    @Key("javabot.port")
    @DefaultValue("6667")
    int ircPort();

    @Key("database.name")
    @DefaultValue("javabot")
    String databaseName();

    @Key("database.host")
    @DefaultValue("localhost")
    String databaseHost();

    @Key("database.port")
    @DefaultValue("27017")
    int databasePort();

    @Key("javabot.nick")
    String nick();

    @Key("javabot.password")
    String password();

    @Key("javabot.admin.nick")
    String adminNick();

    @Key("javabot.admin.hostmask")
    String adminHostMask();

    @Key("javabot.bitly.token")
    String bitlyToken();

    @Key("javabot.bitly.clientId")
    String bitlyClientId();

    @Key("javabot.bitly.secret")
    String bitlySecret();
}
