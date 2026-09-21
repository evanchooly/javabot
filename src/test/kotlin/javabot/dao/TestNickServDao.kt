package javabot.dao

import dev.morphia.Datastore
import jakarta.annotation.Priority
import jakarta.enterprise.inject.Alternative
import jakarta.inject.Inject
import jakarta.inject.Provider
import jakarta.inject.Singleton
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javabot.model.JavabotUser
import javabot.model.NickServInfo
import org.pircbotx.PircBotX

@Alternative
@Priority(1)
@Singleton
class TestNickServDao @Inject constructor(ds: Datastore) : NickServDao(ds) {
    @Inject protected lateinit var ircBot: Provider<PircBotX>

    override fun find(name: String): NickServInfo {
        val nickServInfo = NickServInfo(JavabotUser(name))
        nickServInfo.registered = LocalDateTime.now().minus(30, ChronoUnit.DAYS)
        return nickServInfo
    }
}
