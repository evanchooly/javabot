package javabot

import org.aeonbits.owner.Config
import org.aeonbits.owner.Config.DefaultValue
import org.aeonbits.owner.Config.Key
import org.aeonbits.owner.Config.LoadPolicy
import org.aeonbits.owner.Config.LoadType
import org.aeonbits.owner.Config.Sources

@LoadPolicy(LoadType.MERGE)
@Sources("file:javabot.properties", "file:javabot-sample.properties")
public interface JavabotConfig : Config {
    @Key("javabot.server")
    @DefaultValue("irc.freenode.org")
    public fun ircHost(): String

    @Key("javabot.port")
    @DefaultValue("6667")
    public fun ircPort(): Int

    @Key("database.name")
    @DefaultValue("javabot")
    public fun databaseName(): String

    @Key("database.host")
    @DefaultValue("localhost")
    public fun databaseHost(): String

    @Key("database.port")
    @DefaultValue("27017")
    public fun databasePort(): Int

    @Key("javabot.nick")
    @DefaultValue("testjavabot")
    public fun nick(): String

    @Key("javabot.password")
    public fun password(): String

    @Key("javabot.bitly.token")
    @DefaultValue("")
    public fun bitlyToken(): String

    @Key("start.web.app")
    @DefaultValue("true")
    public fun startWebApp(): Boolean?
}
