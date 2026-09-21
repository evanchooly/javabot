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
        return validateJavabotConfig(config)
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

    // @Dependent (the CDI default pseudo-scope, so no scope annotation here), not
    // @ApplicationScoped: a normal-scoped (e.g. @ApplicationScoped) producer method is
    // forbidden by the CDI spec from returning null (IllegalProductException) -- caught
    // empirically by ShortenerTest once UrlCacheService's `@Inject bitly: Bitly?` field started
    // resolving through this producer for the first time (Task 15/18's combined report). Bitly
    // is a cheap stateless wrapper, so a new instance per injection point is harmless.
    @Produces
    fun bitly(config: JavabotConfig): Bitly? {
        val bitlyToken = config.bitlyToken()
        return if (bitlyToken != "") Bitly(bitlyToken) else null
    }

    protected open fun loadConfigProperties(): HashMap<Any, Any> = HashMap()
}

/**
 * Shared by [DomainProducers.javabotConfig] and [TestDomainProducers.javabotConfig] so both the
 * production and `%test` config producers enforce the same "no missing @Key-annotated properties"
 * invariant.
 */
fun validateJavabotConfig(config: JavabotConfig): JavabotConfig {
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
