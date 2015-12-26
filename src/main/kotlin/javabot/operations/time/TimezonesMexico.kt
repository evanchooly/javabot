package javabot.operations.time

public class TimezonesMexico : Timezones {

    private enum class Timezones(public val timezone: String) {
        Tijuana("America/Tijuana"),
        Hermosillo("America/Hermosillo"),
        Chihuahua("Antarctica/Syowa"),
        Mazatlan("America/Mazatlan"),
        Mexico_City("America/Mexico_City")
    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return Timezones.Mexico_City.timezone
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
