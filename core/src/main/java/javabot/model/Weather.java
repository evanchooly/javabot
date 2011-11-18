package javabot.model;

import org.apache.commons.lang.StringUtils;

/**
 * Simple model for passing around Weather conditions
 *
 * @author Craig Tataryn &lt;craiger@tataryn.net&gt;
 */
public class Weather {
    private static char C = '\u2103';
    private static char F = '\u2109';
    private static char DOT = '\u00B7';

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
    public String getCity()
    {
        return city;
    }
    
    /**
     * Set city.
     *
     * @param city the value to set.
     */
    public void setCity(String city)
    {
        this.city = city;
    }
    
    /**
     * Gets the weather conditions (i.e. "Sunny")
     *
     * @return condition as String.
     */
    public String getCondition()
    {
        return condition;
    }
    
    /**
     * Set condition.
     *
     * @param condition the value to set.
     */
    public void setCondition(String condition)
    {
        this.condition = condition;
    }
    
    /**
     * Gets the tempturature in Fahrenheit
     *
     * @return tempf as String.
     */
    public String getTempf()
    {
        return tempf;
    }
    
    /**
     * Set tempf.
     *
     * @param tempf the value to set.
     */
    public void setTempf(String tempf)
    {
        this.tempf = tempf;
    }
    
    /**
     * Get the temperature in Celcius
     *
     * @return tempc as String.
     */
    public String getTempc()
    {
        return tempc;
    }
    
    /**
     * Set tempc.
     *
     * @param tempc the value to set.
     */
    public void setTempc(String tempc)
    {
        this.tempc = tempc;
    }
    
    /**
     * Get humidity.
     *
     * @return humidity as String.
     */
    public String getHumidity()
    {
        return humidity;
    }
    
    /**
     * Set humidity.
     *
     * @param humidity the value to set.
     */
    public void setHumidity(String humidity)
    {
        this.humidity = humidity;
    }
    
    /**
     * Get wind speed/direction text (i.e. Wind: NW at 20mph)
     *
     * @return wind as String.
     */
    public String getWind()
    {
        return wind;
    }
    
    /**
     * Set wind.
     *
     * @param wind the value to set.
     */
    public void setWind(String wind)
    {
        this.wind = wind;
    }

    /**
     * Get a windchill condition (i.e. 15 F (-9 C) )
     * @return
     */
    public String getWindChill() {
        return windChill;
    }

    public void setWindChill(String windChill) {
        this.windChill = windChill;
    }

    private String replaceDegrees(String degrees) {
        return degrees.replace('C', '℃').replace('F', '℉');
    }
    
    @Override
    public String toString() {
        String dotWithSpaces = " " + DOT + " ";
        StringBuffer result = new StringBuffer("Weather for ");
        result.append(this.city);
        result.append(dotWithSpaces);
        result.append(this.tempf);
        result.append(' ');
        result.append(F);
        result.append(" (");
        result.append(this.tempc);
        result.append(" ");
        result.append(C);
        result.append(')');
        if (StringUtils.trimToEmpty(this.windChill).equals("")) {
            result.append(dotWithSpaces);
        } else {
            result.append(" feels like ");
            result.append(replaceDegrees(this.windChill));
            result.append(dotWithSpaces);
        }
        result.append(this.humidity);
        result.append(dotWithSpaces);
        result.append(this.condition);
        result.append(dotWithSpaces);
        result.append(this.wind);
        return result.toString();
    }

}
