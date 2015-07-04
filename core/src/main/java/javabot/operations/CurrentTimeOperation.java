package javabot.operations;

import com.google.inject.Inject;
import javabot.Message;
import javabot.operations.time.Country;
import javabot.operations.time.CountryParser;
import javabot.operations.time.DateUtils;
import javabot.operations.time.StringUtils;
import javabot.operations.time.TimezoneParser;
import javabot.operations.time.TimezoneParser.Timezone;

import java.util.ArrayList;
import java.util.List;

/**
 * Usage convention<br/> Countries with single timezone: ~time in [country_name]
 * <br/> Countries with multiple timezones: ~time in [country_name], [province/state]<br/>
 */
public class CurrentTimeOperation extends BotOperation {

    @Inject
    CountryParser countryParser;
    @Inject
    TimezoneParser timezoneParser;
    @Inject
    DateUtils dateUtils;
    @Inject
    StringUtils stringUtils;

    private static final String key = "time in";

    @Override
    public boolean handleMessage(final Message event) {
        final String message = event.getValue();
        final List<Message> responses = new ArrayList<>();

        if (message.contains(key)) {
            InputLocale locale = extractLocale(message);

            Country country = countryParser.getCountry(locale.getCountry());
            String result;

            if (country == null) {
                result = "Location you entered is either not supported or does not exist.";
            } else {
                Timezone timezone = timezoneParser.getTimezone(country, locale.getRegion());
                result = String.format("Current time in %s is %s.",
                                       stringUtils.capitalizeFirstCharacter(timezone.getRegion()),
                                       dateUtils.getCurrentDateAtZone(timezone.getTimezone()));
            }

            getBot().postMessageToChannel(event, result);
            return true;
        }
        return false;
    }

    private InputLocale extractLocale(String raw) {
        String extracted = raw.substring(raw.indexOf(key) + key.length());
        String[] inputs = extracted.split(",");

        if (inputs.length == 1) {
            return new InputLocale(inputs[0], null);
        } else if (inputs.length == 2) {
            return new InputLocale(inputs[0], inputs[1]);
        }
        return new InputLocale(null, null);
    }

    private class InputLocale {
        String country;
        String region;

        public InputLocale(String country, String province) {
            this.country = country;
            this.region = province;
        }

        public String getCountry() {
            return country;
        }

        public String getRegion() {
            return region;
        }
    }
}
