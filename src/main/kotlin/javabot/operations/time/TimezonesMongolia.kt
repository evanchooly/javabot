package javabot.operations.time

public class TimezonesMongolia : Timezones {

    private enum class Timezones(public val timezone: String) {
        Choibalsan("Asia/Choibalsan"),
        Ulaanbaatar("Asia/Ulaanbaatar")
    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return Timezones.Ulaanbaatar.timezone
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
