package javabot.operations.time

public class TimezonesAntarctica : Timezones {

    private enum class Timezones(public val timezone: String) {
        Palmer("Antarctica/Palmer"),
        Rothera("Antarctica/Rothera"),
        Syowa("Antarctica/Syowa"),
        Mawson("Antarctica/Mawson"),
        Vostok("Antarctica/Vostok"),
        Davis("Antarctica/Davis"),
        Casey("Antarctica/Casey"),
        Dumont_D_Urville("Antarctica/DumontDUrville")
    }

    override fun get(province: String): String {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return Timezones.Palmer.timezone
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
