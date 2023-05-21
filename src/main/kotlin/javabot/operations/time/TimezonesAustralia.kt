package javabot.operations.time

class TimezonesAustralia : Timezones {

    enum class AustralianState(
        val abbreviation: String,
        val capital: String,
        val timezone: String
    ) {
        WESTERN_AUSTRALIA("WA", "Perth", "Australia/Perth"),
        SOUTH_AUSTRALIA("SA", "Adelaide", "Australia/Adelaide"),
        NORTH_TERRITORY("NT", "Darwin", "Australia/Darwin"),
        QUEENSLAND("QLD", "Brisbane", "Australia/Brisbane"),
        NEW_SOUTH_WALES("NSW", "Sydney", "Australia/Sydney"),
        AUSTRALIAN_CAPITAL_TERRITORY("ACT", "Canberra", "Australia/Canberra"),
        VICTORIA("VIC", "Melbourne", "Australia/Victoria"),
        TASMANIA("TAS", "Hobart", "Australia/Hobart")
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
                timezones.insert(states.name.toLowerCase(), timezone)
                timezones.insert(states.abbreviation.toLowerCase(), timezone)
                timezones.insert(states.capital, timezone)
            }
        }
    }
}
