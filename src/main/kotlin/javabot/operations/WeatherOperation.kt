package javabot.operations

import com.antwerkz.sofia.Sofia
import javabot.Message
import javabot.dao.impl.WeatherDao
import javabot.model.Weather

import javax.inject.Inject

/**
 * Gets current weather conditions for a place given as a parameter.
 */
public class WeatherOperation : BotOperation() {
    @Inject
    lateinit var weatherDao: WeatherDao

    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        if (message.toLowerCase().startsWith("weather ")) {
            val place = message.substring("weather ".length).trim()
            val result = weatherDao.getWeatherFor(place)
            bot.postMessageToChannel(event, if (result == null) Sofia.weatherUnknown(place) else result.toString())
            return true
        }
        return false
    }

}
