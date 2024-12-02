package javabot.dao

import dev.morphia.Datastore
import jakarta.inject.Inject
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javabot.model.JavabotUser
import javabot.model.NickServInfo

class TestNickServDao @Inject constructor(ds: Datastore) : NickServDao(ds) {

    override fun find(name: String): NickServInfo {
        val nickServInfo = NickServInfo(JavabotUser(name))
        nickServInfo.registered = LocalDateTime.now().minus(30, ChronoUnit.DAYS)
        return nickServInfo
    }
}
