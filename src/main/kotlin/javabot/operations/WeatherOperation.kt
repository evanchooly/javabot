package javabot.operations

import com.antwerkz.sofia.Sofia
import java.util.Locale
import javabot.Javabot
import javabot.Message
import javabot.dao.AdminDao
import javabot.dao.weather.WeatherDao
import javax.inject.Inject

/** Gets current weather conditions for a place given as a parameter. */
class WeatherOperation
@Inject
constructor(bot: Javabot, adminDao: AdminDao, var weatherDao: WeatherDao) :
    BotOperation(bot, adminDao) {
    override fun handleMessage(event: Message): List<Message> {
        val responses = arrayListOf<Message>()
        val message = event.value
        if (message.lowercase(Locale.getDefault()).startsWith("weather ")) {
            val place = message.substring("weather ".length).trim()
            val result = weatherDao.getWeatherFor(place)
            responses.add(Message(event, result?.toString() ?: Sofia.weatherUnknown(place)))
        }
        return responses
    }
}
