package javabot.dao.impl;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import java.net.URL;
import org.xml.sax.helpers.XMLReaderFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.util.HashMap;
import java.util.Map;
import javabot.model.Weather;
import javabot.dao.WeatherDao;
import org.springframework.stereotype.Component;

/**
 * Implements a weather service Dao using Google's weather API
 * 
 */
@Component
public class GoogleWeatherDaoImpl implements WeatherDao {
    private static String API_URL = "http://www.google.com/ig/api?weather=";

    public Weather getWeatherFor(String place) {

        GoogleWeatherSaxHandler handler = new GoogleWeatherSaxHandler();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler); 
            reader.parse(new InputSource(new URL(API_URL + place)
                        .openStream()));
        } catch (Exception e) {
            //TODO proper logging/distinction between error vs no-data
            return null;
        }
        return handler.getWeather();
        
    }

}
