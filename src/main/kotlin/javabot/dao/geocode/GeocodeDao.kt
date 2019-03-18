package javabot.dao.geocode

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import javabot.JavabotConfig
import javabot.dao.geocode.model.GeocodeResponse
import org.apache.http.client.fluent.Request

class GeocodeDao @Inject constructor(private val javabotConfig: JavabotConfig) {
    val baseUrl = "https://maps.googleapis.com/maps/api/geocode/json?key=${javabotConfig.googleAPI()}&address="
    fun getLatitudeAndLongitudeFor(place: String): GeoLocation? {
        return if (javabotConfig.googleAPI().isNotEmpty() && place.isNotEmpty()) {
            val mapper = ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            val location = place.replace(" ", "+")
            val geocodeResponse = Request
                    .Get("${baseUrl}$location")
                    .execute()
                    .returnContent().asString()
            val response = mapper.readValue(geocodeResponse, GeocodeResponse::class.java)
            val geoLocation = response.results?.get(0)?.geometry?.location
            if (geoLocation != null) {
                GeoLocation(
                        geoLocation.lat ?: 0.0,
                        geoLocation.lng ?: 0.0,
                        response.results?.get(0)?.formatted_address ?: "")
            } else {
                null
            }
        } else {
            null
        }
    }
}

data class GeoLocation(val latitude: Double, val longitude: Double, val address: String)