package javabot.operations.time

public class TimezonesPortugal : Timezones {

    private enum class Timezones(public val timezone: String) {
        Azores("Atlantic/Azores"),
        Lisbon("Europe/Lisbon")
    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return Timezones.Lisbon.timezone
    }

    companion object {

        private val timezones = Tri<String>()

        init {
            for (timezone in Timezones.values) {
                timezones.insert(timezone.name, timezone.timezone)
            }
        }
    }
}
