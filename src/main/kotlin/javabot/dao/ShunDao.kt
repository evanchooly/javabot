package javabot.dao

import com.google.inject.Inject
import dev.morphia.Datastore
import dev.morphia.DeleteOptions
import java.time.LocalDateTime
import java.util.Locale
import javabot.model.Shun
import javabot.model.criteria.ShunCriteria.Companion.expiry
import javabot.model.criteria.ShunCriteria.Companion.upperNick

class ShunDao @Inject constructor(ds: Datastore) : BaseDao<Shun>(ds, Shun::class.java) {

    fun isShunned(nick: String): Boolean {
        return getShun(nick) != null
    }

    fun getShun(nick: String): Shun? {
        expireShuns()
        return ds.find(Shun::class.java)
            .filter(upperNick().eq(nick.uppercase(Locale.getDefault())))
            .first()
    }

    private fun expireShuns() {
        ds.find(Shun::class.java)
            .filter(expiry().lt(LocalDateTime.now()))
            .delete(DeleteOptions().multi(true))
    }

    fun addShun(nick: String, until: LocalDateTime) {
        var shun = getShun(nick)
        if (shun == null) {
            shun = Shun()
            shun.nick = nick.uppercase(Locale.getDefault())
            shun.expiry = until
            save(shun)
        }
    }
}
