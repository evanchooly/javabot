package javabot.model

import xyz.morphia.annotations.Entity
import java.io.Serializable
import java.text.DecimalFormat
import java.time.LocalDateTime

class Activity(val channel: String, val count: Long, val end: LocalDateTime,
               val start: LocalDateTime, var total: Long) : Serializable {

    fun getPercent(): String {
        return DecimalFormat("##0.00").format(100.0 * count / total)
    }
}
