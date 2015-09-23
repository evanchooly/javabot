package javabot.operations.time

public class TimezoneParser {

    public fun getTimezone(country: Country, province: String): Timezone {
        val timezone: Timezone

        if (country.isMultiZones) {
            val timezones = country.multiZones
            val timezoneId = timezones.get(province)

            if (timezoneId != null) {
                timezone = Timezone(timezoneId, province)
            } else {
                timezone = Timezone(timezones.capital, country.name)
            }
        } else {
            timezone = Timezone(country.timezone, country.name)
        }
        return timezone
    }

    public inner class Timezone(timezone: String, region: String) {
        public var timezone: String
            set
        public var region: String
            set

        init {
            this.timezone = timezone
            this.region = region
        }
    }
}
