package javabot.dao.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

import org.mongodb.morphia.converters.DateConverter
import org.mongodb.morphia.mapping.MappedField
import org.mongodb.morphia.mapping.MappingException

public class LocalDateTimeConverter : DateConverter(LocalDateTime::class.java) {

    Throws(MappingException::class)
    override fun decode(targetClass: Class<Any>?, `val`: Any?, optionalExtraInfo: MappedField?): Any {
        val date = super.decode(targetClass, `val`, optionalExtraInfo) as Date
        val instant = Instant.ofEpochMilli(date.time)
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
    }

    override fun encode(value: Any?, optionalExtraInfo: MappedField?): Any? {
        if (value == null) {
            return null
        }
        return Date((value as LocalDateTime?).toInstant(ZoneOffset.UTC).toEpochMilli())
    }
}
