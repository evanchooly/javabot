package javabot.dao.weather

/**
 * Simple model for passing around Weather conditions
 */
data class Weather(
        var city: String? = null,
        var condition: String? = null,
        var tempCelsius: Double? = null,
        var humidity: String? = null,
        var wind: String? = null,
        var windChill: String? = null,
        var localTime: String? = null
) {
    override fun toString(): String {
        val dotWithSpaces = " $SEPARATOR "
        val result = StringBuilder("Weather for $city$dotWithSpaces")
        val celsius = tempCelsius
        if (celsius != null) {
            val fahrenheit = (celsius * 9 / 5.0) + 32
            result.append("${fahrenheit.toInt()} F (${celsius.toInt()} C)")
        }
        if ((windChill ?: "").isNotEmpty()) {
            result.append(" - feels like $windChill")
        }
        if ((humidity ?: "").isNotEmpty()) {
            result.append(dotWithSpaces)
            result.append("humidity at $humidity")
        }
        if ((condition ?: "").isNotEmpty()) {
            result.append("$dotWithSpaces$condition")
        }
        if ((localTime ?: "").isNotEmpty()) {
            result.append("$dotWithSpaces$localTime")
        }
        return result.toString()
    }

    companion object {
        private val SEPARATOR = '|'
    }
}