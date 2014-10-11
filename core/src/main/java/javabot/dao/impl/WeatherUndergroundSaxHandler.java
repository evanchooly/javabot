package javabot.dao.impl;

import javabot.model.Weather;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WeatherUndergroundSaxHandler extends DefaultHandler {
    boolean collectLocationData = false;
    boolean collectWeatherData = false;
    String currentElem;
    boolean error = false;

    private final Map<String, String> weatherMap = new HashMap<String, String>();
    private Set<String> weatherElems = new HashSet<String>() {
        {
            add("temp_f");
            add("temp_c");
            add("weather");
            add("wind_string");
            add("relative_humidity");
            add("windchill_string");
        }
    };

    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
                throws SAXException {
        if ("display_location".equalsIgnoreCase(qName)) {
            collectLocationData = true;
            collectWeatherData = false;
        } else if ("current_observation".equalsIgnoreCase(qName)) {
            collectWeatherData = true;
        }

        if ((collectLocationData || collectWeatherData)) {
            currentElem = qName;
        }
    }

    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if ("display_location".equalsIgnoreCase(qName)) {
            collectLocationData = false;
            collectWeatherData = true;
        } else if ("current_observation".equalsIgnoreCase(qName)) {
            collectWeatherData = false;
        }
        currentElem = "";
    }

    @Override
    public void characters(char[] chars, int start, int length) throws SAXException {
        String body = StringUtils.trimToEmpty(new StringBuffer().append(chars, start, length).toString());
        if (!error) {
            if (collectWeatherData && weatherElems.contains(currentElem)) {
                //In certain cases WU will display "NA" for elements that don't have a value
                weatherMap.put(currentElem, StringUtils.trimToEmpty(body).equals("NA") ? "" : body);
            } else if (collectLocationData && "full".equals(currentElem)) {
                weatherMap.put(currentElem, body);
            }
        }
    }

    /**
     * Returns the weather data parsed from the feed.
     * @return Weather data, or null if none was parsed
     */
    public Weather getWeather() {
        error = error || weatherMap.get("full").equals(",");
        if (error) {
            //TODO Log
            return null;
        } else {
            final Weather weather = new Weather();
            weather.setCity(weatherMap.get("full"));
            weather.setCondition(weatherMap.get("weather"));
            weather.setTempf(weatherMap.get("temp_f"));
            weather.setTempc(weatherMap.get("temp_c"));
            weather.setHumidity(weatherMap.get("relative_humidity"));
            weather.setWind(weatherMap.get("wind_string"));
            weather.setWindChill(weatherMap.get("windchill_string"));
            return weather;
        }
    }
}
