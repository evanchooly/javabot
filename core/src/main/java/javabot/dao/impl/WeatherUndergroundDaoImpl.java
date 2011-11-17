package javabot.dao.impl;

import javabot.dao.WeatherDao;
import javabot.model.Weather;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.net.URL;
import java.net.URLEncoder;

/**
 * Implements a weather service Dao using Weather Underground's weather API
 *
 * @see WeatherDaoImpl
 * @author Craig Tataryn &lt;craiger@tataryn.net&gt;
 */
public class WeatherUndergroundDaoImpl implements WeatherDao {
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
