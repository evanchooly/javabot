package javabot.operations.time

class TimezonesCanada : Timezones {

    enum class CanadianProvince(private val regionCode: Int, val abbreviation: String, val capital: String,
                                       val timezone: String) {
        ALBERTA(1, "AB", "Edmonton", "America/Edmonton"),
        BRITISH_COLUMBIA(2, "BC", "Victoria", "America/Vancouver"),
        MANITOBA(3, "MB", "Winnipeg", "America/Winnipeg"),
        NEW_BRUNSWICK(4, "NB", "Fredericton", "America/Halifax"),
        NEWFOUNDLAND(5, "NF", "St.John's", "America/St_Johns"),
        NORTHWEST_TERRITORIES(13, "NT", "Yellowknife", "America/Yellowknife"),
        NOVA_SCOTIA(7, "NS", "Halifax", "America/Halifax"),
        NUNAVUT(14, "NU", "Iqaluit", "America/Iqaluit"),
        ONTARIO(8, "ON", "Ottawa", "America/Toronto"),
        PRINCE_EDWARD_ISLAND(9, "PE", "Charlottetown", "America/Halifax"),
        QUEBEC(10, "QC", "Quevec", "America/Montreal"),
        SASKATCHEWAN(11, "SK", "Regina", "America/Regina"),
        YUKON_TERRITORY(12, "YT", "Whitehorse", "America/Whitehorse")
    }

    override fun get(province: String): String? {
        return timezones.get(province)
    }

    override fun getCapital(): String {
        return CanadianProvince.ONTARIO.timezone
    }

    companion object {

        private val timezones = Tri<String>()

        init {
            for (province in CanadianProvince.values()) {
                val timezone = province.timezone
                timezones.insert(province.name.toLowerCase(), timezone)
                timezones.insert(province.abbreviation.toLowerCase(), timezone)
                timezones.insert(province.capital, timezone)
            }
        }
    }
}
