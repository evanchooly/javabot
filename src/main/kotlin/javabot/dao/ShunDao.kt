package javabot.dao

import com.google.inject.Inject
import dev.morphia.Datastore
import dev.morphia.query.experimental.filters.Filters
import javabot.model.Shun
import java.time.LocalDateTime

class ShunDao @Inject constructor(ds: Datastore) : BaseDao<Shun>(ds, Shun::class.java) {

    fun isShunned(nick: String): Boolean {
        return getShun(nick) != null
    }

    fun getShun(nick: String): Shun? {
        expireShuns()
        return ds.find(Shun::class.java)
                .filter(Filters.eq("upperNick", nick.toUpperCase()))
                .first()
    }

    private fun expireShuns() {
        ds.find(Shun::class.java)
                .filter(Filters.lt("expiry", LocalDateTime.now()))
                .remove()
    }

    fun addShun(nick: String, until: LocalDateTime) {
        var shun = getShun(nick)
        if (shun == null) {
            shun = Shun()
            shun.nick = nick.toUpperCase()
            shun.expiry = until
            save(shun)
        }
    }
}
