package javabot

import org.aeonbits.owner.Config
import org.aeonbits.owner.Config.DefaultValue
import org.aeonbits.owner.Config.Key

import org.aeonbits.owner.Config.LoadPolicy
import org.aeonbits.owner.Config.LoadType
import org.aeonbits.owner.Config.Sources
import org.aeonbits.owner.Preprocessor

@LoadPolicy(LoadType.FIRST)
@Sources("file:javabot.properties")
interface JavabotConfig : Config, Preprocessor {

    override fun process(input: String?): String? {
        return input?.trim()
    }

    @Key("javabot.url")
    fun url(): String

    @Key("javabot.server")
    @DefaultValue("irc.freenode.org")
    fun ircHost(): String

    @Key("javabot.port")
    @DefaultValue("6667")
    fun ircPort(): Int

    @Key("database.name")
    @DefaultValue("javabot")
    fun databaseName(): String

    @Key("database.host")
    @DefaultValue("localhost")
    fun databaseHost(): String

    @Key("database.port")
    @DefaultValue("27017")
    fun databasePort(): Int

    @Key("javabot.nick")
    @DefaultValue("testjavabot")
    fun nick(): String

    @Key("javabot.password")
    fun password(): String?

    @Key("javabot.bitly.token")
    @DefaultValue("")
    fun bitlyToken(): String

    @Key("start.web.app")
    @DefaultValue("true")
    fun startWebApp(): Boolean

    @Key("javadoc.jdk.file")
    fun jdkJavadoc(): String
}
