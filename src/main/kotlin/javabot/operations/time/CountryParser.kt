package javabot.operations.time

public class CountryParser {

    public fun getCountry(key: String): Country {
        return tri.get(key)
    }

    companion object {
        private val tri = Tri<Country>()

        init {
            for (country in Country.values()) {
                tri.insert(country.name, country)
                tri.insert(country.abbreviation, country)
            }
        }
    }
}
