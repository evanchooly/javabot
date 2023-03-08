package javabot

import com.antwerkz.sofia.Sofia
import com.google.common.base.CharMatcher
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
import javax.inject.Singleton
import javax.net.ssl.SSLSocketFactory
import org.aeonbits.owner.Config.Key
import org.aeonbits.owner.ConfigFactory
import org.pircbotx.Configuration.Builder
import org.pircbotx.PircBotX
import org.pircbotx.cap.SASLCapHandler

open class JavabotModule : AbstractModule() {
    open val mongoClient: MongoClient by lazy {
        MongoClients.create(MongoClientSettings.builder().build())
    }

    private var config: JavabotConfig? = null
    lateinit var ircAdapterProvider: Provider<out IrcAdapter>
    lateinit var channelDaoProvider: Provider<ChannelDao>
    lateinit var configDaoProvider: Provider<ConfigDao>
    override fun configure() {
        configDaoProvider = binder().getProvider(ConfigDao::class.java)
        channelDaoProvider = binder().getProvider(ChannelDao::class.java)
        ircAdapterProvider = binder().getProvider(IrcAdapter::class.java)
        install(
            FactoryModuleBuilder()
                .build(ViewFactory::class.java)
        )
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
    protected open fun createIrcBot(): PircBotX {
        val config = configDaoProvider.get().get()
        val nick = getBotNick()
        val builder = Builder()
            .setName(nick)
            .setLogin(nick)
            .setAutoNickChange(false)
            .setCapEnabled(false)
            .addListener(getBotListener())
            .addServer(config.server, config.port)
            .addCapHandler(SASLCapHandler(nick, config.password))
            .setSocketFactory(SSLSocketFactory.getDefault())

        return buildBot(builder)
    }

    open fun buildBot(builder: Builder): PircBotX {
        return PircBotX(builder.buildConfiguration())
/*
        return object: PircBotX(builder.buildConfiguration()) {
            override fun sendRawLineToServer(line: String) {

                var line = line
                if (line.length > configuration.maxLineLength - 2) line = line.substring(0, configuration.maxLineLength - 2)
                println("raw line: $line")
                outputWriter.write(line + "\r\n")
                outputWriter.flush()
                val lineParts = tokenizeLine(line)
                getConfiguration().getListenerManager<ListenerManager>().onEvent(OutputEvent(this, line, lineParts))
            }
        }
*/
    }

    protected open fun getBotNick(): String {
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
        if (missingKeys.isNotEmpty()) {
            throw RuntimeException(Sofia.configurationMissingProperties(missingKeys))
        }
        return config
    }

    fun getBotListener(): IrcAdapter {
        return ircAdapterProvider.get()
    }
}

fun tokenizeLine(input: String?): MutableList<String> {
    val stringParts: MutableList<String> = java.util.ArrayList()
    if (input.isNullOrEmpty()) return stringParts
    //Heavily optimized string split by space with all characters after :
    //added as a single entry. Under benchmarks, this is faster than
    //StringTokenizer, String.split, toCharArray, and charAt
    val trimmedInput: String = CharMatcher.whitespace().trimFrom(input)
    var pos = 0
    var end: Int
    while (trimmedInput.indexOf(' ', pos).also { end = it } >= 0) {
        stringParts.add(trimmedInput.substring(pos, end))
        pos = end + 1
        if (trimmedInput[pos] == ':') {
            stringParts.add(trimmedInput.substring(pos + 1))
            return stringParts
        }
    }
    //No more spaces, add last part of line
    stringParts.add(trimmedInput.substring(pos))
    return stringParts
}
