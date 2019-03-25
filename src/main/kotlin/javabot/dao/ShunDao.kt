package javabot.dao

import com.google.inject.Inject
import java.time.LocalDateTime

import javabot.model.Shun
import javabot.model.criteria.ShunCriteria
import dev.morphia.Datastore

class ShunDao @Inject constructor(ds: Datastore) : BaseDao<Shun>(ds, Shun::class.java) {

    fun isShunned(nick: String): Boolean {
        return getShun(nick) != null
    }

    fun getShun(nick: String): Shun? {
        expireShuns()
        val criteria = ShunCriteria(ds)
        criteria.upperNick().equal(nick.toUpperCase())
        return criteria.query().get()
    }

    private fun expireShuns() {
        val criteria = ShunCriteria(ds)
        criteria.expiry().lessThan(LocalDateTime.now())
        ds.delete(criteria.query())
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
