package javabot.model

import org.apache.commons.lang.StringUtils

import java.text.SimpleDateFormat
import java.util.Date

/**
 * Simple model for passing around Weather conditions
 */
public class Weather {
    public var city: String? = null
    public var condition: String? = null
    public var tempf: String? = null
    public var tempc: String? = null
    public var humidity: String? = null
    public var wind: String? = null
    public var windChill: String? = null
    public var localTime: String? = null

    private fun replaceDegrees(degrees: String): String {
        return degrees.replace('C', C).replace('F', F)
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
        private val C = 'C'
        private val F = 'F'
        private val SEPARATOR = '|'
    }

}
/**
 * Get city. This is just a ad-hoc String which is set by the underlying API retrieving weather info

 * @return city as String.
 */
/**
 * Set city.

 * @param city the value to set.
 */
/**
 * Gets the weather conditions (i.e. "Sunny")

 * @return condition as String.
 */
/**
 * Set condition.

 * @param condition the value to set.
 */
/**
 * Gets the tempturature in Fahrenheit

 * @return tempf as String.
 */
/**
 * Set tempf.

 * @param tempf the value to set.
 */
/**
 * Get the temperature in Celcius

 * @return tempc as String.
 */
/**
 * Set tempc.

 * @param tempc the value to set.
 */
/**
 * Get humidity.

 * @return humidity as String.
 */
/**
 * Set humidity.

 * @param humidity the value to set.
 */
/**
 * Get wind speed/direction text (i.e. Wind: NW at 20mph)

 * @return wind as String.
 */
/**
 * Set wind.

 * @param wind the value to set.
 */
/**
 * Get a windchill condition (i.e. 15 F (-9 C) )

 * @return
 */
