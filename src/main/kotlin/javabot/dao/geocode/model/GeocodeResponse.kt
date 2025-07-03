package javabot.dao.geocode.model

data class GeocodeResponse(var results: List<GeocodeLocation>? = null)

data class GeocodeLocation(
    var address_components: List<GeocodeAddressComponent>? = null,
    var formatted_address: String? = null,
    var geometry: GeocodeGeometry? = null,
    var place_id: String? = null,
    var types: List<String>? = null,
)

data class GeocodeAddressComponent(
    var long_name: String? = null,
    var short_name: String? = null,
    var types: List<String>? = null,
)

data class GeocodeGeometry(var location: GeocodeBoundary? = null)

data class GeocodeBoundary(var lat: Double? = null, var lng: Double? = null)
