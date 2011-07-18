package javabot.dao.impl;

import java.net.URL;
import java.net.URLEncoder;

import javabot.dao.WeatherDao;
import javabot.model.Weather;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Implements a weather service Dao using Google's weather API
 *
 * @author Craig Tataryn &lt;craiger@tataryn.net&gt;
 */
@Component
public class GoogleWeatherDaoImpl implements WeatherDao {
    private static final String API_URL = "http://www.google.com/ig/api?weather=";

    public Weather getWeatherFor(final String place) {
        GoogleWeatherSaxHandler handler = new GoogleWeatherSaxHandler();
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
