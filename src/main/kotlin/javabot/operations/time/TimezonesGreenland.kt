package javabot.operations.time

public class TimezonesGreenland : Timezones {

    private enum class Timezones(public val timezone: String) {
        Thule("America/Thule"),
        Godthab("America/Godthab"),
        Scoresbysund("America/Scoresbysund"),
        Danmarkshavn("America/Danmarkshavn")
    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return Timezones.Godthab.timezone
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
