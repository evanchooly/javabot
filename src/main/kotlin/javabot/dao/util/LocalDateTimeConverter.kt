package javabot.dao.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

import xyz.morphia.converters.DateConverter
import xyz.morphia.mapping.MappedField
import xyz.morphia.mapping.MappingException

class LocalDateTimeConverter : DateConverter(LocalDateTime::class.java) {

    @Throws(MappingException::class)
    override fun decode(targetClass: Class<*>?, value: Any?, optionalExtraInfo: MappedField?): Any? {
        val date = super.decode(targetClass, value, optionalExtraInfo) as Date
        val instant = Instant.ofEpochMilli(date.time)
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
    }

    override fun encode(value: Any?, optionalExtraInfo: MappedField?): Any? {
        if (value == null) {
            return null
        }
        return Date((value as LocalDateTime).toInstant(ZoneOffset.UTC).toEpochMilli())
    }
}
