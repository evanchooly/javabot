package javabot;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({"file:javabot.properties",
          "file:javabot-sample.properties",
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
    @DefaultValue("testjavabot")
    String nick();

    @Key("javabot.password")
    String password();

    @Key("javabot.bitly.token")
    @DefaultValue("")
    String bitlyToken();

    @Key("start.web.app")
    @DefaultValue("false")
    Boolean startWebApp();
}
