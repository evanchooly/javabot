package javabot.operations.time

interface Timezones {
    fun get(province: String): String?
    fun getCapital(): String
}
