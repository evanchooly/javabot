package javabot

import org.aeonbits.owner.Config
import org.aeonbits.owner.Config.*
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

    @Key("javabot.openweathermap.token")
    @DefaultValue("")
    fun openweathermapToken(): String

    @Key("javabot.googleapi")
    @DefaultValue("")
    fun googleAPI(): String

    @Key("twitter.consumerKey")
    @DefaultValue("")
    fun twitterConsumerKey(): String

    @Key("twitter.consumerSecret")
    @DefaultValue("")
    fun twitterConsumerSecret(): String

    @Key("twitter.accessToken")
    @DefaultValue("")
    fun twitterAccessToken(): String

    @Key("twitter.accessTokenSecret")
    @DefaultValue("")
    fun twitterAccessTokenSecret(): String
}
