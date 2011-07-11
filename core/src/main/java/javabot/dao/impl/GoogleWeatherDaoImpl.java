package javabot.dao.impl;

import java.net.URL;

import javabot.dao.WeatherDao;
import javabot.model.Weather;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Implements a weather service Dao using Google's weather API
 */
@Component
public class GoogleWeatherDaoImpl implements WeatherDao {
    private static final String API_URL = "http://www.google.com/ig/api?weather=";

    public Weather getWeatherFor(final String place) {
        try {
            final GoogleWeatherSaxHandler handler = new GoogleWeatherSaxHandler();
            final XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new URL(API_URL + place)
                .openStream()));
            return handler.getWeather();
        } catch (Exception e) {
            //TODO proper logging/distinction between error vs no-data
            return null;
        }
    }
}