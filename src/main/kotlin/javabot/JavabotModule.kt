package javabot

import com.antwerkz.sofia.Sofia
import com.google.inject.AbstractModule
import com.google.inject.Provider
import com.google.inject.Provides
import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.ServerAddress
import com.mongodb.WriteConcern
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.util.LocalDateTimeConverter
import javabot.javadoc.JavadocClass
import javabot.model.Factoid
import net.swisstech.bitly.BitlyClient
import org.aeonbits.owner.Config.Key
import org.aeonbits.owner.ConfigFactory
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Morphia
import org.pircbotx.Configuration.Builder
import org.pircbotx.PircBotX
import org.pircbotx.cap.SASLCapHandler
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Singleton

open class JavabotModule : AbstractModule() {

    private var mongoClient: MongoClient? = null

    private var morphia: Morphia? = null

    private var datastore: Datastore? = null
    private var botListenerProvider: Provider<BotListener>? = null

    lateinit var channelDaoProvider: Provider<ChannelDao>
    lateinit var configDaoProvider: Provider<ConfigDao>

    override fun configure() {
        configDaoProvider = binder().getProvider(ConfigDao::class.java)
        channelDaoProvider = binder().getProvider(ChannelDao::class.java)
        botListenerProvider = binder().getProvider(BotListener::class.java)
    }

    @Provides
    @Singleton
    @Throws(IOException::class)
    public fun datastore(): Datastore {
        if (datastore == null) {
            datastore = getMorphia().createDatastore(getMongoClient(), javabotConfig().databaseName())
            datastore!!.defaultWriteConcern = WriteConcern.SAFE
            try {
                datastore!!.ensureIndexes()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return datastore!!
    }

    @Provides
    @Singleton
    public fun getMorphia(): Morphia {
        if (morphia == null) {
            morphia = Morphia()
            morphia!!.mapPackage(JavadocClass::class.java.`package`.name)
            morphia!!.mapPackage(Factoid::class.java.`package`.name)
            morphia!!.mapper.converters.addConverter(LocalDateTimeConverter::class.java)
        }
        return morphia!!
    }

    @Provides
    @Singleton
    @Throws(IOException::class)
    public fun getMongoClient(): MongoClient {
        if (mongoClient == null) {
            try {
                mongoClient = MongoClient(ServerAddress(javabotConfig().databaseHost(), javabotConfig().databasePort()),
                      MongoClientOptions.builder().connectTimeout(2000).build())
            } catch (e: RuntimeException) {
                e.printStackTrace()
                throw RuntimeException(e.getMessage(), e)
            }
        }
        return mongoClient!!
    }

    @Provides
    @Singleton
    @Throws(IOException::class)
    public fun bitlyClient(): BitlyClient? {
        return if (javabotConfig().bitlyToken() != "") BitlyClient(javabotConfig().bitlyToken()) else null
    }

    @Provides
    @Singleton
    protected open fun createIrcBot(): PircBotX {
        val config = configDaoProvider.get().get()
        val nick = config.nick
        val builder = Builder<PircBotX>().setName(nick).setLogin(nick).setAutoNickChange(false).setCapEnabled(false).addListener(
              getBotListener()).setServerHostname(config.server).setServerPort(config.port).addCapHandler(
              SASLCapHandler(nick, config.password))
        /*
        for (Channel channel : channelDaoProvider.get().getChannels()) {
            LOG.info("Adding {} as an autojoined channel", channel.getName());
            if (channel.getKey() == null) {
                builder.addAutoJoinChannel(channel.getName());
            } else {
                builder.addAutoJoinChannel(channel.getName(), channel.getKey());
            }
        }
*/

        return PircBotX(builder.buildConfiguration())
    }

    @Provides
    @Singleton
    @Throws(IOException::class)
    public open fun javabotConfig(): JavabotConfig {
        return validate(ConfigFactory.create(JavabotConfig::class.java, HashMap<Any, Any>(), System.getProperties(), System.getenv()))
    }

    @SuppressWarnings("unchecked")
    protected fun validate(config: JavabotConfig): JavabotConfig {
        val configClass = config.javaClass.interfaces[0] as Class<JavabotConfig>
        val methods = configClass.declaredMethods
        val missingKeys = ArrayList<String>()
        for (method in methods) {
            try {
                val annotation = method.getDeclaredAnnotation(Key::class.java)
                if (annotation != null && method.parameterCount == 0 && method.returnType != Void::class.java && method.invoke(
                      config) == null) {
                    missingKeys.add(annotation.value)
                }
            } catch (e: ReflectiveOperationException) {
                throw RuntimeException(e.getMessage(), e)
            }

        }
        if (!missingKeys.isEmpty()) {
            throw RuntimeException(Sofia.configurationMissingProperties(missingKeys))
        }
        return config
    }

    public fun getBotListener(): BotListener {
        return botListenerProvider!!.get()
    }
}
