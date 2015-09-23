package javabot.operations.throttle

import com.antwerkz.sofia.Sofia
import com.jayway.awaitility.Awaitility
import com.jayway.awaitility.core.ConditionTimeoutException
import javabot.dao.AdminDao
import javabot.dao.BaseDao
import javabot.dao.ConfigDao
import javabot.dao.NickServDao
import javabot.model.NickServInfo
import javabot.model.ThrottleItem
import javabot.model.criteria.ThrottleItemCriteria
import org.pircbotx.PircBotX
import org.pircbotx.User

import javax.inject.Inject
import javax.inject.Provider
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

import java.time.Duration.between
import java.time.LocalDateTime.now

public class Throttler protected constructor() : BaseDao<ThrottleItem>(ThrottleItem::class.java) {
    Inject
    private val configDao: ConfigDao? = null

    Inject
    private val adminDao: AdminDao? = null

    Inject
    private val nickServDao: NickServDao? = null

    Inject
    private val ircBot: Provider<PircBotX>? = null

    /**
     * Check if a user is currently throttled or not.

     * @return true if the user is currently throttled and ought to be ignored, false otherwise.
     */
    public fun isThrottled(user: User): Boolean {
        if (!adminDao!!.isAdmin(user)) {
            validateNickServAccount(user)
            ds.save(ThrottleItem(user))
            val criteria = ThrottleItemCriteria(ds)
            criteria.user(user.nick)
            return criteria.query().countAll() > configDao!!.get()!!.throttleThreshold
        }
        return false
    }

    private fun validateNickServAccount(user: User) {
        val info = AtomicReference(nickServDao!!.find(user.nick))
        if (info.get() == null) {
            ircBot!!.get().sendIRC().message("NickServ", "info " + user.nick)
            Sofia.logWaitingForNickserv(user.nick)
            try {
                Awaitility.await().atMost(15, TimeUnit.SECONDS).until<Any> {
                    info.set(nickServDao.find(user.nick))
                    info.get() != null
                }
            } catch (e: ConditionTimeoutException) {
                Sofia.logNoNickservEntry(user.nick)
                throw NickServViolationException(Sofia.unknownUser())
            }

        }
        val nickServInfo = info.get()
        if (nickServInfo == null) {
            Sofia.logNoNickservEntry(user.nick)
            throw NickServViolationException(Sofia.unknownUser())
        }
        if (nickServInfo.registered == null || between(nickServInfo.registered, now()).toDays() < configDao!!.get()!!.minimumNickServAge) {
            throw NickServViolationException(Sofia.accountTooNew())
        }
    }
}
