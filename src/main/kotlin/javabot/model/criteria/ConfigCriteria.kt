package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.antwerkz.critter.criteria.BaseCriteria
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.model.Config
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.query.Criteria
import org.mongodb.morphia.query.UpdateResults

class ConfigCriteria(ds: Datastore) : BaseCriteria<Config>(ds, Config::class.java) {

    fun historyLength(): TypeSafeFieldEnd<ConfigCriteria, Config, Int> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "historyLength")
    }

    fun historyLength(value: Int?): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "historyLength").equal(value)
    }

    fun id(): TypeSafeFieldEnd<ConfigCriteria, Config, ObjectId> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, org.bson.types.ObjectId>(
              this, query, "id")
    }

    fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    fun minimumNickServAge(): TypeSafeFieldEnd<ConfigCriteria, Config, Int> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "minimumNickServAge")
    }

    fun minimumNickServAge(value: Int?): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "minimumNickServAge").equal(value)
    }

    fun nick(): TypeSafeFieldEnd<ConfigCriteria, Config, String> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "nick")
    }

    fun nick(value: String): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "nick").equal(value)
    }

    fun operations(): TypeSafeFieldEnd<ConfigCriteria, Config, List<Any>> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, List<Any>>(
              this, query, "operations")
    }

    fun operations(value: List<String>): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, List<Any>>(
              this, query, "operations").equal(value)
    }

    fun password(): TypeSafeFieldEnd<ConfigCriteria, Config, String> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "password")
    }

    fun password(value: String): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "password").equal(value)
    }

    fun port(): TypeSafeFieldEnd<ConfigCriteria, Config, Int> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "port")
    }

    fun port(value: Int?): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "port").equal(value)
    }

    fun schemaVersion(): TypeSafeFieldEnd<ConfigCriteria, Config, Int> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "schemaVersion")
    }

    fun schemaVersion(value: Int?): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "schemaVersion").equal(value)
    }

    fun server(): TypeSafeFieldEnd<ConfigCriteria, Config, String> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "server")
    }

    fun server(value: String): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "server").equal(value)
    }

    fun throttleThreshold(): TypeSafeFieldEnd<ConfigCriteria, Config, Int> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "throttleThreshold")
    }

    fun throttleThreshold(value: Int?): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "throttleThreshold").equal(value)
    }

    fun trigger(): TypeSafeFieldEnd<ConfigCriteria, Config, String> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "trigger")
    }

    fun trigger(value: String): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "trigger").equal(value)
    }

    fun url(): TypeSafeFieldEnd<ConfigCriteria, Config, String> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "url")
    }

    fun url(value: String): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "url").equal(value)
    }

    fun getUpdater(): ConfigUpdater {
        return ConfigUpdater()
    }

    inner class ConfigUpdater {
        var updateOperations = ds.createUpdateOperations(Config::class.java)

        fun updateAll(): UpdateResults {
            return ds.update(query(), updateOperations, false)
        }

        fun updateFirst(): UpdateResults {
            return ds.updateFirst(query(), updateOperations, false)
        }

        fun updateAll(wc: WriteConcern): UpdateResults {
            return ds.update(query(), updateOperations, false, wc)
        }

        fun updateFirst(wc: WriteConcern): UpdateResults {
            return ds.updateFirst(query(), updateOperations, false, wc)
        }

        fun upsert(): UpdateResults {
            return ds.update(query(), updateOperations, true)
        }

        fun upsert(wc: WriteConcern): UpdateResults {
            return ds.update(query(), updateOperations, true, wc)
        }

        fun remove(): WriteResult {
            return ds.delete(query())
        }

        fun remove(wc: WriteConcern): WriteResult {
            return ds.delete(query(), wc)
        }

        fun historyLength(value: Int?): ConfigUpdater {
            updateOperations.set("historyLength", value)
            return this
        }

        fun unsetHistoryLength(): ConfigUpdater {
            updateOperations.unset("historyLength")
            return this
        }

        fun decHistoryLength(): ConfigUpdater {
            updateOperations.dec("historyLength")
            return this
        }

        fun incHistoryLength(): ConfigUpdater {
            updateOperations.inc("historyLength")
            return this
        }

        fun incHistoryLength(value: Int?): ConfigUpdater {
            updateOperations.inc("historyLength", value)
            return this
        }

        fun minimumNickServAge(value: Int?): ConfigUpdater {
            updateOperations.set("minimumNickServAge", value)
            return this
        }

        fun unsetMinimumNickServAge(): ConfigUpdater {
            updateOperations.unset("minimumNickServAge")
            return this
        }

        fun decMinimumNickServAge(): ConfigUpdater {
            updateOperations.dec("minimumNickServAge")
            return this
        }

        fun incMinimumNickServAge(): ConfigUpdater {
            updateOperations.inc("minimumNickServAge")
            return this
        }

        fun incMinimumNickServAge(value: Int?): ConfigUpdater {
            updateOperations.inc("minimumNickServAge", value)
            return this
        }

        fun nick(value: String): ConfigUpdater {
            updateOperations.set("nick", value)
            return this
        }

        fun unsetNick(): ConfigUpdater {
            updateOperations.unset("nick")
            return this
        }

        fun operations(value: List<String>): ConfigUpdater {
            updateOperations.set("operations", value)
            return this
        }

        fun unsetOperations(): ConfigUpdater {
            updateOperations.unset("operations")
            return this
        }

        fun addToOperations(value: List<String>): ConfigUpdater {
            updateOperations.add("operations", value)
            return this
        }

        fun addToOperations(value: List<String>, addDups: Boolean): ConfigUpdater {
            updateOperations.add("operations", value, addDups)
            return this
        }

        fun addAllToOperations(values: List<String>,
                                      addDups: Boolean): ConfigUpdater {
            updateOperations.addAll("operations", values, addDups)
            return this
        }

        fun removeFirstFromOperations(): ConfigUpdater {
            updateOperations.removeFirst("operations")
            return this
        }

        fun removeLastFromOperations(): ConfigUpdater {
            updateOperations.removeLast("operations")
            return this
        }

        fun removeFromOperations(value: List<String>): ConfigUpdater {
            updateOperations.removeAll("operations", value)
            return this
        }

        fun removeAllFromOperations(values: List<String>): ConfigUpdater {
            updateOperations.removeAll("operations", values)
            return this
        }

        fun password(value: String): ConfigUpdater {
            updateOperations.set("password", value)
            return this
        }

        fun unsetPassword(): ConfigUpdater {
            updateOperations.unset("password")
            return this
        }

        fun port(value: Int?): ConfigUpdater {
            updateOperations.set("port", value)
            return this
        }

        fun unsetPort(): ConfigUpdater {
            updateOperations.unset("port")
            return this
        }

        fun decPort(): ConfigUpdater {
            updateOperations.dec("port")
            return this
        }

        fun incPort(): ConfigUpdater {
            updateOperations.inc("port")
            return this
        }

        fun incPort(value: Int?): ConfigUpdater {
            updateOperations.inc("port", value)
            return this
        }

        fun schemaVersion(value: Int?): ConfigUpdater {
            updateOperations.set("schemaVersion", value)
            return this
        }

        fun unsetSchemaVersion(): ConfigUpdater {
            updateOperations.unset("schemaVersion")
            return this
        }

        fun decSchemaVersion(): ConfigUpdater {
            updateOperations.dec("schemaVersion")
            return this
        }

        fun incSchemaVersion(): ConfigUpdater {
            updateOperations.inc("schemaVersion")
            return this
        }

        fun incSchemaVersion(value: Int?): ConfigUpdater {
            updateOperations.inc("schemaVersion", value)
            return this
        }

        fun server(value: String): ConfigUpdater {
            updateOperations.set("server", value)
            return this
        }

        fun unsetServer(): ConfigUpdater {
            updateOperations.unset("server")
            return this
        }

        fun throttleThreshold(value: Int?): ConfigUpdater {
            updateOperations.set("throttleThreshold", value)
            return this
        }

        fun unsetThrottleThreshold(): ConfigUpdater {
            updateOperations.unset("throttleThreshold")
            return this
        }

        fun decThrottleThreshold(): ConfigUpdater {
            updateOperations.dec("throttleThreshold")
            return this
        }

        fun incThrottleThreshold(): ConfigUpdater {
            updateOperations.inc("throttleThreshold")
            return this
        }

        fun incThrottleThreshold(value: Int?): ConfigUpdater {
            updateOperations.inc("throttleThreshold", value)
            return this
        }

        fun trigger(value: String): ConfigUpdater {
            updateOperations.set("trigger", value)
            return this
        }

        fun unsetTrigger(): ConfigUpdater {
            updateOperations.unset("trigger")
            return this
        }

        fun url(value: String): ConfigUpdater {
            updateOperations.set("url", value)
            return this
        }

        fun unsetUrl(): ConfigUpdater {
            updateOperations.unset("url")
            return this
        }
    }
}
