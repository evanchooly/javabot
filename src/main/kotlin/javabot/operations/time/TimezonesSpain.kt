package javabot.operations.time

public class TimezonesSpain : Timezones {

    private enum class Timezones(public val timezone: String) {
        Canary_Islands("Atlantic/Canary"),
        Ceuta("Africa/Ceuta"),
        Madrid("Europe/Madrid")
    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return Timezones.Madrid.timezone
    }

    companion object {

        private val timezones = Tri<String>()

        init {
            for (timezone in Timezones.values()) {
                timezones.insert(timezone.name(), timezone.timezone)
            }
        }
    }
}
