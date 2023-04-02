package javabot.dao

import com.google.inject.Injector
import com.google.inject.Singleton
import com.mongodb.client.model.IndexOptions
import dev.morphia.Datastore
import javabot.JavabotConfig
import javabot.model.Config
import javabot.model.Logs
import javabot.operations.BotOperation
import org.bson.Document
import org.reflections.Reflections
import org.slf4j.LoggerFactory
import java.lang.reflect.Modifier
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

@Singleton
class ConfigDao @Inject constructor(ds: Datastore, var injector: Injector, var javabotConfig: JavabotConfig) :
        BaseDao<Config>(ds, Config::class.java) {
    fun <T> list(type: Class<T>): List<T> {
        val reflections = Reflections("javabot")

        val classes = reflections.getSubTypesOf(type)

        val list = ArrayList<T>()
        classes.filterNot { Modifier.isAbstract(it.modifiers) }
                .forEach {
                    try {
                        list.add(injector.getInstance(it))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
        return list
    }

    fun get(): Config {
        var config: Config? = ds.find(Config::class.java).first()
        if (config == null) {
            config = create()
        }
        return config
    }

    private fun create(): Config {
        val config = Config()
        config.url = javabotConfig.url()
        config.nick = javabotConfig.nick()
        config.password = javabotConfig.password() // optional
        config.server = javabotConfig.ircHost()
        config.port = javabotConfig.ircPort()
        config.trigger = "~"
        for (operation in list(BotOperation::class.java)) {
            config.operations.add(operation.getName())
        }
        updateHistoryIndex(config.historyLength)
        save(config)
        return config
    }

/*
    override fun save(entity: Persistent) {
        if (entity is Config && entity.id != null) {
            val old = get()
            if (old.historyLength != entity.historyLength) {
                updateHistoryIndex(entity.historyLength)
            }
        }
        super.save(entity)
    }
*/

    private fun updateHistoryIndex(historyLength: Int) {
        val collection = ds.getCollection(Logs::class.java)
        try {
            collection.dropIndex("updated_1")
        } catch (e: Exception) {
            // no such index yet?
        }

        val options = IndexOptions()
        options.expireAfter(TimeUnit.DAYS.toSeconds((historyLength * 31).toLong()), SECONDS)
        options.background(java.lang.Boolean.TRUE)
        try {
            collection.createIndex(Document("updated", 1), options)
        } catch (e: Exception) {
            LOG.error(e.message, e)
        }

    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ConfigDao::class.java)
    }
}
