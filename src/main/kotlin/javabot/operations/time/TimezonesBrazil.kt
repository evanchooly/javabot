package javabot.operations.time

public class TimezonesBrazil : Timezones {

    private enum class Timezones(public val timezone: String) {
        Boa_Vista("America/Boa_Vista"),
        Campo_Grande("America/Campo_Grande"),
        Cuiaba("America/Cuiaba"),
        Manaus("America/Manaus"),
        Porto_Velho("America/Porto_Velho"),
        Rio_Branco("America/Rio_Branco"),
        Araguaina("America/Araguaina"),
        Salvador("America/Bahia"),
        Fortaleza("America/Fortaleza"),
        Maceio("America/Maceio"),
        Recife("America/Recife"),
        Sao_Paulo("America/Sao_Paulo"),
        Noronha("America/Noronha")
    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return Timezones.Sao_Paulo.timezone
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
