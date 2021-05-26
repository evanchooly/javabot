package javabot

import com.antwerkz.sofia.Sofia
import com.google.inject.AbstractModule
import com.google.inject.Provider
import com.google.inject.Provides
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import dev.morphia.Datastore
import dev.morphia.Morphia
import dev.morphia.mapping.MapperOptions
import javabot.dao.ChannelDao
import javabot.dao.ConfigDao
import javabot.model.Factoid
import javabot.model.javadoc.JavadocClass
import javabot.web.views.ViewFactory
import org.aeonbits.owner.Config.Key
import org.aeonbits.owner.ConfigFactory
import org.pircbotx.Configuration.Builder
import org.pircbotx.PircBotX
import org.pircbotx.cap.SASLCapHandler
import javax.inject.Singleton
import javax.net.ssl.SSLSocketFactory

open class JavabotModule : AbstractModule() {
    open val mongoClient: MongoClient by lazy {
        MongoClients.create(MongoClientSettings.builder().build())
    }

    private var config: JavabotConfig? = null
    lateinit var channelDaoProvider: Provider<ChannelDao>
    lateinit var configDaoProvider: Provider<ConfigDao>
    override fun configure() {
        configDaoProvider = binder().getProvider(ConfigDao::class.java)
        channelDaoProvider = binder().getProvider(ChannelDao::class.java)
        install(FactoryModuleBuilder().build(ViewFactory::class.java))
        install(FactoryModuleBuilder().build(IrcAdapterFactory::class.java))
    }

    open fun client(): MongoClient? {
        return mongoClient
    }

    @Provides
    @Singleton
    fun datastore(): Datastore {
        val databaseName: String = javabotConfig().databaseName()
        val datastore = Morphia.createDatastore(
            client(), databaseName, MapperOptions.builder()
                .enablePolymorphicQueries(true)
                .build()
        )

        datastore.mapper.mapPackageFromClass(JavadocClass::class.java)
        datastore.mapper.mapPackageFromClass(Factoid::class.java)
        datastore.ensureIndexes()

        return datastore
    }

    @Provides
    @Singleton
    protected open fun createIrcBot(): List<PircBotX> {
        val config = configDaoProvider.get().get()
        return config.servers.map { server ->
                val builder = Builder()
                    .setName(config.nick)
                    .setLogin(config.nick)
                    .setAutoNickChange(false)
                    .setCapEnabled(false)
                    .addListener(getBotListener())
                    .addServer(server.server, server.port)
                    .addCapHandler(SASLCapHandler(config.nick, server.password))

                if (server.ssl) {
                    builder.setSocketFactory(SSLSocketFactory.getDefault())
                }

                buildBot(builder)
            }
    }

    open fun buildBot(builder: Builder): PircBotX {
        return PircBotX(builder.buildConfiguration())
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
        @Suppress("UNCHECKED_CAST")
        val configClass = config.javaClass.interfaces[0] as Class<JavabotConfig>
        val methods = configClass.declaredMethods
        val missingKeys = ArrayList<String>()
        for (method in methods) {
            try {
                val annotation = method.getDeclaredAnnotation(Key::class.java)
                if (annotation != null && method.parameterCount == 0 && method.returnType != Void::class.java && method.invoke(
                        config
                    ) == null
                ) {
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
}