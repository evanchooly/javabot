package javabot.dao

import com.google.inject.Injector
import com.google.inject.Singleton
import com.mongodb.BasicDBObject
import javabot.JavabotConfig
import javabot.model.Config
import javabot.model.Logs
import javabot.model.Persistent
import javabot.operations.BotOperation
import org.mongodb.morphia.Datastore
import org.reflections.Reflections
import org.slf4j.LoggerFactory
import java.lang.reflect.Modifier
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Singleton
class ConfigDao @Inject constructor(ds: Datastore, var injector: Injector, var javabotConfig: JavabotConfig) :
        BaseDao<Config>(ds, Config::class.java) {

    fun <T> list(type: Class<T>): List<T> {
        val reflections = Reflections("javabot")

        val classes = reflections.getSubTypesOf(type)

        val list = ArrayList<T>()
        for (operation in classes) {
            if (!Modifier.isAbstract(operation.modifiers)) {
                try {
                    list.add(injector.getInstance(operation))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        return list
    }

    fun get(): Config {
        var config: Config? = ds.createQuery(Config::class.java).get()
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

    override fun save(entity: Persistent) {
        if (entity is Config && entity.id != null) {
            val old = get()
            if (old.historyLength != entity.historyLength) {
                updateHistoryIndex(entity.historyLength)
            }
        }
        super.save(entity)
    }

    private fun updateHistoryIndex(historyLength: Int) {
        val collection = ds.getCollection(Logs::class.java)
        try {
            collection.dropIndex("updated_1")
        } catch (e: Exception) {
            // no such index yet?
        }

        val keys = BasicDBObject()
        keys.put("expireAfterSeconds", TimeUnit.DAYS.toSeconds((historyLength * 31).toLong()))
        keys.put("background", java.lang.Boolean.TRUE)
        try {
            collection.createIndex(BasicDBObject("updated", 1), keys)
        } catch (e: Exception) {
            LOG.error(e.message, e)
        }

    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ConfigDao::class.java)
    }
}