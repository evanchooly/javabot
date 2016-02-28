package javabot.operations.time

class TimezonesEcuador : Timezones {

    private enum class Timezones(val timezone: String) {
        Galapagos("Pacific/Galapagos"),
        Guayaquil("America/Guayaquil")
    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return Timezones.Guayaquil.timezone
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
