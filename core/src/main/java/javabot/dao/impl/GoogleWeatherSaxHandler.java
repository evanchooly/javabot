package javabot.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javabot.model.Weather;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX handler for Google's weather API
 *
 * @author Craig Tataryn &lt;craiger@tataryn.net&gt;
 */
public class GoogleWeatherSaxHandler extends DefaultHandler {
    private boolean collectData;
    private boolean error;
    private final Map<String, String> weatherMap = new HashMap<String, String>();

    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
                throws SAXException {
        if ("current_conditions".equalsIgnoreCase(qName) || "forecast_information".equalsIgnoreCase(qName)) {
            collectData = true;
        } else if ("problem_cause".equalsIgnoreCase(qName)) {
            error = true;
        } else if (collectData) {
            weatherMap.put(qName, attributes.getValue("data"));
        }
    }

    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if ("current_conditions".equalsIgnoreCase(qName)) {
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
            final Weather weather = new Weather();
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
