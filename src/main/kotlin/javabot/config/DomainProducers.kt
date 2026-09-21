package javabot.config

import com.antwerkz.sofia.Sofia
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import dev.morphia.Datastore
import dev.morphia.Morphia
import dev.morphia.config.ManualMorphiaConfig
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import javabot.JavabotConfig
import javabot.model.Factoid
import javabot.model.javadoc.JavadocClass
import net.thauvin.erik.bitly.Bitly
import org.aeonbits.owner.Config.Key
import org.aeonbits.owner.ConfigFactory

@ApplicationScoped
class DomainProducers {

    @Produces
    @ApplicationScoped
    fun mongoClient(): MongoClient = MongoClients.create(MongoClientSettings.builder().build())

    @Produces
    @ApplicationScoped
    fun javabotConfig(): JavabotConfig {
        val config =
            ConfigFactory.create(
                JavabotConfig::class.java,
                loadConfigProperties(),
                System.getProperties(),
                System.getenv(),
            )
        return validate(config)
    }

    @Produces
    @ApplicationScoped
    fun datastore(mongoClient: MongoClient, config: JavabotConfig): Datastore {
        return Morphia.createDatastore(
            mongoClient,
            ManualMorphiaConfig()
                .database(config.databaseName())
                .enablePolymorphicQueries(true)
                .autoImportModels(true)
                .packages(
                    listOf(JavadocClass::class.java.packageName, Factoid::class.java.packageName)
                ),
        )
    }

    @Produces
    @ApplicationScoped
    fun bitly(config: JavabotConfig): Bitly? {
        val bitlyToken = config.bitlyToken()
        return if (bitlyToken != "") Bitly(bitlyToken) else null
    }

    protected open fun loadConfigProperties(): HashMap<Any, Any> = HashMap()

    private fun validate(config: JavabotConfig): JavabotConfig {
        @Suppress("UNCHECKED_CAST")
        val configClass = config.javaClass.interfaces[0] as Class<JavabotConfig>
        val missingKeys = ArrayList<String>()
        for (method in configClass.declaredMethods) {
            try {
                val annotation = method.getDeclaredAnnotation(Key::class.java)
                if (
                    annotation != null &&
                        method.parameterCount == 0 &&
                        method.returnType != Void::class.java &&
                        method.invoke(config) == null
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
}
