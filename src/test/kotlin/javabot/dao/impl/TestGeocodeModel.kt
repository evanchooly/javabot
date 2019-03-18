package javabot.dao.impl

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import javabot.dao.geocode.model.GeocodeResponse
import org.testng.annotations.Test

class TestGeocodeModel {
    @Test
    fun testDeserializeGeocodeModel() {
        val mapper=ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        this::class.java.getResourceAsStream("/geocode-response.json").use { inputStream ->
            println(mapper.readValue(inputStream, GeocodeResponse::class.java))
        }
    }

    @Test
    fun testAssignment() {
        val (lat, long) = emptyList<Double>()
    }
}