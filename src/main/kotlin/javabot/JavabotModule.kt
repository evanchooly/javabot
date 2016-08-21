package javabot

import com.antwerkz.sofia.Sofia
import com.google.inject.AbstractModule
import com.google.inject.Provider
import com.google.inject.Provides
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.ServerAddress
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.dao.util.LocalDateTimeConverter
import javabot.javadoc.JavadocClass
import javabot.model.Factoid
import javabot.web.views.ViewFactory
import org.aeonbits.owner.Config.Key
import org.aeonbits.owner.ConfigFactory
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Morphia
import org.pircbotx.Configuration.Builder
import org.pircbotx.PircBotX
import org.pircbotx.cap.SASLCapHandler
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Singleton

open class JavabotModule : AbstractModule() {

    private var mongoClient: MongoClient? = null

    private var morphia: Morphia? = null

    private var config: JavabotConfig? = null

    private val datastore: Datastore by lazy {
        val ds = getMorphia().createDatastore(getMongoClient(), javabotConfig().databaseName())
        ds.ensureIndexes()
        ds
    }
    lateinit var ircAdapterProvider: Provider<out IrcAdapter>
    lateinit var channelDaoProvider: Provider<ChannelDao>
    lateinit var configDaoProvider: Provider<ConfigDao>

    override fun configure() {
        configDaoProvider = binder().getProvider(ConfigDao::class.java)
        channelDaoProvider = binder().getProvider(ChannelDao::class.java)
        ircAdapterProvider = binder().getProvider(IrcAdapter::class.java)
        install(FactoryModuleBuilder()
                .build(ViewFactory::class.java))
    }

    @Provides
    @Singleton
    fun datastore(): Datastore {
        return datastore
    }

    @Provides
    @Singleton
    fun getMorphia(): Morphia {
        if (morphia == null) {
            morphia = Morphia()
            morphia!!.mapPackageFromClass(JavadocClass::class.java)
            morphia!!.mapPackageFromClass(Factoid::class.java)
            morphia!!.mapper.converters.addConverter(LocalDateTimeConverter::class.java)
        }
        return morphia!!
    }

    @Provides
    @Singleton
    fun getMongoClient(): MongoClient {
        if (mongoClient == null) {
            mongoClient = MongoClient(ServerAddress(javabotConfig().databaseHost(), javabotConfig().databasePort()),
                MongoClientOptions.builder().connectTimeout(2000).build())
        }
        return mongoClient!!
    }

    @Provides
    @Singleton
    protected open fun createIrcBot(): PircBotX {
        val config = configDaoProvider.get().get()
        val nick = getBotNick()
        val builder = Builder<PircBotX>()
                .setName(nick)
                .setLogin(nick)
                .setAutoNickChange(false)
                .setCapEnabled(false)
                .addListener(getBotListener())
                .setServerHostname(config.server)
                .setServerPort(config.port)
                .addCapHandler(SASLCapHandler(nick, config.password))

        return buildBot(builder)
    }

    open fun buildBot(builder: Builder<PircBotX>): PircBotX {
        return PircBotX(builder.buildConfiguration())
    }

    open protected fun getBotNick(): String {
        return configDaoProvider.get().get().nick
    }

    @Provides
    @Singleton
    fun javabotConfig(): JavabotConfig {
        if (config == null) {
            config = ConfigFactory.create(JavabotConfig::class.java, loadConfigProperties(), System.getProperties(), System.getenv())
            validate(config!!)
        }
        return config!!
    }

    protected open fun loadConfigProperties(): HashMap<Any, Any> = HashMap()

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
                throw RuntimeException(e.message, e)
            }

        }
        if (!missingKeys.isEmpty()) {
            throw RuntimeException(Sofia.configurationMissingProperties(missingKeys))
        }
        return config
    }

    fun getBotListener(): IrcAdapter {
        return ircAdapterProvider.get()
    }
}
