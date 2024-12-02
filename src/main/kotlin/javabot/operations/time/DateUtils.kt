package javabot.operations.time

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("MMMM dd")
    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm aa")

    fun getCurrentDateAtZone(timezone: String): String {
        val date = LocalDateTime.now()
        val formattedDate = dateFormatter.withZone(ZoneId.of(timezone)).format(date)
        val formattedTime = timeFormatter.withZone(ZoneId.of(timezone)).format(date).lowercase()
        return "$formattedDate @ $formattedTime"
    }
}
