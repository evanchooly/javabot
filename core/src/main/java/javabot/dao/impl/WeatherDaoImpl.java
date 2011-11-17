package javabot.dao.impl;

import javabot.dao.WeatherDao;
import javabot.model.Weather;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implements a weather service Dao using Weather Underground's Weather service,
 * if that fails it tries Google's.
 * <p>
 *     The user can also specify "from google for [place]" or "from wu for [place]" if they want to retrieve
 *     weather data from a specific API
 * </p>
 *
 * @author Craig Tataryn &lt;craiger@tataryn.net&gt;
 */
@Component
public class WeatherDaoImpl implements WeatherDao {
    private static int WU = 0;
    private static int GOOGLE = 1;

    List<WeatherDao> weatherDaos = new ArrayList<WeatherDao>() {
        {
            add(new WeatherUndergroundDaoImpl());
            add(new GoogleWeatherDaoImpl());
        }
    };

    public Weather getWeatherFor(final String place) {
        Weather weather = null;
        //if they ask for a specific service, use it
        if (place.toLowerCase().startsWith("from google for ")) {
            return weatherDaos.get(GOOGLE).getWeatherFor(place.substring(16));
        } else if (place.toLowerCase().startsWith("from wu for ")) {
            return weatherDaos.get(WU).getWeatherFor(place.substring(12));
        }

        //if they don't want a sepcific service, then use each service until one returns something
        for (WeatherDao weatherDao : weatherDaos) {
            weather = weatherDao.getWeatherFor(place);
            if (weather == null) {
                continue;
            } else {
                break;
            }
        }
        return weather;
    }
}
