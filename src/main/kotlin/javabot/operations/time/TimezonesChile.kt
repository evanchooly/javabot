package javabot.operations.time

class TimezonesChile : Timezones {

    private enum class Timezones(val timezone: String) {
        Noronha("Pacific/Easter"),
        Santiago("America/Santiago"),
    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return Timezones.Santiago.timezone
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
