package javabot.model

import java.io.Serializable
import java.text.DecimalFormat
import java.time.LocalDateTime

public class Activity(public val channel: String, public val count: Long, public val end: LocalDateTime, public val start: LocalDateTime, public var total: Long) : Serializable {

    public fun getPercent(): String {
        return DecimalFormat("##0.00").format(100.0 * count / total)
    }
}
