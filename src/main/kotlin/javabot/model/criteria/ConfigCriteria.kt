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

public class ConfigCriteria(ds: Datastore) : BaseCriteria<Config>(ds, Config::class.java) {

    public fun historyLength(): TypeSafeFieldEnd<ConfigCriteria, Config, Int> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "historyLength")
    }

    public fun historyLength(value: Int?): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "historyLength").equal(value)
    }

    public fun id(): TypeSafeFieldEnd<ConfigCriteria, Config, ObjectId> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, org.bson.types.ObjectId>(
              this, query, "id")
    }

    public fun id(value: ObjectId): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, org.bson.types.ObjectId>(
              this, query, "id").equal(value)
    }

    public fun minimumNickServAge(): TypeSafeFieldEnd<ConfigCriteria, Config, Int> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "minimumNickServAge")
    }

    public fun minimumNickServAge(value: Int?): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "minimumNickServAge").equal(value)
    }

    public fun nick(): TypeSafeFieldEnd<ConfigCriteria, Config, String> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "nick")
    }

    public fun nick(value: String): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "nick").equal(value)
    }

    public fun operations(): TypeSafeFieldEnd<ConfigCriteria, Config, List<Any>> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, List<Any>>(
              this, query, "operations")
    }

    public fun operations(value: List<String>): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, List<Any>>(
              this, query, "operations").equal(value)
    }

    public fun password(): TypeSafeFieldEnd<ConfigCriteria, Config, String> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "password")
    }

    public fun password(value: String): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "password").equal(value)
    }

    public fun port(): TypeSafeFieldEnd<ConfigCriteria, Config, Int> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "port")
    }

    public fun port(value: Int?): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "port").equal(value)
    }

    public fun schemaVersion(): TypeSafeFieldEnd<ConfigCriteria, Config, Int> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "schemaVersion")
    }

    public fun schemaVersion(value: Int?): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "schemaVersion").equal(value)
    }

    public fun server(): TypeSafeFieldEnd<ConfigCriteria, Config, String> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "server")
    }

    public fun server(value: String): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "server").equal(value)
    }

    public fun throttleThreshold(): TypeSafeFieldEnd<ConfigCriteria, Config, Int> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "throttleThreshold")
    }

    public fun throttleThreshold(value: Int?): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, Int>(
              this, query, "throttleThreshold").equal(value)
    }

    public fun trigger(): TypeSafeFieldEnd<ConfigCriteria, Config, String> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "trigger")
    }

    public fun trigger(value: String): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "trigger").equal(value)
    }

    public fun url(): TypeSafeFieldEnd<ConfigCriteria, Config, String> {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "url")
    }

    public fun url(value: String): Criteria {
        return TypeSafeFieldEnd<ConfigCriteria, Config, String>(
              this, query, "url").equal(value)
    }

    public fun getUpdater(): ConfigUpdater {
        return ConfigUpdater()
    }

    public inner class ConfigUpdater {
        var updateOperations = ds.createUpdateOperations(Config::class.java)

        public fun updateAll(): UpdateResults {
            return ds.update(query(), updateOperations, false)
        }

        public fun updateFirst(): UpdateResults {
            return ds.updateFirst(query(), updateOperations, false)
        }

        public fun updateAll(wc: WriteConcern): UpdateResults {
            return ds.update(query(), updateOperations, false, wc)
        }

        public fun updateFirst(wc: WriteConcern): UpdateResults {
            return ds.updateFirst(query(), updateOperations, false, wc)
        }

        public fun upsert(): UpdateResults {
            return ds.update(query(), updateOperations, true)
        }

        public fun upsert(wc: WriteConcern): UpdateResults {
            return ds.update(query(), updateOperations, true, wc)
        }

        public fun remove(): WriteResult {
            return ds.delete(query())
        }

        public fun remove(wc: WriteConcern): WriteResult {
            return ds.delete(query(), wc)
        }

        public fun historyLength(value: Int?): ConfigUpdater {
            updateOperations.set("historyLength", value)
            return this
        }

        public fun unsetHistoryLength(): ConfigUpdater {
            updateOperations.unset("historyLength")
            return this
        }

        public fun decHistoryLength(): ConfigUpdater {
            updateOperations.dec("historyLength")
            return this
        }

        public fun incHistoryLength(): ConfigUpdater {
            updateOperations.inc("historyLength")
            return this
        }

        public fun incHistoryLength(value: Int?): ConfigUpdater {
            updateOperations.inc("historyLength", value)
            return this
        }

        public fun minimumNickServAge(value: Int?): ConfigUpdater {
            updateOperations.set("minimumNickServAge", value)
            return this
        }

        public fun unsetMinimumNickServAge(): ConfigUpdater {
            updateOperations.unset("minimumNickServAge")
            return this
        }

        public fun decMinimumNickServAge(): ConfigUpdater {
            updateOperations.dec("minimumNickServAge")
            return this
        }

        public fun incMinimumNickServAge(): ConfigUpdater {
            updateOperations.inc("minimumNickServAge")
            return this
        }

        public fun incMinimumNickServAge(value: Int?): ConfigUpdater {
            updateOperations.inc("minimumNickServAge", value)
            return this
        }

        public fun nick(value: String): ConfigUpdater {
            updateOperations.set("nick", value)
            return this
        }

        public fun unsetNick(): ConfigUpdater {
            updateOperations.unset("nick")
            return this
        }

        public fun operations(value: List<String>): ConfigUpdater {
            updateOperations.set("operations", value)
            return this
        }

        public fun unsetOperations(): ConfigUpdater {
            updateOperations.unset("operations")
            return this
        }

        public fun addToOperations(value: List<String>): ConfigUpdater {
            updateOperations.add("operations", value)
            return this
        }

        public fun addToOperations(value: List<String>, addDups: Boolean): ConfigUpdater {
            updateOperations.add("operations", value, addDups)
            return this
        }

        public fun addAllToOperations(values: List<String>,
                                      addDups: Boolean): ConfigUpdater {
            updateOperations.addAll("operations", values, addDups)
            return this
        }

        public fun removeFirstFromOperations(): ConfigUpdater {
            updateOperations.removeFirst("operations")
            return this
        }

        public fun removeLastFromOperations(): ConfigUpdater {
            updateOperations.removeLast("operations")
            return this
        }

        public fun removeFromOperations(value: List<String>): ConfigUpdater {
            updateOperations.removeAll("operations", value)
            return this
        }

        public fun removeAllFromOperations(values: List<String>): ConfigUpdater {
            updateOperations.removeAll("operations", values)
            return this
        }

        public fun password(value: String): ConfigUpdater {
            updateOperations.set("password", value)
            return this
        }

        public fun unsetPassword(): ConfigUpdater {
            updateOperations.unset("password")
            return this
        }

        public fun port(value: Int?): ConfigUpdater {
            updateOperations.set("port", value)
            return this
        }

        public fun unsetPort(): ConfigUpdater {
            updateOperations.unset("port")
            return this
        }

        public fun decPort(): ConfigUpdater {
            updateOperations.dec("port")
            return this
        }

        public fun incPort(): ConfigUpdater {
            updateOperations.inc("port")
            return this
        }

        public fun incPort(value: Int?): ConfigUpdater {
            updateOperations.inc("port", value)
            return this
        }

        public fun schemaVersion(value: Int?): ConfigUpdater {
            updateOperations.set("schemaVersion", value)
            return this
        }

        public fun unsetSchemaVersion(): ConfigUpdater {
            updateOperations.unset("schemaVersion")
            return this
        }

        public fun decSchemaVersion(): ConfigUpdater {
            updateOperations.dec("schemaVersion")
            return this
        }

        public fun incSchemaVersion(): ConfigUpdater {
            updateOperations.inc("schemaVersion")
            return this
        }

        public fun incSchemaVersion(value: Int?): ConfigUpdater {
            updateOperations.inc("schemaVersion", value)
            return this
        }

        public fun server(value: String): ConfigUpdater {
            updateOperations.set("server", value)
            return this
        }

        public fun unsetServer(): ConfigUpdater {
            updateOperations.unset("server")
            return this
        }

        public fun throttleThreshold(value: Int?): ConfigUpdater {
            updateOperations.set("throttleThreshold", value)
            return this
        }

        public fun unsetThrottleThreshold(): ConfigUpdater {
            updateOperations.unset("throttleThreshold")
            return this
        }

        public fun decThrottleThreshold(): ConfigUpdater {
            updateOperations.dec("throttleThreshold")
            return this
        }

        public fun incThrottleThreshold(): ConfigUpdater {
            updateOperations.inc("throttleThreshold")
            return this
        }

        public fun incThrottleThreshold(value: Int?): ConfigUpdater {
            updateOperations.inc("throttleThreshold", value)
            return this
        }

        public fun trigger(value: String): ConfigUpdater {
            updateOperations.set("trigger", value)
            return this
        }

        public fun unsetTrigger(): ConfigUpdater {
            updateOperations.unset("trigger")
            return this
        }

        public fun url(value: String): ConfigUpdater {
            updateOperations.set("url", value)
            return this
        }

        public fun unsetUrl(): ConfigUpdater {
            updateOperations.unset("url")
            return this
        }
    }
}
