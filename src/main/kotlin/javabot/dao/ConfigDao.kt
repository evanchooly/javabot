package javabot.dao

import com.google.inject.Injector
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import javabot.JavabotConfig
import javabot.model.Config
import javabot.model.Logs
import javabot.model.Persistent
import javabot.operations.BotOperation
import org.reflections.Reflections
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject
import java.lang.reflect.Modifier
import java.util.ArrayList
import java.util.concurrent.TimeUnit

public class ConfigDao protected constructor() : BaseDao<Config>(Config::class.java) {

    Inject
    private val injector: Injector? = null

    Inject
    private val javabotConfig: JavabotConfig? = null

    public fun <T> list(type: Class<T>): List<T> {
        val reflections = Reflections("javabot")

        val classes = reflections.getSubTypesOf(type)

        val list = ArrayList<T>()
        for (operation in classes) {
            if (!Modifier.isAbstract(operation.modifiers)) {
                try {
                    list.add(injector!!.getInstance<out T>(operation))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        return list
    }

    public fun get(): Config? {
        var config: Config? = ds.createQuery(Config::class.java).get()
        if (config == null) {
            config = create()
        }
        return config
    }

    public fun create(): Config {
        val config = Config()
        config.nick = javabotConfig!!.nick()
        config.password = javabotConfig.password() // optional
        config.server = javabotConfig.ircHost()
        config.port = javabotConfig.ircPort()
        config.trigger = "~"
        for (operation in list(BotOperation::class.java)) {
            config.operations.add(operation.name)
        }
        updateHistoryIndex(config.historyLength)
        save(config)
        return config
    }

    override fun save(`object`: Persistent) {
        if (`object` is Config && `object`.id != null) {
            val old = get()
            if (old != null) {
                if (old.historyLength != `object`.historyLength) {
                    updateHistoryIndex(`object`.historyLength)
                }
            }
        }
        super.save(`object`)
    }

    private fun updateHistoryIndex(historyLength: Int?) {
        val collection = ds.getCollection(Logs::class.java)
        try {
            collection.dropIndex("updated_1")
        } catch (e: Exception) {
            // no such index yet?
        }

        val keys = BasicDBObject()
        keys.put("expireAfterSeconds", TimeUnit.DAYS.toSeconds((historyLength!! * 31).toLong()))
        keys.put("background", java.lang.Boolean.TRUE)
        try {
            collection.createIndex(BasicDBObject("updated", 1), keys)
        } catch (e: Exception) {
            LOG.error(e.getMessage(), e)
        }

    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ConfigDao::class.java)
    }
}