package javabot.dao.impl;

import java.net.URL;
import java.net.URLEncoder;

import javabot.dao.WeatherHandler;
import javabot.model.Weather;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Implements a weather service Dao using Weather Underground's weather API
 *
 * @see WeatherDao
 */
public class WeatherUndergroundHandler implements WeatherHandler {
    private static final String API_URL = "http://api.wunderground.com/auto/wui/geo/WXCurrentObXML/index.xml?query=";

    public Weather getWeatherFor(final String place) {
        WeatherUndergroundSaxHandler handler = new WeatherUndergroundSaxHandler();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new URL(API_URL + URLEncoder.encode(place, "UTF-8"))
                .openStream()));
        } catch (Exception e) {
            //TODO proper logging/distinction between error vs no-data
            return null;
        }
        return handler.getWeather();
    }
}
