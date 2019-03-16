package javabot.model

import org.apache.commons.lang.StringUtils

/**
 * Simple model for passing around Weather conditions
 */
class Weather {
    var city: String? = null
    var condition: String? = null
    var tempf: String? = null
    var tempc: String? = null
    var humidity: String? = null
    var wind: String? = null
    var windChill: String? = null
    var localTime: String? = null

    private fun replaceDegrees(degrees: String?): String {
        return degrees?.replace('C', C)?.replace('F', F) ?: ""
    }

    override fun toString(): String {
        var windShown = false
        val dotWithSpaces = " $SEPARATOR "
        val result = StringBuilder("Weather for ")
        result.append(city)
        result.append(dotWithSpaces)
        result.append(tempf)
        result.append(' ')
        result.append(F)
        result.append(" (")
        result.append(tempc)
        result.append(" ")
        result.append(C)
        result.append(')')
        if (StringUtils.trimToEmpty(windChill).isEmpty()) {
            result.append(dotWithSpaces)
        } else {
            result.append(" feels like ")
            result.append(replaceDegrees(windChill))
            result.append(dotWithSpaces)
        }
        result.append("humidity at ")
        result.append(humidity)
        result.append(dotWithSpaces)
        result.append(condition)
        result.append(dotWithSpaces)
        if (StringUtils.trimToEmpty(this.wind) == "") {
            windShown = true
            result.append("Wind ")
            //lowercase the first char since we are prepending with an uppercase word
            //for instance "wind" might be "From the West at..."  We'd want that do be "from the West at..."
            result.append(this.wind!!.substring(0, 1).toLowerCase())
            result.append(this.wind!!.substring(1))
        }
        if (this.localTime != null) {
            if (windShown) {
                result.append(dotWithSpaces)
            }
            result.append(this.localTime)
        }
        return result.toString()
    }

    companion object {
        //    PircBot does not support unicode at this time
        //    private static char C = '\u2103';
        //    private static char F = '\u2109';
        //    private static char DOT = '\u00B7';
        private const val C = 'C'
        private const val F = 'F'
        private const val SEPARATOR = '|'
    }
}