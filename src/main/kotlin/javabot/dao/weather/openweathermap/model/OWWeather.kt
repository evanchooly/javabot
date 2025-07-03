@file:Suppress("unused")

package javabot.dao.weather.openweathermap.model

data class OWWeather(
    var coord: OWCoordinates? = null,
    var weather: List<OWCondition>? = null,
    var base: String? = null,
    var main: OWMain? = null,
    var visibility: Long? = null,
    var wind: OWWind? = null,
    var clouds: OWClouds? = null,
    var dt: Long? = null,
    var sys: OWSys? = null,
    var id: Long? = null,
    var name: String? = null,
    var timezone: String? = null,
    var cod: Int? = null,
)

data class OWCoordinates(var lon: Double? = null, var lat: Double? = null)

data class OWCondition(
    var id: Long? = null,
    var main: String? = null,
    var description: String? = null,
    var icon: String? = null,
)

data class OWMain(
    var temp: Double? = null,
    var pressure: Long? = null,
    var humidity: Long? = null,
    var temp_min: Double? = null,
    var temp_max: Double? = null,
    var feels_like: Double? = null,
)

data class OWWind(var speed: Double? = null, var deg: Long? = null, var gust: Double? = null)

data class OWClouds(var all: Long? = null)

data class OWSys(
    var type: Long? = null,
    var id: Long? = null,
    var message: Double? = null,
    var country: String? = null,
    var sunrise: Long? = null,
    var sunset: Long? = null,
)
