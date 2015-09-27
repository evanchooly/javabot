package javabot.operations.time

public class TimezonesAustralia : Timezones {

    public enum class AustralianState(regionCode: Int, public val abbreviation: String, public val capital: String,
                                      public val timezone: String) {
        WESTERN_AUSTRALIA(8, "WA", "Perth", "Australia/Perth"),
        SOUTH_AUSTRALIA(5, "SA", "Adelaide", "Australia/Adelaide"),
        NORTH_TERRITORY(3, "NT", "Darwin", "Australia/Darwin"),
        QUEENSLAND(4, "QLD", "Brisbane", "Australia/Brisbane"),
        NEW_SOUTH_WALES(2, "NSW", "Sydney", "Australia/Sydney"),
        AUSTRALIAN_CAPITAL_TERRITORY(1, "ACT", "Canberra", "Australia/Canberra"),
        VICTORIA(7, "VIC", "Melbourne", "Australia/Victoria"),
        TASMANIA(6, "TAS", "Hobart", "Australia/Hobart")

    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return AustralianState.NEW_SOUTH_WALES.timezone
    }

    companion object {

        private val timezones = Tri<String>()

        init {
            for (states in AustralianState.values()) {
                val timezone = states.timezone
                timezones.insert(states.name().toLowerCase(), timezone)
                timezones.insert(states.abbreviation.toLowerCase(), timezone)
                timezones.insert(states.capital, timezone)
            }
        }
    }
}
