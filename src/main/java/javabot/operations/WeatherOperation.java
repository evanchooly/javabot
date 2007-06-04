package javabot.operations;

import java.util.ArrayList;
import java.util.List;

import javabot.BotEvent;
import javabot.Message;

// User: joed
// Date: May 3, 2007
// Time: 3:15:07 PM

//

public class WeatherOperation implements BotOperation {
    public List<Message> handleMessage(BotEvent event) {
        List<Message> messages = new ArrayList<Message>();

//        String message = event.getMessage();
//        String channel = event.getChannel();
//
//        if (!message.startsWith("weather "))
//            return messages;
//
//
//        String[] words = message.split(" ");
//
//        message = "Usage: ~weather STATION-ID - see http://www.rap.ucar.edu/weather/surface/stations.txt";
//
//
//        final Metar metar = net.sf.jweather.Weather.getMetar(words[1]);
//
//        if (metar != null) {
//            Float result;
//
//            message = "Station ID: " + metar.getStationID();
//
//            result = metar.getWindSpeedInMPH();
//
//
//            if (result != null) {
//                message = message + " Wind Speed: " + result + " mph, " + metar.getWindSpeedInKnots() + " knots";
//            }
//
//            result = metar.getVisibility();
//
//            if (result != null) {
//                if (!metar.getVisibilityLessThan()) {
//                    message = message + " Visibility: " + result + " mile(s)";
//                } else {
//                    message = message + " Visibility: < " + result + " mile(s)";
//                }
//            }
//
//            result = metar.getPressure();
//
//            if (result != null) {
//                message = message + " Pressure: " + result + " in Hg";
//            }
//
//            result = metar.getTemperatureInCelsius();
//
//            if (result != null) {
//                message = message + " Temperature: " + result + " C, " + metar.getTemperatureInFahrenheit() + " F";
//
//            }
//
//            if (metar.getWeatherConditions() != null) {
//
//                for (Object o : metar.getWeatherConditions()) {
//                    final WeatherCondition weatherCondition = (WeatherCondition) o;
//                    message = message + " " + weatherCondition.getNaturalLanguageString();
//                }
//            }
//
//            if (metar.getSkyConditions() != null) {
//
//                for (Object o : metar.getSkyConditions()) {
//                    final SkyCondition skyCondition = (SkyCondition) o;
//                    message = message + " " + skyCondition.getNaturalLanguageString();
//                }
//            }
//
//
//        }
//
//
//        messages.add(new Message(channel, message, false));
//
        return messages;
    }

    public List<Message> handleChannelMessage(BotEvent event) {
        return new ArrayList<Message>();
    }
}
