package javabot.operations.time

class TimezonesCongo : Timezones {

    private enum class Timezones(val timezone: String) {
        Kinshasa("Africa/Kinshasa"),
        Lubumbashi("Africa/Lubumbashi"),
    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return Timezones.Kinshasa.timezone
    }

    companion object {

        private val timezones = Tri<String>()

        init {
            for (timezone in Timezones.values()) {
                timezones.insert(timezone.name, timezone.timezone)
            }
        }
    }
}
