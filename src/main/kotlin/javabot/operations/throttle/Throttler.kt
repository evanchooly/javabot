package javabot.operations.throttle

import com.antwerkz.sofia.Sofia
import com.jayway.awaitility.Awaitility
import com.jayway.awaitility.core.ConditionTimeoutException
import javabot.Javabot
import javabot.dao.AdminDao
import javabot.dao.BaseDao
import javabot.dao.ConfigDao
import javabot.dao.NickServDao
import javabot.model.JavabotUser
import javabot.model.ThrottleItem
import javabot.model.criteria.ThrottleItemCriteria
import org.mongodb.morphia.Datastore
import java.time.Duration.between
import java.time.LocalDateTime.now
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Provider

class Throttler @Inject constructor(
        ds: Datastore,
        var configDao: ConfigDao,
        var adminDao: AdminDao,
        var nickServDao: NickServDao,
        var bot: Provider<Javabot> ) :
        BaseDao<ThrottleItem>(ds, ThrottleItem::class.java) {

    /**
     * Check if a user is currently throttled or not.

     * @return true if the user is currently throttled and ought to be ignored, false otherwise.
     */
    fun isThrottled(user: JavabotUser): Boolean {
        if (!adminDao.isAdmin(user)) {
            validateNickServAccount(user)
            ds.save(ThrottleItem(user.nick))
            val criteria = ThrottleItemCriteria(ds)
            criteria.user(user.nick)
            return criteria.query().countAll() > configDao.get().throttleThreshold
        }
        return false
    }

    private fun validateNickServAccount(user: JavabotUser) {
        val info = AtomicReference(nickServDao.find(user.nick))
        if (info.get() == null) {
            bot.get().message("NickServ", "info " + user.nick)
            Sofia.logWaitingForNickserv(user.nick)
            try {
                Awaitility.await()
                        .atMost(15, TimeUnit.SECONDS)
                        .until<Boolean> {
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
        if (between(nickServInfo.registered, now()).toDays() < configDao.get().minimumNickServAge) {
            throw NickServViolationException(Sofia.accountTooNew())
        }
    }
}
