package javabot.operations

import com.google.inject.Inject
import javabot.Message
import javabot.operations.time.Country
import javabot.operations.time.CountryParser
import javabot.operations.time.DateUtils
import javabot.operations.time.StringUtils
import javabot.operations.time.TimezoneParser
import javabot.operations.time.TimezoneParser.Timezone

import java.util.ArrayList

/**
 * Usage convention Countries with single timezone: ~time in [country_name]
 *  Countries with multiple timezones: ~time in [country_name], [province/state]
 */
public class CurrentTimeOperation : BotOperation() {

    Inject
    var countryParser: CountryParser
    Inject
    var timezoneParser: TimezoneParser
    Inject
    var dateUtils: DateUtils
    Inject
    var stringUtils: StringUtils

    override fun handleMessage(event: Message): Boolean {
        val message = event.value
        val responses = ArrayList<Message>()

        if (message.contains(key)) {
            val locale = extractLocale(message)

            val country = countryParser.getCountry(locale.country)
            val result: String

            if (country == null) {
                result = "Location you entered is either not supported or does not exist."
            } else {
                val timezone = timezoneParser.getTimezone(country, locale.region)
                result = String.format("Current time in %s is %s.",
                      stringUtils.capitalizeFirstCharacter(timezone.region),
                      dateUtils.getCurrentDateAtZone(timezone.timezone))
            }

            bot.postMessageToChannel(event, result)
            return true
        }
        return false
    }

    private fun extractLocale(raw: String): InputLocale {
        val extracted = raw.substring(raw.indexOf(key) + key.length())
        val inputs = extracted.split(",")

        if (inputs.size() == 1) {
            return InputLocale(inputs[0], null)
        } else if (inputs.size() == 2) {
            return InputLocale(inputs[0], inputs[1])
        }
        return InputLocale(null, null)
    }

    private inner class InputLocale(country: String, province: String) {
        public var country: String
            set
        public var region: String
            set

        init {
            this.country = country
            this.region = province
        }
    }

    companion object {

        private val key = "time in"
    }
}
