package javabot.model;

import org.apache.commons.lang.StringUtils;

/**
 * Simple model for passing around Weather conditions
 */
public class Weather {
//    PircBot does not support unicode at this time
//    private static char C = '\u2103';
//    private static char F = '\u2109';
//    private static char DOT = '\u00B7';
    private static final char C = 'C';
    private static final char F = 'F';
    private static final char SEPARATOR = '|';
    private String city;
    private String condition;
    private String tempf;
    private String tempc;
    private String humidity;
    private String wind;
    private String windChill;

    /**
     * Get city. This is just a ad-hoc String which is set by the underlying API retrieving weather info
     *
     * @return city as String.
     */
    public String getCity() {
        return city;
    }

    /**
     * Set city.
     *
     * @param city the value to set.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the weather conditions (i.e. "Sunny")
     *
     * @return condition as String.
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Set condition.
     *
     * @param condition the value to set.
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Gets the tempturature in Fahrenheit
     *
     * @return tempf as String.
     */
    public String getTempf() {
        return tempf;
    }

    /**
     * Set tempf.
     *
     * @param tempf the value to set.
     */
    public void setTempf(String tempf) {
        this.tempf = tempf;
    }

    /**
     * Get the temperature in Celcius
     *
     * @return tempc as String.
     */
    public String getTempc() {
        return tempc;
    }

    /**
     * Set tempc.
     *
     * @param tempc the value to set.
     */
    public void setTempc(String tempc) {
        this.tempc = tempc;
    }

    /**
     * Get humidity.
     *
     * @return humidity as String.
     */
    public String getHumidity() {
        return humidity;
    }

    /**
     * Set humidity.
     *
     * @param humidity the value to set.
     */
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    /**
     * Get wind speed/direction text (i.e. Wind: NW at 20mph)
     *
     * @return wind as String.
     */
    public String getWind() {
        return wind;
    }

    /**
     * Set wind.
     *
     * @param wind the value to set.
     */
    public void setWind(String wind) {
        this.wind = wind;
    }

    /**
     * Get a windchill condition (i.e. 15 F (-9 C) )
     *
     * @return
     */
    public String getWindChill() {
        return windChill;
    }

    public void setWindChill(String windChill) {
        this.windChill = windChill;
    }

    private String replaceDegrees(String degrees) {
        return degrees.replace('C', C).replace('F', F);
    }

    @Override
    public String toString() {
        String dotWithSpaces = " " + SEPARATOR + " ";
        StringBuilder result = new StringBuilder("Weather for ");
        result.append(city);
        result.append(dotWithSpaces);
        result.append(tempf);
        result.append(' ');
        result.append(F);
        result.append(" (");
        result.append(tempc);
        result.append(" ");
        result.append(C);
        result.append(')');
        if (StringUtils.trimToEmpty(windChill).isEmpty()) {
            result.append(dotWithSpaces);
        } else {
            result.append(" feels like ");
            result.append(replaceDegrees(windChill));
            result.append(dotWithSpaces);
        }
        result.append("humidity at ");
        result.append(humidity);
        result.append(dotWithSpaces);
        result.append(condition);
        result.append(dotWithSpaces);
        if (StringUtils.trimToEmpty(this.wind).equals("")) {
            result.append("Wind ");
            //lowercase the first char since we are prepending with an uppercase word
            //for instance "wind" might be "From the West at..."  We'd want that do be "from the West at..."
            result.append(this.wind.substring(0,1).toLowerCase());
            result.append(this.wind.substring(1));
        }
        return result.toString();
    }

}
