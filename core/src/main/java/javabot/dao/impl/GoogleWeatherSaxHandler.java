package javabot.dao.impl;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.HashMap;
import java.util.Map;
import javabot.model.Weather;

/**
 * SAX handler for Google's weather API
 *
 * @author Craig Tataryn &lt;craiger@tataryn.net&gt;
 */
public class GoogleWeatherSaxHandler extends DefaultHandler {
    private boolean collectData;
    private boolean error;
    private Map<String, String> weatherMap = new HashMap<String, String>();

    public void startElement(String uri, String localName, 
            String qName, Attributes attributes) 
                throws SAXException {
        if (qName.equalsIgnoreCase("current_conditions")) {
            collectData = true;
        } else if (qName.equalsIgnoreCase("forecast_information")) {
            collectData = true;
        } else if (qName.equalsIgnoreCase("problem_cause")) {
            error = true;
        } else if (collectData) {
            weatherMap.put(qName, attributes.getValue("data"));

        }

    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("current_conditions")) {
            collectData = false;
        }
    }

    /**
     * Returns the weather data parsed from the feed.
     * @return Weather data, or null if none was parsed
     */
    public Weather getWeather() {
        if (error) {
            //TODO Log
            return null;
        } else {
            Weather weather = new Weather();
            weather.setCity(weatherMap.get("city"));
            weather.setCondition(weatherMap.get("condition"));
            weather.setTempf(weatherMap.get("temp_f"));
            weather.setTempc(weatherMap.get("temp_c"));
            weather.setHumidity(weatherMap.get("humidity"));
            weather.setWind(weatherMap.get("wind_condition"));
            return weather;
        }
    }
}
