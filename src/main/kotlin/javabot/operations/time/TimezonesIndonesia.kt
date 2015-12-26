package javabot.operations.time

public class TimezonesIndonesia : Timezones {

    private enum class Timezones(public val timezone: String) {
        Jakarta("Asia/Jakarta"),
        Makassar("Asia/Makassar"),
        Jayapura("Asia/Jayapura")
    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return Timezones.Jakarta.timezone
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
