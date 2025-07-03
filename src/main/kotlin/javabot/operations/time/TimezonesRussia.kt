package javabot.operations.time

class TimezonesRussia : Timezones {

    private enum class Timezones(val timezone: String) {
        Kaliningrad("Europe/Kaliningrad"),
        Moscow("Europe/Moscow"),
        Samara("Europe/Samara"),
        Yekaterinburg("Asia/Yekaterinburg"),
        Omsk("Asia/Omsk"),
        Krasnoyarsk("Asia/Krasnoyarsk"),
        Irkutsk("Asia/Irkutsk"),
        Yakutsk("Asia/Yakutsk"),
        Yuzhno_Sakhalinsk("Asia/Vladivostok"),
        Petropavlovsk_Kamchatskiy("Asia/Kamchatka"),
        Magadan("Asia/Magadan"),
    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return Timezones.Moscow.timezone
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
