package javabot.model.criteria

import com.antwerkz.critter.TypeSafeFieldEnd
import com.mongodb.WriteConcern
import com.mongodb.WriteResult
import javabot.dao.weather.Weather
import kotlin.String
import kotlin.Suppress
import xyz.morphia.Datastore
import xyz.morphia.query.Criteria
import xyz.morphia.query.CriteriaContainer
import xyz.morphia.query.Query
import xyz.morphia.query.UpdateOperations
import xyz.morphia.query.UpdateResults

@Suppress("UNCHECKED_CAST")
class WeatherCriteria(
    private val ds: Datastore,
    private val query: Query<*>,
    fieldName: String?
) {
    private val prefix: String = fieldName?.let { fieldName + "." } ?: "" 

    constructor(ds: Datastore, fieldName: String? = null) : this(ds, ds.find(Weather::class.java), fieldName)

    fun query(): Query<Weather> = query as Query<Weather>
    fun delete(wc: WriteConcern = ds.defaultWriteConcern): WriteResult = ds.delete(query, wc)
    fun or(vararg criteria: Criteria): CriteriaContainer = query.or(*criteria)
    fun and(vararg criteria: Criteria): CriteriaContainer = query.and(*criteria)
    fun city(): TypeSafeFieldEnd<WeatherCriteria, String?> = TypeSafeFieldEnd(this, query, city)
    fun city(__newValue: String?): Criteria = TypeSafeFieldEnd<WeatherCriteria, String?>(this, query, city).equal(__newValue)
    fun condition(): TypeSafeFieldEnd<WeatherCriteria, String?> = TypeSafeFieldEnd(this, query, condition)
    fun condition(__newValue: String?): Criteria = TypeSafeFieldEnd<WeatherCriteria, String?>(this, query, condition).equal(__newValue)
    fun tempCelsius(): TypeSafeFieldEnd<WeatherCriteria, Double?> = TypeSafeFieldEnd(this, query, tempCelsius)
    fun tempCelsius(__newValue: Double?): Criteria = TypeSafeFieldEnd<WeatherCriteria, Double?>(this, query, tempCelsius).equal(__newValue)
    fun humidity(): TypeSafeFieldEnd<WeatherCriteria, String?> = TypeSafeFieldEnd(this, query, humidity)
    fun humidity(__newValue: String?): Criteria = TypeSafeFieldEnd<WeatherCriteria, String?>(this, query, humidity).equal(__newValue)
    fun wind(): TypeSafeFieldEnd<WeatherCriteria, String?> = TypeSafeFieldEnd(this, query, wind)
    fun wind(__newValue: String?): Criteria = TypeSafeFieldEnd<WeatherCriteria, String?>(this, query, wind).equal(__newValue)
    fun windChill(): TypeSafeFieldEnd<WeatherCriteria, String?> = TypeSafeFieldEnd(this, query, windChill)
    fun windChill(__newValue: String?): Criteria = TypeSafeFieldEnd<WeatherCriteria, String?>(this, query, windChill).equal(__newValue)
    fun localTime(): TypeSafeFieldEnd<WeatherCriteria, String?> = TypeSafeFieldEnd(this, query, localTime)
    fun localTime(__newValue: String?): Criteria = TypeSafeFieldEnd<WeatherCriteria, String?>(this, query, localTime).equal(__newValue)
    fun updater(): WeatherUpdater = WeatherUpdater(ds, query, ds.createUpdateOperations(Weather::class.java),
    if(prefix.isNotEmpty()) prefix else null)
    companion object {
        val city: String = "city"

        val condition: String = "condition"

        val tempCelsius: String = "tempCelsius"

        val humidity: String = "humidity"

        val wind: String = "wind"

        val windChill: String = "windChill"

        val localTime: String = "localTime"
    }

    class WeatherUpdater(
        private val ds: Datastore,
        private val query: Query<*>,
        private val updateOperations: UpdateOperations<*>,
        fieldName: String?
    ) {
        private val prefix: String = fieldName?.let { fieldName + "." } ?: "" 

        fun updateAll(wc: WriteConcern = ds.defaultWriteConcern): UpdateResults = ds.update(query as Query<Any>, updateOperations as UpdateOperations<Any>, false, wc)
        fun updateFirst(wc: WriteConcern = ds.defaultWriteConcern): UpdateResults = ds.updateFirst(query as Query<Any>, updateOperations as UpdateOperations<Any>, false, wc)
        fun upsert(wc: WriteConcern = ds.defaultWriteConcern): UpdateResults = ds.update(query as Query<Any>, updateOperations as UpdateOperations<Any>, true, wc)
        fun remove(wc: WriteConcern = ds.defaultWriteConcern): WriteResult = ds.delete(query, wc)
        fun city(__newValue: String?): WeatherUpdater {
                                        updateOperations.set(prefix + city, __newValue)
                                        return this}

        fun unsetCity(): WeatherUpdater {

                                        updateOperations.unset(prefix + city)
                                        return this
                                        }

        fun removeFirstFromCity(): WeatherUpdater {
            updateOperations.removeFirst(prefix + city)
            return this }

        fun removeLastFromCity(): WeatherUpdater {
            updateOperations.removeLast(prefix + city)
            return this }

        fun removeFromCity(__newValue: String?): WeatherUpdater {
            updateOperations.removeAll(prefix + city, __newValue)
            return this }

        fun removeAllFromCity(values: String?): WeatherUpdater {
            updateOperations.removeAll(prefix + city, values)
            return this }

        fun condition(__newValue: String?): WeatherUpdater {
                                        updateOperations.set(prefix + condition, __newValue)
                                        return this}

        fun unsetCondition(): WeatherUpdater {

                                        updateOperations.unset(prefix + condition)
                                        return this
                                        }

        fun removeFirstFromCondition(): WeatherUpdater {
            updateOperations.removeFirst(prefix + condition)
            return this }

        fun removeLastFromCondition(): WeatherUpdater {
            updateOperations.removeLast(prefix + condition)
            return this }

        fun removeFromCondition(__newValue: String?): WeatherUpdater {
            updateOperations.removeAll(prefix + condition, __newValue)
            return this }

        fun removeAllFromCondition(values: String?): WeatherUpdater {
            updateOperations.removeAll(prefix + condition, values)
            return this }

        fun tempCelsius(__newValue: Double?): WeatherUpdater {
                                        updateOperations.set(prefix + tempCelsius, __newValue)
                                        return this}

        fun unsetTempCelsius(): WeatherUpdater {

                                        updateOperations.unset(prefix + tempCelsius)
                                        return this
                                        }

        fun incTempCelsius(__newValue: Double? = 1.toDouble?()): WeatherUpdater {
            updateOperations.inc(prefix + tempCelsius, __newValue)
            return this}

        fun removeFirstFromTempCelsius(): WeatherUpdater {
            updateOperations.removeFirst(prefix + tempCelsius)
            return this }

        fun removeLastFromTempCelsius(): WeatherUpdater {
            updateOperations.removeLast(prefix + tempCelsius)
            return this }

        fun removeFromTempCelsius(__newValue: Double?): WeatherUpdater {
            updateOperations.removeAll(prefix + tempCelsius, __newValue)
            return this }

        fun removeAllFromTempCelsius(values: Double?): WeatherUpdater {
            updateOperations.removeAll(prefix + tempCelsius, values)
            return this }

        fun humidity(__newValue: String?): WeatherUpdater {
                                        updateOperations.set(prefix + humidity, __newValue)
                                        return this}

        fun unsetHumidity(): WeatherUpdater {

                                        updateOperations.unset(prefix + humidity)
                                        return this
                                        }

        fun removeFirstFromHumidity(): WeatherUpdater {
            updateOperations.removeFirst(prefix + humidity)
            return this }

        fun removeLastFromHumidity(): WeatherUpdater {
            updateOperations.removeLast(prefix + humidity)
            return this }

        fun removeFromHumidity(__newValue: String?): WeatherUpdater {
            updateOperations.removeAll(prefix + humidity, __newValue)
            return this }

        fun removeAllFromHumidity(values: String?): WeatherUpdater {
            updateOperations.removeAll(prefix + humidity, values)
            return this }

        fun wind(__newValue: String?): WeatherUpdater {
                                        updateOperations.set(prefix + wind, __newValue)
                                        return this}

        fun unsetWind(): WeatherUpdater {

                                        updateOperations.unset(prefix + wind)
                                        return this
                                        }

        fun removeFirstFromWind(): WeatherUpdater {
            updateOperations.removeFirst(prefix + wind)
            return this }

        fun removeLastFromWind(): WeatherUpdater {
            updateOperations.removeLast(prefix + wind)
            return this }

        fun removeFromWind(__newValue: String?): WeatherUpdater {
            updateOperations.removeAll(prefix + wind, __newValue)
            return this }

        fun removeAllFromWind(values: String?): WeatherUpdater {
            updateOperations.removeAll(prefix + wind, values)
            return this }

        fun windChill(__newValue: String?): WeatherUpdater {
                                        updateOperations.set(prefix + windChill, __newValue)
                                        return this}

        fun unsetWindChill(): WeatherUpdater {

                                        updateOperations.unset(prefix + windChill)
                                        return this
                                        }

        fun removeFirstFromWindChill(): WeatherUpdater {
            updateOperations.removeFirst(prefix + windChill)
            return this }

        fun removeLastFromWindChill(): WeatherUpdater {
            updateOperations.removeLast(prefix + windChill)
            return this }

        fun removeFromWindChill(__newValue: String?): WeatherUpdater {
            updateOperations.removeAll(prefix + windChill, __newValue)
            return this }

        fun removeAllFromWindChill(values: String?): WeatherUpdater {
            updateOperations.removeAll(prefix + windChill, values)
            return this }

        fun localTime(__newValue: String?): WeatherUpdater {
                                        updateOperations.set(prefix + localTime, __newValue)
                                        return this}

        fun unsetLocalTime(): WeatherUpdater {

                                        updateOperations.unset(prefix + localTime)
                                        return this
                                        }

        fun removeFirstFromLocalTime(): WeatherUpdater {
            updateOperations.removeFirst(prefix + localTime)
            return this }

        fun removeLastFromLocalTime(): WeatherUpdater {
            updateOperations.removeLast(prefix + localTime)
            return this }

        fun removeFromLocalTime(__newValue: String?): WeatherUpdater {
            updateOperations.removeAll(prefix + localTime, __newValue)
            return this }

        fun removeAllFromLocalTime(values: String?): WeatherUpdater {
            updateOperations.removeAll(prefix + localTime, values)
            return this }
    }
}
