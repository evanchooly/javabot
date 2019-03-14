@file:Suppress("unused")

package javabot.dao.impl.openweathermap.model

class OWWeather(
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
        var cod: Int? = null
)

class OWCoordinates(
        var lon: Double? = null,
        var lat: Double? = null
)

class OWCondition(
        var id: Long? = null,
        var main: String? = null,
        var description: String? = null,
        var icon: String? = null
)

class OWMain(
        var temp: Double? = null,
        var pressure: Long? = null,
        var humidity: Long? = null,
        var temp_min: Double? = null,
        var temp_max: Double? = null
)

class OWWind(
        var speed: Double? = null,
        var deg: Long? = null,
        var gust: Double? = null
)

class OWClouds(
        var all: Long? = null
)

class OWSys(
        var type: Long? = null,
        var id: Long? = null,
        var message: Double? = null,
        var country: String? = null,
        var sunrise: Long? = null,
        var sunset: Long? = null
)